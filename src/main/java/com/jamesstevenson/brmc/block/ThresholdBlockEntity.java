package com.jamesstevenson.brmc.block;

import java.util.UUID;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.gate.DestinationVolume;
import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.gate.LinkedDimensionPortal;
import com.jamesstevenson.brmc.gate.LinkedVolumeBackend;
import com.jamesstevenson.brmc.gate.PortalCrossTracker;
import com.jamesstevenson.brmc.gate.SeamlessGate;
import com.jamesstevenson.brmc.gate.SeamlessGateService;
import com.jamesstevenson.brmc.worldgen.YellowMonoLayout;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ThresholdBlockEntity extends BlockEntity implements LinkedDimensionPortal {
	private static final int SAMPLE_RETRY_TICKS = 20;
	private static final int SAMPLE_REFRESH_TICKS = 200;
	private static final double PRELOAD_RANGE = 48.0;

	private final PortalCrossTracker crossTracker = new PortalCrossTracker();
	private DestinationVolume destVolume = DestinationVolume.EMPTY;
	private boolean destTicketed;
	private int sampleCooldown;
	private int waitThroughTicks;
	private UUID pendingCross;

	public ThresholdBlockEntity(BlockPos pos, BlockState state) {
		super(BrmcBlockEntities.LINKED_VOLUME, pos, state);
	}

	@Override
	public GateKind kind() {
		if (this.getBlockState().getBlock() instanceof ThresholdBlock threshold) {
			return threshold.kind();
		}

		return GateKind.COMMONS;
	}

	@Override
	public ResourceKey<Level> from() {
		return BrmcDimensions.FIRST;
	}

	@Override
	public ResourceKey<Level> to() {
		SeamlessGate gate = gate();
		return gate == null ? BrmcDimensions.FALSE_FIRST : gate.to();
	}

	@Override
	public BlockPos threshold() {
		return this.worldPosition;
	}

	@Override
	public Direction facing() {
		return this.kind().planeFacing();
	}

	@Override
	public DestinationVolume destVolume() {
		return this.destVolume;
	}

	public boolean isPortalAnchor() {
		if (this.level == null) {
			return this.worldPosition.getY() == YellowMonoLayout.CARPET_Y;
		}

		return !(this.level.getBlockState(this.worldPosition.below()).getBlock() instanceof ThresholdBlock);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ThresholdBlockEntity be) {
		if (!(level instanceof ServerLevel serverLevel) || !be.isPortalAnchor()) {
			return;
		}

		if (!BrmcDimensions.isFirst(serverLevel)) {
			return;
		}

		SeamlessGate gate = be.gate();
		if (gate == null) {
			return;
		}

		boolean nearby = serverLevel.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, PRELOAD_RANGE, false) != null;
		if (nearby || be.kind().hasLinkedVolumeThisPass()) {
			if (!be.destTicketed) {
				LinkedVolumeBackend.preloadDestination(serverLevel, gate);
				be.destTicketed = true;
			}

			be.maybeSample(serverLevel, gate);
		}

		if (!be.kind().planeCrossOnly()) {
			return;
		}

		AABB scan = new AABB(pos).inflate(1.2, 2.5, 1.2);
		for (ServerPlayer player : serverLevel.getEntitiesOfClass(ServerPlayer.class, scan)) {
			be.tryPlaneCross(serverLevel, player, gate);
		}
	}

	private void maybeSample(ServerLevel level, SeamlessGate gate) {
		if (!this.kind().hasLinkedVolumeThisPass()) {
			return;
		}

		if (this.sampleCooldown > 0) {
			this.sampleCooldown--;
			return;
		}

		DestinationVolume sampled = LinkedVolumeBackend.sampleDestination(level, gate);
		this.sampleCooldown = sampled.isReady() ? SAMPLE_REFRESH_TICKS : SAMPLE_RETRY_TICKS;
		if (sampled.voxelCount() == this.destVolume.voxelCount() && sampled.isReady() == this.destVolume.isReady()) {
			return;
		}

		if (sampled.isEmpty() && this.destVolume.isEmpty()) {
			return;
		}

		this.destVolume = sampled;
		this.setChanged();
		level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
	}

	private void tryPlaneCross(ServerLevel level, ServerPlayer player, SeamlessGate gate) {
		Vec3 pose = player.position();
		if (!PortalCrossTracker.inDoorway(pose, this.worldPosition, this.facing())) {
			this.crossTracker.forget(player.getUUID());
			if (player.getUUID().equals(this.pendingCross)) {
				this.pendingCross = null;
				this.waitThroughTicks = 0;
			}

			return;
		}

		double signed = PortalCrossTracker.signedDistance(pose, this.worldPosition, this.facing());
		if (this.crossTracker.crossedTowardDest(player.getUUID(), signed)) {
			this.pendingCross = player.getUUID();
			this.waitThroughTicks = 0;
		}

		if (signed < 0.0 || !player.getUUID().equals(this.pendingCross)) {
			return;
		}

		SeamlessGateService.ensureOpening(level, gate);
		if (LinkedVolumeBackend.destinationReady(level, gate, player) || this.waitThroughTicks >= 20) {
			this.waitThroughTicks = 0;
			this.pendingCross = null;
			this.crossTracker.forget(player.getUUID());
			SeamlessGateService.traverse(player, level, gate);
			return;
		}

		this.waitThroughTicks++;
	}

	private SeamlessGate gate() {
		return SeamlessGateService.gate(this.kind(), this.worldPosition, this.facing());
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		this.destVolume.write(output);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.destVolume = DestinationVolume.read(input);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}
}

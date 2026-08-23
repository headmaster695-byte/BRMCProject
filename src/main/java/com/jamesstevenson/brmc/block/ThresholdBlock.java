package com.jamesstevenson.brmc.block;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.gate.SeamlessGate;
import com.jamesstevenson.brmc.gate.SeamlessGateService;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jspecify.annotations.Nullable;

/**
 * Invisible walk-through plane. See-through comes from the linked volume
 * plus {@code LinkedVolumeRenderer}. No “Entering” copy.
 */
public class ThresholdBlock extends net.minecraft.world.level.block.Block implements EntityBlock {
	private final GateKind kind;

	public ThresholdBlock(GateKind kind, BlockBehaviour.Properties properties) {
		super(properties);
		this.kind = kind;
	}

	public GateKind kind() {
		return this.kind;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ThresholdBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
			attemptTraverse(serverLevel, serverPlayer, pos);
			return InteractionResult.SUCCESS_SERVER;
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	protected void entityInside(
		BlockState state,
		Level level,
		BlockPos pos,
		Entity entity,
		InsideBlockEffectApplier effectApplier,
		boolean isPrecise
	) {
		if (level instanceof ServerLevel serverLevel && entity instanceof ServerPlayer player) {
			attemptTraverse(serverLevel, player, pos);
		}
	}

	private void attemptTraverse(ServerLevel level, ServerPlayer player, BlockPos pos) {
		if (!BrmcDimensions.isFirst(level)) {
			return;
		}

		Direction facing = this.kind.planeFacing();
		SeamlessGate gate = SeamlessGateService.gate(this.kind, pos, facing);
		if (gate == null) {
			return;
		}

		SeamlessGateService.ensureOpening(level, gate);
		SeamlessGateService.traverse(player, level, gate);
	}
}

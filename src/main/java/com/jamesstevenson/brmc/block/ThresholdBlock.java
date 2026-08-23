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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Placeable First-pocket threshold. Crossing is the hook an Immersive Portals
 * backend should replace with a see-through opening. No "Entering" copy.
 */
public class ThresholdBlock extends Block {
	private final GateKind kind;

	public ThresholdBlock(GateKind kind, BlockBehaviour.Properties properties) {
		super(properties);
		this.kind = kind;
	}

	public GateKind kind() {
		return this.kind;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
			attemptTraverse(serverLevel, serverPlayer, pos, player.getDirection());
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
			attemptTraverse(serverLevel, player, pos, player.getDirection());
		}
	}

	private void attemptTraverse(ServerLevel level, ServerPlayer player, BlockPos pos, Direction facing) {
		if (!BrmcDimensions.isFirst(level)) {
			return;
		}

		SeamlessGate gate = SeamlessGateService.gate(this.kind, pos, facing);
		if (gate == null) {
			return;
		}

		SeamlessGateService.ensureOpening(level, gate);
		SeamlessGateService.traverse(player, level, gate);
	}
}

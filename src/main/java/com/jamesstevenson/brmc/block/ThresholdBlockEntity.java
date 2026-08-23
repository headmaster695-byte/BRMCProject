package com.jamesstevenson.brmc.block;

import com.jamesstevenson.brmc.gate.GateKind;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ThresholdBlockEntity extends BlockEntity {
	public ThresholdBlockEntity(BlockPos pos, BlockState state) {
		super(BrmcBlockEntities.LINKED_VOLUME, pos, state);
	}

	public GateKind kind() {
		if (this.getBlockState().getBlock() instanceof ThresholdBlock threshold) {
			return threshold.kind();
		}

		return GateKind.COMMONS;
	}
}

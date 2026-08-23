package com.jamesstevenson.brmc.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Reserved hum-buzz stub. First pitch later; Second is lower / wetter when
 * audio lands. No entities spawn. No audio in this pass.
 */
public class TrofferBlockEntity extends BlockEntity {
	public TrofferBlockEntity(BlockPos pos, BlockState state) {
		super(BrmcBlockEntities.TROFFER, pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TrofferBlockEntity be) {
	}
}

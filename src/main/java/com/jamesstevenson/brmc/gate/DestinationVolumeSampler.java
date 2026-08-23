package com.jamesstevenson.brmc.gate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Samples the linked dest dimension at the same world coordinates as the
 * threshold (identity transform). That is the dest volume the player walks
 * into — not First wallpaper painted to look like dest.
 */
public final class DestinationVolumeSampler {
	public static final int DEPTH = 8;
	public static final int HALF_WIDTH = 2;
	public static final int Y_MIN = -1;
	public static final int Y_MAX = 3;

	private DestinationVolumeSampler() {
	}

	public static DestinationVolume sample(ServerLevel destination, BlockPos threshold, Direction facing) {
		if (destination == null || threshold == null || facing == null) {
			return DestinationVolume.EMPTY;
		}

		List<Integer> packed = new ArrayList<>();
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		boolean complete = true;

		for (int along = 1; along <= DEPTH; along++) {
			for (int lateral = -HALF_WIDTH; lateral <= HALF_WIDTH; lateral++) {
				for (int y = Y_MIN; y <= Y_MAX; y++) {
					int dx = facing.getStepX() * along + facing.getStepZ() * lateral;
					int dz = facing.getStepZ() * along + facing.getStepX() * lateral;
					cursor.set(threshold.getX() + dx, threshold.getY() + y, threshold.getZ() + dz);
					if (!destination.hasChunkAt(cursor)) {
						complete = false;
						continue;
					}

					BlockState state = destination.getBlockState(cursor);
					if (state.isAir()) {
						continue;
					}

					packed.add(dx);
					packed.add(y);
					packed.add(dz);
					packed.add(Block.getId(state));
				}
			}
		}

		int[] array = new int[packed.size()];
		for (int i = 0; i < packed.size(); i++) {
			array[i] = packed.get(i);
		}

		return new DestinationVolume(destination.dimension().identifier().toString(), array, complete);
	}
}

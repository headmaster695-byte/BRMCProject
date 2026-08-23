package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.block.BrmcBlocks;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Third P0 cold garage. Empty-lot grammar: sodium fluorescents, wet concrete,
 * lying stall paint. Wear is noise. Ramp door is a mouth liar — not EXIT.
 * No chainlink / crate / headlight this pass. No entities.
 */
public final class ColdGaragePalette {
	private ColdGaragePalette() {
	}

	public static BlockState floor() {
		return BrmcBlocks.THIRD_CONCRETE.defaultBlockState();
	}

	public static BlockState fill() {
		return BrmcBlocks.THIRD_CONCRETE.defaultBlockState();
	}

	public static BlockState field(int worldX, int worldZ) {
		if (isMouthQuiet(worldX, worldZ)) {
			return BrmcBlocks.THIRD_CONCRETE.defaultBlockState();
		}

		if (isStallPaint(worldX, worldZ)) {
			return BrmcBlocks.THIRD_STALL_PAINT.defaultBlockState();
		}

		if (isWetConcrete(worldX, worldZ)) {
			return BrmcBlocks.THIRD_CONCRETE_WET.defaultBlockState();
		}

		if (isDrain(worldX, worldZ)) {
			return BrmcBlocks.THIRD_DRAIN.defaultBlockState();
		}

		return BrmcBlocks.THIRD_CONCRETE.defaultBlockState();
	}

	public static BlockState wall(int worldX, int worldZ) {
		return (isPipeColumn(worldX, worldZ)
			? BrmcBlocks.THIRD_PIPE
			: BrmcBlocks.THIRD_PILLAR).defaultBlockState();
	}

	public static BlockState prop(int worldX, int worldZ) {
		return (Math.floorMod(worldX + worldZ, 2) == 0
			? BrmcBlocks.THIRD_PILLAR
			: BrmcBlocks.THIRD_PIPE).defaultBlockState();
	}

	public static BlockState ceiling(boolean light, int worldX, int worldZ) {
		if (light) {
			return (isDeadFluorescent(worldX, worldZ)
				? BrmcBlocks.THIRD_FLUORESCENT_DEAD
				: BrmcBlocks.THIRD_FLUORESCENT).defaultBlockState();
		}

		return BrmcBlocks.THIRD_CONCRETE.defaultBlockState();
	}

	public static BlockState doorSkin(int worldX, int worldZ) {
		return (isRampFrame(worldX, worldZ)
			? BrmcBlocks.THIRD_DOOR_FRAME
			: BrmcBlocks.THIRD_DOOR_RAMP).defaultBlockState();
	}

	/**
	 * Liar mouth on the glance cell only (Clark XYZ / {@code /brmc third}).
	 * Not a live gate and not honest EXIT signage.
	 */
	public static boolean isRampMouth(int worldX, int worldZ) {
		return YellowMonoLayout.cellCoord(worldX) == 2
			&& YellowMonoLayout.cellCoord(worldZ) == 2
			&& YellowMonoLayout.localInCell(worldX) == 7
			&& YellowMonoLayout.localInCell(worldZ) >= 2
			&& YellowMonoLayout.localInCell(worldZ) <= 5;
	}

	public static boolean isRampFrame(int worldX, int worldZ) {
		if (!isRampMouth(worldX, worldZ)) {
			return false;
		}

		int localZ = YellowMonoLayout.localInCell(worldZ);
		return localZ == 2 || localZ == 5;
	}

	/**
	 * Misaligned stall noise — prime hash, never cell-local 3–4 parking lanes.
	 */
	public static boolean isStallPaint(int worldX, int worldZ) {
		if (!destWearColumn(worldX, worldZ)) {
			return false;
		}

		int localX = YellowMonoLayout.localInCell(worldX);
		int localZ = YellowMonoLayout.localInCell(worldZ);
		if (localX == 3 || localX == 4 || localZ == 3 || localZ == 4) {
			return false;
		}

		return Math.floorMod(worldX * 7 + worldZ * 13, 41) == 0;
	}

	public static boolean isWetConcrete(int worldX, int worldZ) {
		return destWearColumn(worldX, worldZ)
			&& !isStallPaint(worldX, worldZ)
			&& Math.floorMod(worldX * 11 + worldZ * 19, 53) == 0;
	}

	public static boolean isDrain(int worldX, int worldZ) {
		return destWearColumn(worldX, worldZ)
			&& !isStallPaint(worldX, worldZ)
			&& !isWetConcrete(worldX, worldZ)
			&& Math.floorMod(worldX * 17 + worldZ * 23, 67) == 0;
	}

	public static boolean isDeadFluorescent(int worldX, int worldZ) {
		if (isGlanceCell(worldX, worldZ)) {
			return false;
		}

		return Math.floorMod(worldX * 11 + worldZ * 29, 71) == 0;
	}

	private static boolean isPipeColumn(int worldX, int worldZ) {
		return Math.floorMod(worldX * 3 + worldZ * 5, 4) == 0;
	}

	private static boolean destWearColumn(int worldX, int worldZ) {
		return !isMouthQuiet(worldX, worldZ);
	}

	private static boolean isMouthQuiet(int worldX, int worldZ) {
		if (isRampMouth(worldX, worldZ)) {
			return true;
		}

		return Math.abs(worldX - 23) <= 1 && worldZ >= 18 && worldZ <= 21;
	}

	private static boolean isGlanceCell(int worldX, int worldZ) {
		return YellowMonoLayout.cellCoord(worldX) == 2 && YellowMonoLayout.cellCoord(worldZ) == 2;
	}
}

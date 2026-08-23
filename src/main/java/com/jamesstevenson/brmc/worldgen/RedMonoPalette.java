package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.block.BrmcBlocks;
import com.jamesstevenson.brmc.gate.LinkedOpenings;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Second P0 crimson mono. Same grammar as First: wallpaper A/B/C seam phase,
 * wear as sparse noise, debris fill never gray. No safelight. No yellow-bleed.
 */
public final class RedMonoPalette {
	private RedMonoPalette() {
	}

	public static BlockState floor() {
		return BrmcBlocks.SECOND_DEBRIS_CARPET.defaultBlockState();
	}

	public static BlockState debris() {
		return BrmcBlocks.SECOND_DEBRIS_CARPET.defaultBlockState();
	}

	public static BlockState wallpaper(int worldX, int y, int worldZ) {
		if (y == YellowMonoLayout.CARPET_Y || YellowMonoLayout.chevronDark(worldX, y, worldZ)) {
			return wallpaperSeamPhase(worldX, y, worldZ).defaultBlockState();
		}

		return BrmcBlocks.SECOND_WALLPAPER.defaultBlockState();
	}

	/** B/C are the same print, phase-shifted so a seam does not tile as a landmark. */
	private static Block wallpaperSeamPhase(int worldX, int y, int worldZ) {
		return Math.floorMod(worldX + worldZ * 2 + (y >> 1), 2) == 0
			? BrmcBlocks.SECOND_WALLPAPER_B
			: BrmcBlocks.SECOND_WALLPAPER_C;
	}

	public static BlockState carpet(int worldX, int worldZ) {
		if (isArrivalQuiet(worldX, worldZ)) {
			return BrmcBlocks.SECOND_CARPET.defaultBlockState();
		}

		if (isCarpetMold(worldX, worldZ)) {
			return BrmcBlocks.SECOND_CARPET_MOLD.defaultBlockState();
		}

		if (isCarpetTorn(worldX, worldZ)) {
			return BrmcBlocks.SECOND_CARPET_TORN.defaultBlockState();
		}

		return BrmcBlocks.SECOND_CARPET.defaultBlockState();
	}

	public static BlockState ceiling(boolean light, int worldX, int worldZ) {
		if (light) {
			return (isDeadTroffer(worldX, worldZ)
				? BrmcBlocks.SECOND_TROFFER_DEAD
				: BrmcBlocks.SECOND_TROFFER).defaultBlockState();
		}

		return BrmcBlocks.SECOND_CEILING_TILE.defaultBlockState();
	}

	public static BlockState doorSkin(int worldX, int worldZ) {
		return (YellowMonoLayout.isVestibuleDoorFrame(worldX, worldZ)
			? BrmcBlocks.SECOND_DOOR_FRAME
			: BrmcBlocks.SECOND_DOOR_COMMERCIAL).defaultBlockState();
	}

	public static boolean isDestDoorPlane(int worldX, int worldZ) {
		return YellowMonoLayout.isVestibuleDoor2Plane(worldX, worldZ);
	}

	/**
	 * Sparse dest wear. Never a landmark and never adjacent to the vestibule
	 * arrival so mold/torn do not teach the gate.
	 */
	public static boolean isCarpetMold(int worldX, int worldZ) {
		return destWearColumn(worldX, worldZ) && Math.floorMod(worldX * 13 + worldZ * 17, 43) == 0;
	}

	public static boolean isCarpetTorn(int worldX, int worldZ) {
		return destWearColumn(worldX, worldZ)
			&& !isCarpetMold(worldX, worldZ)
			&& Math.floorMod(worldX * 19 + worldZ * 23, 61) == 0;
	}

	public static boolean isDeadTroffer(int worldX, int worldZ) {
		if (isArrivalCell(worldX, worldZ)) {
			return false;
		}

		return Math.floorMod(worldX * 11 + worldZ * 29, 71) == 0;
	}

	private static boolean destWearColumn(int worldX, int worldZ) {
		return !isArrivalQuiet(worldX, worldZ);
	}

	private static boolean isArrivalQuiet(int worldX, int worldZ) {
		if (isDestDoorPlane(worldX, worldZ) || LinkedOpenings.isDestReturnColumn(DestClimate.RED_MONO, worldX, worldZ)) {
			return true;
		}

		return LinkedOpenings.forDestClimate(DestClimate.RED_MONO)
			.map(opening -> {
				for (var identity : opening.identities()) {
					if (Math.abs(identity.getX() - worldX) <= 1 && Math.abs(identity.getZ() - worldZ) <= 1) {
						return true;
					}
				}

				return false;
			})
			.orElse(false);
	}

	private static boolean isArrivalCell(int worldX, int worldZ) {
		return LinkedOpenings.forDestClimate(DestClimate.RED_MONO)
			.map(opening -> {
				for (var identity : opening.identities()) {
					if (YellowMonoLayout.cellCoord(worldX) == YellowMonoLayout.cellCoord(identity.getX())
						&& YellowMonoLayout.cellCoord(worldZ) == YellowMonoLayout.cellCoord(identity.getZ())) {
						return true;
					}
				}

				return false;
			})
			.orElse(false);
	}
}

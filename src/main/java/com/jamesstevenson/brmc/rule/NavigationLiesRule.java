package com.jamesstevenson.brmc.rule;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;

import net.minecraft.world.level.Level;

/**
 * Maps, compass, and coordinates lie or fail in First. Not a tutorial.
 */
public final class NavigationLiesRule {
	private NavigationLiesRule() {
	}

	public static void initialize() {
	}

	public static boolean shouldLie(Level level) {
		if (level == null) {
			return false;
		}

		return BrmcDimensions.isFirst(level)
			|| BrmcDimensions.SECOND.equals(level.dimension())
			|| BrmcDimensions.isSubDimension(level.dimension());
	}

	public static boolean shouldLieCoordinates(Level level) {
		return shouldLie(level);
	}

	public static boolean shouldLieCompass(Level level) {
		return shouldLie(level);
	}

	public static boolean shouldLieMaps(Level level) {
		return shouldLie(level);
	}

	/** Stable wrong offset so F3 looks populated but cannot navigate. */
	public static int coordinateShift(Level level) {
		if (!shouldLieCoordinates(level)) {
			return 0;
		}

		return 4096 + Math.floorMod(level.dimension().identifier().hashCode(), 2048);
	}
}

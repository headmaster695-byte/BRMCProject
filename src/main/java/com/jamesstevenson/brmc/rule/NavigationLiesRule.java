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
		return BrmcDimensions.isFirst(level);
	}
}

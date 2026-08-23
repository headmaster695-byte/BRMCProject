package com.jamesstevenson.brmc.rule;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;

import net.minecraft.world.level.Level;

/**
 * Gameplay rule, not a tutorial: maps in First do not tell the truth.
 * The mixin cancels vanilla map updates so charts freeze / go stale.
 */
public final class MapsLieRule {
	private MapsLieRule() {
	}

	public static void initialize() {
	}

	public static boolean shouldLie(Level level) {
		return NavigationLiesRule.shouldLie(level);
	}
}

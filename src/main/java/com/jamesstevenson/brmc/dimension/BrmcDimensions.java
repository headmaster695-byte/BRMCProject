package com.jamesstevenson.brmc.dimension;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * Design-locked dimension set for the First Dimension pass.
 * Do not add more destinations here without a design lock.
 */
public final class BrmcDimensions {
	public static final ResourceKey<Level> FIRST = level("first");
	public static final ResourceKey<Level> SECOND = level("second");
	public static final ResourceKey<Level> FALSE_FIRST = level("false_first");

	public static final ResourceKey<DimensionType> FIRST_TYPE = dimensionType("first");
	public static final ResourceKey<DimensionType> SECOND_TYPE = dimensionType("second");
	public static final ResourceKey<DimensionType> FALSE_FIRST_TYPE = dimensionType("false_first");

	public static final ResourceKey<LevelStem> FIRST_STEM = stem("first");
	public static final ResourceKey<LevelStem> SECOND_STEM = stem("second");
	public static final ResourceKey<LevelStem> FALSE_FIRST_STEM = stem("false_first");

	private BrmcDimensions() {
	}

	public static void bootstrap() {
		// Resource keys are the Java-side handle. Actual LevelStem / DimensionType
		// payloads live in data/brmc/dimension* so they stay datapack-correct.
	}

	public static boolean isFirst(Level level) {
		return level != null && FIRST.equals(level.dimension());
	}

	public static boolean isLockedDestination(Level level) {
		if (level == null) {
			return false;
		}

		ResourceKey<Level> dimension = level.dimension();
		return FIRST.equals(dimension) || SECOND.equals(dimension) || FALSE_FIRST.equals(dimension);
	}

	private static ResourceKey<Level> level(String path) {
		return ResourceKey.create(Registries.DIMENSION, BrmcMod.id(path));
	}

	private static ResourceKey<DimensionType> dimensionType(String path) {
		return ResourceKey.create(Registries.DIMENSION_TYPE, BrmcMod.id(path));
	}

	private static ResourceKey<LevelStem> stem(String path) {
		return ResourceKey.create(Registries.LEVEL_STEM, BrmcMod.id(path));
	}
}

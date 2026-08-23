package com.jamesstevenson.brmc.dimension;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * First is the hub. Second is only via the true-exit vestibule.
 * Third is a dest-climate stub beyond Second — not a First pocket and not a
 * new live First / Second plane-cross this pass. Other First exits are
 * sub-dimensions, not Second. False Gate is not a First hop — only via False
 * First. Digital is sealed. Fourth does not exist. Bounded is not a First
 * pocket; apartment janitor goes to Custodial.
 */
public final class BrmcDimensions {
	public static final ResourceKey<Level> FIRST = level("first");
	public static final ResourceKey<Level> SECOND = level("second");
	public static final ResourceKey<Level> THIRD = level("third");
	public static final ResourceKey<Level> FALSE_FIRST = level("false_first");
	public static final ResourceKey<Level> SECOND_FALSE_FIRST = level("second_false_first");
	public static final ResourceKey<Level> BUTTONS = level("buttons");
	public static final ResourceKey<Level> SPIRAL = level("spiral");
	public static final ResourceKey<Level> OUT_OF_BOUNDS = level("out_of_bounds");
	public static final ResourceKey<Level> CUSTODIAL = level("custodial");

	public static final ResourceKey<DimensionType> FIRST_TYPE = dimensionType("first");
	public static final ResourceKey<DimensionType> SECOND_TYPE = dimensionType("second");
	public static final ResourceKey<DimensionType> THIRD_TYPE = dimensionType("third");
	public static final ResourceKey<DimensionType> FALSE_FIRST_TYPE = dimensionType("false_first");
	public static final ResourceKey<DimensionType> SECOND_FALSE_FIRST_TYPE = dimensionType("second_false_first");
	public static final ResourceKey<DimensionType> BUTTONS_TYPE = dimensionType("buttons");
	public static final ResourceKey<DimensionType> SPIRAL_TYPE = dimensionType("spiral");
	public static final ResourceKey<DimensionType> OUT_OF_BOUNDS_TYPE = dimensionType("out_of_bounds");
	public static final ResourceKey<DimensionType> CUSTODIAL_TYPE = dimensionType("custodial");

	public static final ResourceKey<LevelStem> FIRST_STEM = stem("first");
	public static final ResourceKey<LevelStem> SECOND_STEM = stem("second");
	public static final ResourceKey<LevelStem> THIRD_STEM = stem("third");
	public static final ResourceKey<LevelStem> FALSE_FIRST_STEM = stem("false_first");
	public static final ResourceKey<LevelStem> SECOND_FALSE_FIRST_STEM = stem("second_false_first");
	public static final ResourceKey<LevelStem> BUTTONS_STEM = stem("buttons");
	public static final ResourceKey<LevelStem> SPIRAL_STEM = stem("spiral");
	public static final ResourceKey<LevelStem> OUT_OF_BOUNDS_STEM = stem("out_of_bounds");
	public static final ResourceKey<LevelStem> CUSTODIAL_STEM = stem("custodial");

	private BrmcDimensions() {
	}

	public static void bootstrap() {
	}

	public static boolean isFirst(Level level) {
		return level != null && FIRST.equals(level.dimension());
	}

	public static boolean isSubDimension(ResourceKey<Level> dimension) {
		return FALSE_FIRST.equals(dimension)
			|| SECOND_FALSE_FIRST.equals(dimension)
			|| BUTTONS.equals(dimension)
			|| SPIRAL.equals(dimension)
			|| OUT_OF_BOUNDS.equals(dimension)
			|| CUSTODIAL.equals(dimension);
	}

	/** False Gate is reached only through False First, never from First. */
	public static boolean isFalseGate(ResourceKey<Level> dimension) {
		return false;
	}

	/** Digital is sealed. No First pocket opens it. Not registered. */
	public static boolean isDigitalSealed() {
		return true;
	}

	/** Fourth does not exist. Do not invent it. */
	public static boolean inventsFourth() {
		return false;
	}

	/** Bounded is not a First pocket or a First destination. */
	public static boolean isBounded(ResourceKey<Level> dimension) {
		return false;
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

package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.gate.GateKind;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * Authored anchors on First. These are pockets on the yellow-mono hub,
 * not distant dimensions. Digital is sealed from every First pocket.
 * Fourth is not a destination.
 */
public enum FirstPocket {
	CLARK_CHAMBER("first-clark-chamber", null, null, false),
	APARTMENT("first-apartment-pocket", null, null, false),
	UTILITIES("first-utilities", GateKind.UTILITIES, "buttons", false),
	COMMON_EXIT("first-common-exit", GateKind.COMMONS, "false_first", false),
	VESTIBULE("first-vestibule", GateKind.VESTIBULE, "second", false),
	VESTIBULE_OOB("first-vestibule", GateKind.OOB_HOLE, "out_of_bounds", true),
	CURVING_HALL("first-curving-hall", GateKind.CURVING_HALL, "second_false_first", false),
	FALSE_FLOOR("first-false-floor", GateKind.FALSE_FLOOR, "spiral", false),
	FLUORESCENT_DEAD_ZONE("first-fluorescent-dead-zone", null, null, false);

	private final String slug;
	private final @Nullable GateKind gate;
	private final @Nullable String destinationPath;
	private final boolean statefulLate;

	FirstPocket(String slug, @Nullable GateKind gate, @Nullable String destinationPath, boolean statefulLate) {
		this.slug = slug;
		this.gate = gate;
		this.destinationPath = destinationPath;
		this.statefulLate = statefulLate;
	}

	public String slug() {
		return this.slug;
	}

	public @Nullable GateKind gate() {
		return this.gate;
	}

	public boolean statefulLate() {
		return this.statefulLate;
	}

	public boolean opensDigital() {
		return false;
	}

	public @Nullable ResourceKey<Level> destination() {
		if (this.destinationPath == null) {
			return null;
		}

		return switch (this.destinationPath) {
			case "second" -> BrmcDimensions.SECOND;
			case "false_first" -> BrmcDimensions.FALSE_FIRST;
			case "second_false_first" -> BrmcDimensions.SECOND_FALSE_FIRST;
			case "buttons" -> BrmcDimensions.BUTTONS;
			case "spiral" -> BrmcDimensions.SPIRAL;
			case "out_of_bounds" -> BrmcDimensions.OUT_OF_BOUNDS;
			default -> null;
		};
	}
}

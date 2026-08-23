package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.gate.PresentationLock;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * Authored anchors on First. These are pockets on the yellow-mono hub,
 * not distant dimensions. Digital is sealed from every First pocket.
 * Fourth is not a destination. Apartment is not Bounded.
 */
public enum FirstPocket {
	CLARK_CHAMBER(
		"first-clark-chamber",
		null,
		null,
		false,
		LoreThread.SPINE_LEAVE,
		PocketStructure.CLARK_CHAMBER,
		PresentationLock.Crossing.NONE
	),
	/** Habitation lobe. Drywall / bed / kitchen. Not Bounded. */
	APARTMENT(
		"first-apartment-pocket",
		null,
		null,
		false,
		LoreThread.HABITATION_STACK,
		PocketStructure.APARTMENT_LOBE,
		PresentationLock.Crossing.NONE
	),
	/** Rare janitor closet inside the apartment. Leads to Custodial. */
	APARTMENT_JANITOR(
		"first-apartment-pocket",
		GateKind.CUSTODIAL,
		"custodial",
		true,
		LoreThread.MAINTENANCE_STACK,
		PocketStructure.JANITOR_CLOSET,
		PresentationLock.Crossing.RARE_ENCOUNTER
	),
	UTILITIES(
		"first-utilities",
		GateKind.UTILITIES,
		"buttons",
		false,
		LoreThread.MAINTENANCE_STACK,
		PocketStructure.UTILITIES_MOUTH,
		PresentationLock.Crossing.CONTINUOUS_PLANT
	),
	COMMON_EXIT(
		"first-common-exit",
		GateKind.COMMONS,
		"false_first",
		false,
		LoreThread.EXIT_LITERACY_STACK,
		PocketStructure.COMMONS_LITERACY,
		PresentationLock.Crossing.YELLOW_TO_YELLOW
	),
	VESTIBULE(
		"first-vestibule",
		GateKind.VESTIBULE,
		"second",
		false,
		LoreThread.SPINE_LEAVE,
		PocketStructure.VESTIBULE_TRUE_EXIT,
		PresentationLock.Crossing.YELLOW_TO_RED
	),
	/** Hole before door 2 only. Stateful/late. Puzzle chain is not implemented. */
	VESTIBULE_OOB(
		"first-vestibule",
		GateKind.OOB_HOLE,
		"out_of_bounds",
		true,
		LoreThread.FALL_COIL,
		PocketStructure.VESTIBULE_OOB_HOLE,
		PresentationLock.Crossing.DROP
	),
	CURVING_HALL(
		"first-curving-hall",
		GateKind.CURVING_HALL,
		"second_false_first",
		false,
		LoreThread.CURVE_NEST,
		PocketStructure.CURVING_HALL_SEAM,
		PresentationLock.Crossing.INVISIBLE_SEAM
	),
	FALSE_FLOOR(
		"first-false-floor",
		GateKind.FALSE_FLOOR,
		"spiral",
		false,
		LoreThread.FALL_COIL,
		PocketStructure.FALSE_FLOOR_DROP,
		PresentationLock.Crossing.DROP
	),
	FLUORESCENT_DEAD_ZONE(
		"first-fluorescent-dead-zone",
		null,
		null,
		false,
		LoreThread.SPINE_LEAVE,
		PocketStructure.FLUORESCENT_DEAD_RUN,
		PresentationLock.Crossing.NONE
	);

	private final String slug;
	private final @Nullable GateKind gate;
	private final @Nullable String destinationPath;
	private final boolean statefulLate;
	private final LoreThread loreThread;
	private final PocketStructure structure;
	private final PresentationLock.Crossing crossing;

	FirstPocket(
		String slug,
		@Nullable GateKind gate,
		@Nullable String destinationPath,
		boolean statefulLate,
		LoreThread loreThread,
		PocketStructure structure,
		PresentationLock.Crossing crossing
	) {
		this.slug = slug;
		this.gate = gate;
		this.destinationPath = destinationPath;
		this.statefulLate = statefulLate;
		this.loreThread = loreThread;
		this.structure = structure;
		this.crossing = crossing;
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

	public LoreThread loreThread() {
		return this.loreThread;
	}

	public PocketStructure structure() {
		return this.structure;
	}

	public PresentationLock.Crossing crossing() {
		return this.crossing;
	}

	/** No First pocket is Bounded. Apartment is a habitation lobe. */
	public boolean isBounded() {
		return false;
	}

	/** Digital is sealed. No First pocket opens it. */
	public boolean opensDigital() {
		return false;
	}

	/** Fourth does not exist. */
	public boolean isFourth() {
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
			case "custodial" -> BrmcDimensions.CUSTODIAL;
			default -> null;
		};
	}
}

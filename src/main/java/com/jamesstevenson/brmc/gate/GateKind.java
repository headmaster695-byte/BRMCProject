package com.jamesstevenson.brmc.gate;

import net.minecraft.core.Direction;

/**
 * First-dimension exits from the Floor Sheet. Second is only VESTIBULE door 2,
 * and that door always works once the vestibule is found.
 * COMMONS is a single yellow→yellow threshold — no airlock, red, or OOB hole.
 * CUSTODIAL is the rare apartment janitor leak — not Bounded.
 * False Gate is not listed — it is not a First hop.
 * OOB is present but not live.
 */
public enum GateKind {
	VESTIBULE,
	COMMONS,
	CURVING_HALL,
	FALSE_FLOOR,
	UTILITIES,
	OOB_HOLE,
	CUSTODIAL;

	/** Authored walk-through facing for First's wired planes. */
	public Direction planeFacing() {
		return switch (this) {
			case VESTIBULE, COMMONS -> Direction.EAST;
			case CUSTODIAL -> Direction.WEST;
			case FALSE_FLOOR -> Direction.DOWN;
			default -> Direction.NORTH;
		};
	}

	public boolean hasLinkedVolumeThisPass() {
		return this != OOB_HOLE && this != FALSE_FLOOR;
	}

	/** Architecture only — no live swap until the drop systems exist. */
	public boolean architectureOnly() {
		return this == OOB_HOLE || this == FALSE_FLOOR;
	}

	/** Commons / False First: see-through may invent First wallpaper dest does not have. */
	public boolean previewLies() {
		return this == COMMONS;
	}

	/** Vestibule door 2: far side may chromatic-shift / heat-haze. */
	public boolean farSideDistorts() {
		return this == VESTIBULE;
	}

	public boolean planeCrossOnly() {
		return hasLinkedVolumeThisPass();
	}
}

package com.jamesstevenson.brmc.gate;

/**
 * First-dimension exits from the Floor Sheet. Second is only VESTIBULE door 2,
 * and that door always works once the vestibule is found.
 * COMMONS is a single yellow→yellow threshold — no airlock, red, or OOB hole.
 * CUSTODIAL is the rare apartment janitor leak — not Bounded.
 * False Gate is not listed — it is not a First hop.
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
	public net.minecraft.core.Direction planeFacing() {
		return switch (this) {
			case VESTIBULE, COMMONS -> net.minecraft.core.Direction.EAST;
			default -> net.minecraft.core.Direction.NORTH;
		};
	}

	public boolean hasLinkedVolumeThisPass() {
		return this == VESTIBULE || this == COMMONS;
	}
}

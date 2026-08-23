package com.jamesstevenson.brmc.gate;

/**
 * First-dimension exits from the Floor Sheet. Second is only VESTIBULE door 2.
 * False Gate is not listed — it is not a First hop.
 */
public enum GateKind {
	VESTIBULE,
	COMMONS,
	CURVING_HALL,
	FALSE_FLOOR,
	UTILITIES,
	OOB_HOLE
}

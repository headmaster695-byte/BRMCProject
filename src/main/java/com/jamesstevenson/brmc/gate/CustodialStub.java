package com.jamesstevenson.brmc.gate;

/**
 * Rare janitor leak from {@code first-apartment-pocket} into Custodial.
 * The closet threshold is a live plane-cross gate; the janitor NPC encounter
 * is not implemented. Apartment is a habitation lobe — not Bounded.
 */
public final class CustodialStub {
	private CustodialStub() {
	}

	public static boolean encounterImplemented() {
		return false;
	}

	public static boolean opensBounded() {
		return false;
	}
}

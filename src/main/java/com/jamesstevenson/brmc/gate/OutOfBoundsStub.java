package com.jamesstevenson.brmc.gate;

/**
 * Vestibule floor hole before door 2. The hole is the only stateful First exit.
 * Later chain (not implemented here): Lost Island → reactor SCRAM → Metaverse soul.
 * Do not build that puzzle on this pass.
 */
public final class OutOfBoundsStub {
	private OutOfBoundsStub() {
	}

	public static boolean holePresent() {
		return true;
	}

	/** Late / not live. The hole exists as architecture; the fall is not the current slice. */
	public static boolean holeLive() {
		return false;
	}

	public static boolean puzzleChainImplemented() {
		return false;
	}
}

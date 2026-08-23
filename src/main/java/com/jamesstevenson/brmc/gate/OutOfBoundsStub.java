package com.jamesstevenson.brmc.gate;

/**
 * Vestibule floor hole before door 2. The hole is architecture on this pass.
 * Later chain (not implemented here): Lost Island → reactor SCRAM → Metaverse soul.
 * Do not build that puzzle on this pass.
 *
 * <p>{@link #holeLive()} is the only switch. {@link GateKind#architectureOnly()}
 * reads it so OOB refuse is one path — not a dead flag beside a hardcoded refuse.
 */
public final class OutOfBoundsStub {
	private OutOfBoundsStub() {
	}

	/**
	 * False until a dest volume can hold the player. Wired into
	 * {@link GateKind#architectureOnly()} — do not add a second refuse flag.
	 */
	public static boolean holeLive() {
		return false;
	}
}

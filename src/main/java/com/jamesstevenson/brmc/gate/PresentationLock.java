package com.jamesstevenson.brmc.gate;

/**
 * Hard presentation lock for every First crossing.
 * Prefer honest linked volumes (Immersive Portals–class).
 * No teleport sting, fade-to-load, nether swirl, or “Entering X” UI.
 */
public final class PresentationLock {
	public enum Crossing {
		NONE,
		/** Commons: one stride, yellow stays yellow. */
		YELLOW_TO_YELLOW,
		/** Vestibule door 2: yellow airlock into dest-sampled red Second. First-side yellow→red frame is a miss. */
		YELLOW_TO_RED,
		/** Utilities deep door: plant continues into Buttons. */
		CONTINUOUS_PLANT,
		/** Curving hall: invisible seam, yellow around a soft plan. */
		INVISIBLE_SEAM,
		/** False floor or OOB hole. */
		DROP,
		/** Rare apartment janitor → Custodial. */
		RARE_ENCOUNTER
	}

	private PresentationLock() {
	}

	public static boolean allowsTeleportSting() {
		return false;
	}

	public static boolean allowsFadeToLoad() {
		return false;
	}

	public static boolean allowsNetherSwirl() {
		return false;
	}

	public static boolean allowsEnteringUi() {
		return false;
	}

	public static Crossing crossing(GateKind kind) {
		if (kind == null) {
			return Crossing.NONE;
		}

		return switch (kind) {
			case COMMONS -> Crossing.YELLOW_TO_YELLOW;
			case VESTIBULE -> Crossing.YELLOW_TO_RED;
			case UTILITIES -> Crossing.CONTINUOUS_PLANT;
			case CURVING_HALL -> Crossing.INVISIBLE_SEAM;
			case FALSE_FLOOR, OOB_HOLE -> Crossing.DROP;
			case CUSTODIAL -> Crossing.RARE_ENCOUNTER;
		};
	}
}

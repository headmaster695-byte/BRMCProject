package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.BrmcMod;

/**
 * Invariant checks for the First layout lock. Failures log; they do not crash play.
 */
public final class LayoutSanity {
	private LayoutSanity() {
	}

	public static void bootstrap() {
		int errors = 0;
		if (!YellowMonoLayout.inClarkChamber(YellowMonoLayout.SPAWN_X, YellowMonoLayout.SPAWN_Z)) {
			BrmcMod.LOGGER.error("Clark spawn is outside the authored chamber.");
			errors++;
		}

		if (YellowMonoLayout.pocketAt(YellowMonoLayout.SPAWN_X, YellowMonoLayout.SPAWN_Z) != FirstPocket.CLARK_CHAMBER) {
			BrmcMod.LOGGER.error("Clark spawn is not tagged first-clark-chamber.");
			errors++;
		}

		errors += expectPocket(52, 20, FirstPocket.VESTIBULE, "east spine vestibule");
		errors += expectPocket(20, 52, FirstPocket.COMMON_EXIT, "south spine commons");
		errors += expectPocket(-28, 20, FirstPocket.APARTMENT, "west spine apartment");
		errors += expectPocket(20, -28, FirstPocket.UTILITIES, "north spine utilities");
		errors += expectPocket(52, 52, FirstPocket.CURVING_HALL, "SE curving hall");
		errors += expectPocket(-20, 52, FirstPocket.FALSE_FLOOR, "SW false floor");
		errors += expectPocket(-28, -20, FirstPocket.FLUORESCENT_DEAD_ZONE, "NW dead zone");

		if (YellowMonoLayout.isDoor2RedFrame(20, 52)) {
			BrmcMod.LOGGER.error("Commons must not carry vestibule red framing.");
			errors++;
		}

		if (!YellowMonoLayout.isDoor2RedFrame(70, 20) || !YellowMonoLayout.isDoor2RedFrame(76, 20)) {
			BrmcMod.LOGGER.error("Vestibule door 2 / linked Second volume is not red-framed.");
			errors++;
		}

		if (!YellowMonoLayout.isLinkedDestinationVolume(76, 20) || !YellowMonoLayout.isLinkedDestinationVolume(28, 52)) {
			BrmcMod.LOGGER.error("Linked destination volumes missing for vestibule/commons.");
			errors++;
		}

		if (errors == 0) {
			BrmcMod.LOGGER.info("First layout sanity: Clark 32×32, cardinal spines, authored pockets placed.");
		} else {
			BrmcMod.LOGGER.error("First layout sanity failed with {} issue(s).", errors);
		}
	}

	private static int expectPocket(int x, int z, FirstPocket expected, String label) {
		if (YellowMonoLayout.inClarkChamber(x, z)) {
			BrmcMod.LOGGER.error("Pocket {} at {},{} is inside Clark.", label, x, z);
			return 1;
		}

		FirstPocket actual = YellowMonoLayout.pocketAt(x, z);
		if (actual != expected) {
			BrmcMod.LOGGER.error("Pocket {} at {},{} is {} (expected {}).", label, x, z, actual, expected);
			return 1;
		}

		return 0;
	}
}

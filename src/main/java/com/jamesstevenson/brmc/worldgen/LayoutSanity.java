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

		if (YellowMonoLayout.pocketAt(52, 20) != null) {
			BrmcMod.LOGGER.error("East cell before the airlock must stay uncommon yellow, not a vestibule aperture.");
			errors++;
		}

		errors += expectPocket(60, 20, FirstPocket.VESTIBULE, "vestibule airlock");
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

		errors += expectPocket(-39, 22, FirstPocket.APARTMENT_JANITOR, "apartment janitor closet");
		if (!YellowMonoLayout.isThresholdAnchor(19, -40)) {
			BrmcMod.LOGGER.error("Utilities deep door is not a threshold.");
			errors++;
		}

		if (!YellowMonoLayout.isThresholdAnchor(62, 62)) {
			BrmcMod.LOGGER.error("Curving hall seam is not a threshold.");
			errors++;
		}

		if (!YellowMonoLayout.isThresholdAnchor(-20, 52)) {
			BrmcMod.LOGGER.error("False floor hole is not a threshold.");
			errors++;
		}

		if (!YellowMonoLayout.isThresholdAnchor(-39, 22)) {
			BrmcMod.LOGGER.error("Janitor closet is not a threshold.");
			errors++;
		}

		if (DestClimateChunkGenerator.columnState(DestClimate.YELLOW_MONO, 0, 66, 0).isAir()) {
			BrmcMod.LOGGER.error("False First dest cell is missing corner walls.");
			errors++;
		}

		if (!DestClimateChunkGenerator.columnState(DestClimate.YELLOW_MONO, 4, 66, 4).isAir()) {
			BrmcMod.LOGGER.error("False First dest cell center is not walkable.");
			errors++;
		}

		if (!DestClimateChunkGenerator.columnState(DestClimate.SPIRAL_WELL, -20, 64, 52).isAir()) {
			BrmcMod.LOGGER.error("Spiral well shaft is sealed at the false-floor identity column.");
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

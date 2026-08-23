package com.jamesstevenson.brmc.gate;

import com.jamesstevenson.brmc.BrmcMod;

/**
 * Compile-time / boot checks for the linked-dimension portal lock.
 * Failures log; they do not crash play.
 */
public final class PortalArchitectureSanity {
	private PortalArchitectureSanity() {
	}

	public static void bootstrap() {
		int errors = 0;
		if (LinkedDimensionArchitecture.hopIsDefault()) {
			BrmcMod.LOGGER.error("Hop must not be the default gate language.");
			errors++;
		}

		if (LinkedDimensionArchitecture.dualWorldClientLevel()) {
			BrmcMod.LOGGER.error("Do not claim a dest ClientLevel that this pass does not have.");
			errors++;
		}

		if (!LinkedDimensionArchitecture.destSampledSeeThrough() || !LinkedDimensionArchitecture.planeCrossIdentitySwap()) {
			BrmcMod.LOGGER.error("Linked-dimension skeleton must keep dest sampling and plane-cross.");
			errors++;
		}

		if (!GateKind.COMMONS.previewLies() || GateKind.VESTIBULE.previewLies()) {
			BrmcMod.LOGGER.error("Only commons / False First may lie about the far side.");
			errors++;
		}

		if (!GateKind.VESTIBULE.farSideDistorts() || GateKind.COMMONS.farSideDistorts()) {
			BrmcMod.LOGGER.error("Only vestibule door 2 uses far-side distort.");
			errors++;
		}

		if (!GateKind.COMMONS.planeCrossOnly() || !GateKind.VESTIBULE.planeCrossOnly()) {
			BrmcMod.LOGGER.error("Commons and vestibule must cross on the plane, not on any touch.");
			errors++;
		}

		if (PortalLod.values().length != 3 || PortalRenderBudget.FULL_CAP < 1 || PortalRenderBudget.MESH_CAP < 1) {
			BrmcMod.LOGGER.error("Per-portal LOD budget hooks are missing.");
			errors++;
		}

		if (BrmcGateConfig.allowHopGates()) {
			BrmcMod.LOGGER.warn("Hop flag is on in this process. Player-facing path is still linked volume.");
		}

		if (errors == 0) {
			BrmcMod.LOGGER.info(
				"Portal architecture: dest-sampled LOD + plane-cross. Dual-world ClientLevel: no. Hop default: no."
			);
		} else {
			BrmcMod.LOGGER.error("Portal architecture sanity failed with {} issue(s).", errors);
		}
	}
}

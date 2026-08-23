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

		for (GateKind kind : GateKind.values()) {
			if (kind.architectureOnly()) {
				if (kind.hasLinkedVolumeThisPass() || kind.planeCrossOnly()) {
					BrmcMod.LOGGER.error("{} is architecture only and must not live-swap.", kind);
					errors++;
				}

				continue;
			}

			if (!kind.hasLinkedVolumeThisPass() || !kind.planeCrossOnly()) {
				BrmcMod.LOGGER.error("{} must use dest-sampled plane-cross, not touch hop.", kind);
				errors++;
			}

			if (!MercyReturnService.hasReturn(kind)) {
				BrmcMod.LOGGER.error("{} is live without a mercy return — that is a softlock.", kind);
				errors++;
			}
		}

		if (!GateKind.FALSE_FLOOR.architectureOnly() || !GateKind.OOB_HOLE.architectureOnly()) {
			BrmcMod.LOGGER.error("False floor and OOB must stay architecture-only.");
			errors++;
		}

		if (OutOfBoundsStub.holeLive()) {
			BrmcMod.LOGGER.error("OOB holeLive() is on — refuse path expects it false this pass.");
			errors++;
		}

		if (GateKind.OOB_HOLE.architectureOnly() == OutOfBoundsStub.holeLive()) {
			BrmcMod.LOGGER.error("OOB architectureOnly() must track !holeLive().");
			errors++;
		}

		if (LinkedOpenings.ALL.size() != 5 || LinkedOpenings.destReturnGates().size() != LinkedOpenings.firstOutboundGates().size()) {
			BrmcMod.LOGGER.error("Live openings must register matching outbound and dest-return planes.");
			errors++;
		}

		for (LinkedOpenings.Opening opening : LinkedOpenings.ALL) {
			if (opening.destReturnFacing() != opening.firstOutboundFacing().getOpposite()) {
				BrmcMod.LOGGER.error("{} dest return facing is not the outbound opposite.", opening.kind());
				errors++;
			}

			if (!MercyReturnService.outboundRegistered(opening.kind()) || !MercyReturnService.destReturnRegistered(opening.kind())) {
				BrmcMod.LOGGER.error("{} is missing a both-ways mercy plane.", opening.kind());
				errors++;
			}

			if (!opening.destination().equals(SeamlessGateService.gate(opening.kind(), opening.identities().getFirst(), opening.firstOutboundFacing()).to())) {
				BrmcMod.LOGGER.error("{} dest world does not match the outbound gate.", opening.kind());
				errors++;
			}
		}

		if (GateKind.CUSTODIAL.planeFacing() != net.minecraft.core.Direction.WEST) {
			BrmcMod.LOGGER.error("Custodial closet must face west from the apartment.");
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
				"Portal architecture: dest-sampled LOD + plane-cross on all live First exits. "
					+ "Dual-world ClientLevel: no. Hop default: no. OOB: not live."
			);
		} else {
			BrmcMod.LOGGER.error("Portal architecture sanity failed with {} issue(s).", errors);
		}
	}
}

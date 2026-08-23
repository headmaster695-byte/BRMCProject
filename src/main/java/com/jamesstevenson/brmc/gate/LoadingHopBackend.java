package com.jamesstevenson.brmc.gate;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Isolated-test hop only. Never the player-facing gate language.
 * {@link SeamlessGateService} constructs this solely when no IP-class backend
 * is present and {@link BrmcGateConfig#allowHopGates()} is true.
 * Keep the destination pose identity-aligned so an Immersive Portals backend
 * can replace this without moving the architecture.
 * Do not send title/subtitle “Entering” copy; {@link PresentationLock} forbids it.
 */
public final class LoadingHopBackend implements SeamlessGateBackend {
	@Override
	public String id() {
		return "loading_hop_fallback";
	}

	@Override
	public boolean isSeamless() {
		return false;
	}

	@Override
	public void ensureOpening(ServerLevel source, SeamlessGate gate) {
	}

	@Override
	public boolean traverse(ServerPlayer player, ServerLevel source, SeamlessGate gate) {
		if (!BrmcGateConfig.allowHopGates()) {
			BrmcMod.LOGGER.error(
				"LoadingHopBackend blocked {} : {} is off. Hop is never the design language.",
				gate.kind(),
				BrmcGateConfig.FLAG
			);
			return false;
		}

		ServerLevel destination = source.getServer().getLevel(gate.to());
		if (destination == null) {
			BrmcMod.LOGGER.warn("Gate {} missing destination {}", gate.kind(), gate.to().identifier());
			return false;
		}

		Vec3 dest = gate.alignedDestination();
		player.teleport(new TeleportTransition(
			destination,
			dest,
			player.getDeltaMovement(),
			player.getYRot(),
			player.getXRot(),
			TeleportTransition.DO_NOTHING
		));
		return true;
	}
}

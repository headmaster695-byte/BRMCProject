package com.jamesstevenson.brmc.gate;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Development fallback only. This is a loading-screen hop and is explicitly
 * not the design target. Keep the destination pose identity-aligned so an
 * Immersive Portals backend can replace this without moving the architecture.
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

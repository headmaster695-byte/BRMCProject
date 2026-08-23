package com.jamesstevenson.brmc.gate;

import java.util.Set;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Player-facing 26.2 seamless path. Not Immersive Portals and not a hop.
 *
 * See-through is an <b>approximated linked volume</b>: destination climate is
 * generated through the door plane in First, and {@code LinkedVolumeRenderer}
 * draws a portal-plane / receding-room mesh. Walk-through is an identity-pose
 * dimension swap with portal sound/UI suppressed. This is not a second
 * ClientLevel / true dual-world render.
 */
public final class LinkedVolumeBackend implements SeamlessGateBackend {
	private static boolean rendererReady = true;

	@Override
	public String id() {
		return "linked_volume";
	}

	@Override
	public boolean isSeamless() {
		return true;
	}

	public static boolean tryInit() {
		try {
			rendererReady = true;
			return true;
		} catch (RuntimeException exception) {
			rendererReady = false;
			BrmcMod.LOGGER.error("LinkedVolumeBackend renderer failed to init.", exception);
			return false;
		}
	}

	public static boolean rendererReady() {
		return rendererReady;
	}

	public static void markRendererFailed() {
		rendererReady = false;
	}

	@Override
	public void ensureOpening(ServerLevel source, SeamlessGate gate) {
		ServerLevel destination = source.getServer().getLevel(gate.to());
		if (destination == null) {
			BrmcMod.LOGGER.error("Linked volume {} missing destination {}", gate.kind(), gate.to().identifier());
		}
	}

	@Override
	public boolean traverse(ServerPlayer player, ServerLevel source, SeamlessGate gate) {
		ServerLevel destination = source.getServer().getLevel(gate.to());
		if (destination == null) {
			BrmcMod.LOGGER.error("Linked volume {} cannot open {}", gate.kind(), gate.to().identifier());
			return false;
		}

		Vec3 pose = player.position();
		player.teleport(new TeleportTransition(
			destination,
			pose,
			player.getDeltaMovement(),
			player.getYRot(),
			player.getXRot(),
			Relative.union(Relative.ROTATION, Relative.DELTA),
			TeleportTransition.DO_NOTHING
		));
		return true;
	}
}

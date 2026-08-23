package com.jamesstevenson.brmc.gate;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Player-facing 26.2 path: linked-dimension portal, not a hop.
 *
 * See-through is dest-sampled voxels + LOD mesh ({@link PortalLod}). Walk-through
 * is a plane-cross identity-pose swap into a dest volume that already has
 * {@link TicketType#PORTAL} tickets. Not a second {@code ClientLevel}.
 */
public final class LinkedVolumeBackend implements SeamlessGateBackend {
	private static final int DEST_TICKET_RADIUS = 2;
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
		preloadDestination(source, gate);
	}

	public static void preloadDestination(ServerLevel source, SeamlessGate gate) {
		ServerLevel destination = source.getServer().getLevel(gate.to());
		if (destination == null) {
			BrmcMod.LOGGER.error("Linked volume {} missing destination {}", gate.kind(), gate.to().identifier());
			return;
		}

		destination.getChunkSource().addTicketWithRadius(
			TicketType.PORTAL,
			ChunkPos.containing(gate.threshold()),
			DEST_TICKET_RADIUS
		);
	}

	public static DestinationVolume sampleDestination(ServerLevel source, SeamlessGate gate) {
		ServerLevel destination = source.getServer().getLevel(gate.to());
		if (destination == null) {
			return DestinationVolume.EMPTY;
		}

		preloadDestination(source, gate);
		return DestinationVolumeSampler.sample(destination, gate.threshold(), gate.facing());
	}

	public static boolean destinationReady(ServerLevel source, SeamlessGate gate, ServerPlayer player) {
		ServerLevel destination = source.getServer().getLevel(gate.to());
		if (destination == null) {
			return false;
		}

		return destination.hasChunkAt(player.blockPosition());
	}

	@Override
	public boolean traverse(ServerPlayer player, ServerLevel source, SeamlessGate gate) {
		ServerLevel destination = source.getServer().getLevel(gate.to());
		if (destination == null) {
			BrmcMod.LOGGER.error("Linked volume {} cannot open {}", gate.kind(), gate.to().identifier());
			return false;
		}

		preloadDestination(source, gate);

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

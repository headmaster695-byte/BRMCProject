package com.jamesstevenson.brmc.spawn;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.worldgen.YellowMonoLayout;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import com.mojang.serialization.Codec;

/**
 * Clark cold-open: the player wakes in the authored first Backrooms chamber
 * (post-threshold yellow room only). No store, basement, portal, or tutorial.
 */
public final class ClarkColdOpen {
	public static final AttachmentType<Boolean> ARRIVED = AttachmentRegistry.create(
		BrmcMod.id("clark_arrived"),
		builder -> builder.persistent(Codec.BOOL).initializer(() -> false)
	);

	private ClarkColdOpen() {
	}

	public static void initialize() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> arrive(handler.player));
	}

	public static void arrive(ServerPlayer player) {
		if (Boolean.TRUE.equals(player.getAttached(ARRIVED))) {
			return;
		}

		ServerLevel first = player.level().getServer().getLevel(BrmcDimensions.FIRST);
		if (first == null) {
			BrmcMod.LOGGER.warn("First Dimension is not loaded; Clark cold-open skipped.");
			return;
		}

		Vec3 spawn = new Vec3(YellowMonoLayout.SPAWN_X + 0.5, YellowMonoLayout.CARPET_Y + 1, YellowMonoLayout.SPAWN_Z + 0.5);
		player.teleport(new TeleportTransition(
			first,
			spawn,
			Vec3.ZERO,
			YellowMonoLayout.SPAWN_Y_ROT,
			0.0F,
			TeleportTransition.DO_NOTHING
		));
		player.setAttached(ARRIVED, true);
	}
}

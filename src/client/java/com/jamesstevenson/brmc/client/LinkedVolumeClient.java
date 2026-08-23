package com.jamesstevenson.brmc.client;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.block.BrmcBlockEntities;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.gate.LinkedVolumeBackend;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

/**
 * Client half of {@link LinkedVolumeBackend}: portal-plane renderer and
 * crossing flag so loading/nether swirl can be suppressed.
 */
public final class LinkedVolumeClient {
	private static int crossingTicks;
	private static ResourceKey<Level> lastDimension;

	private LinkedVolumeClient() {
	}

	public static void initialize() {
		try {
			BlockEntityRenderers.register(BrmcBlockEntities.LINKED_VOLUME, context -> new LinkedVolumeRenderer());
			BrmcMod.LOGGER.info("LinkedVolumeRenderer registered (portal-plane mesh, not dual-world).");
		} catch (RuntimeException exception) {
			LinkedVolumeBackend.markRendererFailed();
			BrmcMod.LOGGER.error("LinkedVolumeRenderer failed to register.", exception);
		}

		ClientTickEvents.END_CLIENT_TICK.register(client -> tick(client));
	}

	public static boolean isCrossing() {
		return crossingTicks > 0;
	}

	public static void markCrossing() {
		crossingTicks = 80;
	}

	private static void tick(Minecraft client) {
		if (crossingTicks > 0) {
			crossingTicks--;
		}

		if (client.player == null) {
			return;
		}

		ResourceKey<Level> now = client.player.level().dimension();
		if (lastDimension != null
			&& BrmcDimensions.FIRST.equals(lastDimension)
			&& (BrmcDimensions.SECOND.equals(now) || BrmcDimensions.FALSE_FIRST.equals(now))) {
			markCrossing();
		}

		lastDimension = now;
	}
}

package com.jamesstevenson.brmc.client;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.block.BrmcBlockEntities;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.gate.LinkedVolumeBackend;
import com.jamesstevenson.brmc.gate.PortalRenderBudget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

/**
 * Client half of the linked-dimension portal: LOD budget, dest-voxel renderer,
 * and crossing flag so loading / nether swirl can be held off-screen.
 */
public final class LinkedVolumeClient {
	private static final PortalRenderBudget BUDGET = new PortalRenderBudget();
	private static int crossingTicks;
	private static ResourceKey<Level> lastDimension;

	private LinkedVolumeClient() {
	}

	public static void initialize() {
		try {
			BlockEntityRenderers.register(BrmcBlockEntities.LINKED_VOLUME, context -> new LinkedVolumeRenderer());
			BrmcMod.LOGGER.info("LinkedVolumeRenderer registered (dest-sampled LOD, not dual-world ClientLevel).");
		} catch (RuntimeException exception) {
			LinkedVolumeBackend.markRendererFailed();
			BrmcMod.LOGGER.error("LinkedVolumeRenderer failed to register.", exception);
		}

		ClientTickEvents.END_CLIENT_TICK.register(LinkedVolumeClient::tick);
	}

	public static PortalRenderBudget budget() {
		return BUDGET;
	}

	public static boolean isCrossing() {
		return crossingTicks > 0;
	}

	public static void markCrossing() {
		crossingTicks = 80;
	}

	public static void markLinkedDimensionSwap(ResourceKey<Level> from, ResourceKey<Level> to) {
		if (from == null || to == null) {
			return;
		}

		boolean fromFirst = BrmcDimensions.FIRST.equals(from);
		boolean toFirst = BrmcDimensions.FIRST.equals(to);
		boolean fromDest = BrmcDimensions.SECOND.equals(from) || BrmcDimensions.isSubDimension(from);
		boolean toDest = BrmcDimensions.SECOND.equals(to) || BrmcDimensions.isSubDimension(to);
		if (fromFirst && toDest || fromDest && toFirst) {
			markCrossing();
		}
	}

	private static void tick(Minecraft client) {
		BUDGET.reset();
		if (crossingTicks > 0) {
			crossingTicks--;
		}

		if (client.player == null) {
			return;
		}

		ResourceKey<Level> now = client.player.level().dimension();
		markLinkedDimensionSwap(lastDimension, now);
		lastDimension = now;
	}
}

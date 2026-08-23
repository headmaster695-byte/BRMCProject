package com.jamesstevenson.brmc.rule;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

/**
 * Distant wrong sounds that never resolve into an encounter.
 * Foreshadowing only — no entity, no chat, no tip text.
 */
public final class UnresolvedSoundRule {
	private static final int BASE_PERIOD_TICKS = 360;

	private UnresolvedSoundRule() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			ServerLevel first = server.getLevel(BrmcDimensions.FIRST);
			if (first != null) {
				tick(first);
			}
		});
	}

	private static void tick(ServerLevel level) {
		long time = level.getGameTime();
		for (ServerPlayer player : level.players()) {
			int stagger = Math.floorMod(player.getUUID().hashCode(), 80);
			if ((time + stagger) % BASE_PERIOD_TICKS != 0) {
				continue;
			}

			double angle = (time * 0.17 + stagger) % (Math.PI * 2);
			double distance = 28.0 + (stagger & 7);
			double x = player.getX() + Mth.cos((float) angle) * distance;
			double z = player.getZ() + Mth.sin((float) angle) * distance;
			double y = player.getY() + 1.0;
			level.playSound(
				null,
				x,
				y,
				z,
				SoundEvents.AMBIENT_CAVE,
				SoundSource.AMBIENT,
				0.35F,
				0.55F + level.getRandom().nextFloat() * 0.15F
			);
		}
	}
}

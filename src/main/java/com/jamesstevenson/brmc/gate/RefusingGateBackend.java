package com.jamesstevenson.brmc.gate;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Production-ish default when no Immersive Portals–class backend is present.
 * Refuses to hop so playtests do not learn fade-load as the gate language.
 */
public final class RefusingGateBackend implements SeamlessGateBackend {
	private static final long LOG_COOLDOWN_TICKS = 200L;
	private long lastLogTick = Long.MIN_VALUE;

	@Override
	public String id() {
		return "refusing_no_seamless_backend";
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
		long now = source.getGameTime();
		if (now - this.lastLogTick >= LOG_COOLDOWN_TICKS) {
			this.lastLogTick = now;
			BrmcMod.LOGGER.error(
				"Refusing gate {} at {}: no Immersive Portals–class backend. "
					+ "Identity hop is disabled so playtests do not learn fade-load as the gate language. "
					+ "Install an IP-class backend, or set -D{}=true only for isolated tests.",
				gate.kind(),
				gate.threshold(),
				BrmcGateConfig.FLAG
			);
		}

		return false;
	}
}

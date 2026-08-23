package com.jamesstevenson.brmc.gate;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Immersive Portals–class contract: the player must be able to see and walk
 * through the threshold as continuous space. No teleport sting, fade-to-load,
 * nether swirl, or “Entering X” UI. Implementations that hop through a
 * dimension change without a see-through opening are development fallbacks.
 */
public interface SeamlessGateBackend {
	String id();

	boolean isSeamless();

	void ensureOpening(ServerLevel source, SeamlessGate gate);

	boolean traverse(ServerPlayer player, ServerLevel source, SeamlessGate gate);
}

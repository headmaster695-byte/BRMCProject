package com.jamesstevenson.brmc.gate;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Immersive Portals–class contract: see and walk through the threshold as
 * continuous travel into the linked dimension. No teleport sting, fade-to-load,
 * nether swirl, or “Entering X” UI. Touch-then-hop is not this contract.
 * Identity hop is never the design language and is only constructed behind
 * {@link BrmcGateConfig#allowHopGates()}.
 */
public interface SeamlessGateBackend {
	String id();

	boolean isSeamless();

	void ensureOpening(ServerLevel source, SeamlessGate gate);

	boolean traverse(ServerPlayer player, ServerLevel source, SeamlessGate gate);
}

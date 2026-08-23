package com.jamesstevenson.brmc.gate;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Optional adapter. Immersive Portals last published for Fabric 1.21.1 and is
 * archived; it cannot be a hard 26.2 dependency. When an IP-compatible API is
 * on the classpath, this backend should create a see-through portal with an
 * identity transform at the gate threshold.
 */
public final class ImmersivePortalsBackend implements SeamlessGateBackend {
	private static final String[] PROBE_CLASSES = {
		"qouteall.imm_ptl.core.portal.Portal",
		"qouteall.q_misc_util.api.McRemoteProcedureCall"
	};

	public static boolean available() {
		for (String className : PROBE_CLASSES) {
			try {
				Class.forName(className, false, ImmersivePortalsBackend.class.getClassLoader());
				return true;
			} catch (ClassNotFoundException ignored) {
			}
		}

		return false;
	}

	@Override
	public String id() {
		return "immersive_portals";
	}

	@Override
	public boolean isSeamless() {
		return true;
	}

	@Override
	public void ensureOpening(ServerLevel source, SeamlessGate gate) {
		BrmcMod.LOGGER.debug("IP backend would open {} at {}", gate.kind(), gate.threshold());
	}

	@Override
	public boolean traverse(ServerPlayer player, ServerLevel source, SeamlessGate gate) {
		// Walking through a live IP portal should already move the player.
		// If IP is present but has not mounted this opening yet, refuse to hop.
		return false;
	}
}

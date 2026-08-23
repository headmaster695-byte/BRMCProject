package com.jamesstevenson.brmc.gate;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class SeamlessGateService {
	private static final long TRAVERSE_COOLDOWN_TICKS = 40L;
	private static final Map<UUID, Long> lastTraverseTick = new HashMap<>();
	private static SeamlessGateBackend backend = new LoadingHopBackend();

	private SeamlessGateService() {
	}

	public static void initialize() {
		if (ImmersivePortalsBackend.available()) {
			backend = new ImmersivePortalsBackend();
			BrmcMod.LOGGER.info("Using Immersive Portals backend for seamless gates.");
		} else {
			backend = new LoadingHopBackend();
			BrmcMod.LOGGER.warn(
				"Immersive Portals is not on the classpath. Gates use a loading-hop fallback. "
					+ "This is not the design target; plug an IP-class backend into SeamlessGateBackend."
			);
		}
	}

	public static String backendName() {
		return backend.id();
	}

	public static boolean isSeamless() {
		return backend.isSeamless();
	}

	public static SeamlessGate vestibule(BlockPos threshold, Direction facing) {
		return gate(GateKind.VESTIBULE, threshold, facing);
	}

	public static SeamlessGate commons(BlockPos threshold, Direction facing) {
		return gate(GateKind.COMMONS, threshold, facing);
	}

	public static SeamlessGate gate(GateKind kind, BlockPos threshold, Direction facing) {
		return switch (kind) {
			case VESTIBULE -> new SeamlessGate(kind, BrmcDimensions.FIRST, BrmcDimensions.SECOND, threshold, facing);
			case COMMONS -> new SeamlessGate(kind, BrmcDimensions.FIRST, BrmcDimensions.FALSE_FIRST, threshold, facing);
			case CURVING_HALL -> new SeamlessGate(kind, BrmcDimensions.FIRST, BrmcDimensions.SECOND_FALSE_FIRST, threshold, facing);
			case FALSE_FLOOR -> new SeamlessGate(kind, BrmcDimensions.FIRST, BrmcDimensions.SPIRAL, threshold, facing);
			case UTILITIES -> new SeamlessGate(kind, BrmcDimensions.FIRST, BrmcDimensions.BUTTONS, threshold, facing);
			case OOB_HOLE -> new SeamlessGate(kind, BrmcDimensions.FIRST, BrmcDimensions.OUT_OF_BOUNDS, threshold, facing);
		};
	}

	public static void ensureOpening(ServerLevel source, SeamlessGate gate) {
		backend.ensureOpening(source, gate);
	}

	public static boolean traverse(ServerPlayer player, ServerLevel source, SeamlessGate gate) {
		if (!gate.from().equals(source.dimension())) {
			return false;
		}

		long now = source.getGameTime();
		Long previous = lastTraverseTick.get(player.getUUID());
		if (previous != null && now - previous < TRAVERSE_COOLDOWN_TICKS) {
			return false;
		}

		boolean crossed = backend.traverse(player, source, gate);
		if (crossed) {
			lastTraverseTick.put(player.getUUID(), now);
		}

		return crossed;
	}
}

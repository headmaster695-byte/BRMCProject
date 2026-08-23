package com.jamesstevenson.brmc.rule;

import java.util.HashSet;
import java.util.Set;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * Building is OK. Track player-placed blocks so mining regen does not eat them.
 * In-memory stub; persist via chunk attachments when the layout grows.
 */
public final class BuildingTracker {
	private static final Set<Long> PLAYER_BUILT = new HashSet<>();

	private BuildingTracker() {
	}

	public static void initialize() {
	}

	public static void markPlayerBuilt(ServerLevel level, BlockPos pos) {
		if (BrmcDimensions.isFirst(level)) {
			PLAYER_BUILT.add(pack(level, pos));
		}
	}

	public static boolean isPlayerBuilt(ServerLevel level, BlockPos pos) {
		return PLAYER_BUILT.contains(pack(level, pos));
	}

	public static void clear(ServerLevel level, BlockPos pos) {
		PLAYER_BUILT.remove(pack(level, pos));
	}

	private static long pack(ServerLevel level, BlockPos pos) {
		return (long) level.dimension().identifier().hashCode() << 32 ^ pos.asLong();
	}
}

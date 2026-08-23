package com.jamesstevenson.brmc.rule;

import java.util.ArrayList;
import java.util.List;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import com.mojang.serialization.Codec;

/**
 * Building and pillaring persist in First. Tracked on the chunk so regen
 * does not eat player-placed blocks after a restart.
 */
public final class BuildingTracker {
	public static final AttachmentType<List<Long>> PLAYER_BUILT = AttachmentRegistry.create(
		BrmcMod.id("player_built"),
		builder -> builder.persistent(Codec.LONG.listOf()).initializer(List::of)
	);

	private BuildingTracker() {
	}

	public static void initialize() {
	}

	public static void markPlayerBuilt(ServerLevel level, BlockPos pos) {
		if (!BrmcDimensions.isFirst(level)) {
			return;
		}

		LevelChunk chunk = level.getChunkAt(pos);
		List<Long> list = new ArrayList<>(attached(chunk));
		long packed = pos.asLong();
		if (list.contains(packed)) {
			return;
		}

		list.add(packed);
		chunk.setAttached(PLAYER_BUILT, List.copyOf(list));
		chunk.markUnsaved();
	}

	public static boolean isPlayerBuilt(ServerLevel level, BlockPos pos) {
		if (!BrmcDimensions.isFirst(level)) {
			return false;
		}

		return attached(level.getChunkAt(pos)).contains(pos.asLong());
	}

	public static void clear(ServerLevel level, BlockPos pos) {
		if (!BrmcDimensions.isFirst(level)) {
			return;
		}

		LevelChunk chunk = level.getChunkAt(pos);
		List<Long> list = new ArrayList<>(attached(chunk));
		if (!list.remove(pos.asLong())) {
			return;
		}

		chunk.setAttached(PLAYER_BUILT, List.copyOf(list));
		chunk.markUnsaved();
	}

	private static List<Long> attached(LevelChunk chunk) {
		List<Long> value = chunk.getAttached(PLAYER_BUILT);
		return value == null ? List.of() : value;
	}
}

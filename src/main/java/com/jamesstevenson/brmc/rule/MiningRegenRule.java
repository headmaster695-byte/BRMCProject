package com.jamesstevenson.brmc.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.worldgen.YellowMonoLayout;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

/**
 * First-dimension rule: generated fabric comes back. Floor / ceiling / subfloor
 * return quickly so the yellow layer cannot be dug out. Player-built blocks stay.
 */
public final class MiningRegenRule {
	private static final int WALL_REGEN_TICKS = 80;
	private static final int SHELL_REGEN_TICKS = 20;
	private static final List<PendingRegen> QUEUE = new ArrayList<>();

	private MiningRegenRule() {
	}

	public static void initialize() {
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (level instanceof ServerLevel serverLevel && BrmcDimensions.isFirst(serverLevel)) {
				schedule(serverLevel, pos.immutable(), state);
			}
		});
		ServerTickEvents.END_SERVER_TICK.register(MiningRegenRule::tick);
	}

	public static void schedule(ServerLevel level, BlockPos pos, BlockState state) {
		if (BuildingTracker.isPlayerBuilt(level, pos)) {
			BuildingTracker.clear(level, pos);
			return;
		}

		if (state.isAir()) {
			return;
		}

		int delay = YellowMonoLayout.isLayerShell(pos.getY()) ? SHELL_REGEN_TICKS : WALL_REGEN_TICKS;
		QUEUE.add(new PendingRegen(level.dimension(), pos, state, level.getGameTime() + delay));
	}

	private static void tick(MinecraftServer server) {
		if (QUEUE.isEmpty()) {
			return;
		}

		Iterator<PendingRegen> iterator = QUEUE.iterator();
		while (iterator.hasNext()) {
			PendingRegen pending = iterator.next();
			ServerLevel level = server.getLevel(pending.dimension());
			if (level == null) {
				iterator.remove();
				continue;
			}

			if (level.getGameTime() < pending.readyAt()) {
				continue;
			}

			if (BuildingTracker.isPlayerBuilt(level, pending.pos())) {
				iterator.remove();
				continue;
			}

			if (level.getBlockState(pending.pos()).isAir()) {
				level.setBlockAndUpdate(pending.pos(), pending.state());
			}

			iterator.remove();
		}
	}

	private record PendingRegen(
		net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension,
		BlockPos pos,
		BlockState state,
		long readyAt
	) {
	}
}

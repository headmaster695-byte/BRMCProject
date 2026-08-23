package com.jamesstevenson.brmc.gate;

import java.util.List;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.worldgen.YellowMonoLayout;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

/**
 * Walk back through the same identity plane from dest into First.
 * Live dest stubs without this are softlocks; refuse those gates instead.
 */
public final class MercyReturnService {
	private static final List<SeamlessGate> RETURNS = List.of(
		SeamlessGateService.gate(GateKind.VESTIBULE, new BlockPos(70, YellowMonoLayout.CARPET_Y, 20), Direction.EAST),
		SeamlessGateService.gate(GateKind.COMMONS, new BlockPos(23, YellowMonoLayout.CARPET_Y, 52), Direction.EAST),
		SeamlessGateService.gate(GateKind.UTILITIES, new BlockPos(19, YellowMonoLayout.CARPET_Y, -40), Direction.NORTH),
		SeamlessGateService.gate(GateKind.UTILITIES, new BlockPos(20, YellowMonoLayout.CARPET_Y, -40), Direction.NORTH),
		SeamlessGateService.gate(GateKind.CURVING_HALL, new BlockPos(62, YellowMonoLayout.CARPET_Y, 62), Direction.NORTH),
		SeamlessGateService.gate(GateKind.CUSTODIAL, new BlockPos(-39, YellowMonoLayout.CARPET_Y, 22), Direction.WEST)
	);

	private static final PortalCrossTracker TRACKER = new PortalCrossTracker();

	private MercyReturnService() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(MercyReturnService::tick);
	}

	public static boolean hasReturn(GateKind kind) {
		if (kind == null || kind.architectureOnly()) {
			return false;
		}

		for (SeamlessGate gate : RETURNS) {
			if (gate.kind() == kind) {
				return true;
			}
		}

		return false;
	}

	private static void tick(MinecraftServer server) {
		ServerLevel first = server.getLevel(BrmcDimensions.FIRST);
		if (first == null) {
			return;
		}

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			tryReturn(first, player);
		}
	}

	private static void tryReturn(ServerLevel first, ServerPlayer player) {
		if (BrmcDimensions.isFirst(player.level())) {
			return;
		}

		Vec3 pose = player.position();
		SeamlessGate active = null;
		for (SeamlessGate gate : RETURNS) {
			if (gate.to().equals(player.level().dimension())
				&& PortalCrossTracker.inDoorway(pose, gate.threshold(), gate.facing())) {
				active = gate;
				break;
			}
		}

		if (active == null) {
			TRACKER.forget(player.getUUID());
			return;
		}

		SeamlessGate gate = active;
		double signed = PortalCrossTracker.signedDistance(pose, gate.threshold(), gate.facing());
		if (!TRACKER.crossedTowardSource(player.getUUID(), signed)) {
			return;
		}

		first.getChunkSource().addTicketWithRadius(
			net.minecraft.server.level.TicketType.PORTAL,
			net.minecraft.world.level.ChunkPos.containing(gate.threshold()),
			2
		);
		player.teleport(new TeleportTransition(
			first,
			pose,
			player.getDeltaMovement(),
			player.getYRot(),
			player.getXRot(),
			Relative.union(Relative.ROTATION, Relative.DELTA),
			TeleportTransition.DO_NOTHING
		));
		TRACKER.forget(player.getUUID());
		BrmcMod.LOGGER.debug("Mercy return {} → First at {}", gate.kind(), gate.threshold());
	}
}

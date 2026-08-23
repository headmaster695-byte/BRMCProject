package com.jamesstevenson.brmc.gate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/**
 * Was-behind → now-through the dest-facing plane. Touching the threshold
 * volume is not a cross. Camera and player share this motion; the backend
 * then swaps the player into the dest dimension already loaded at identity.
 */
public final class PortalCrossTracker {
	private final Map<UUID, Double> lastSigned = new HashMap<>();

	public boolean crossedTowardDest(UUID id, double signed) {
		Double previous = this.lastSigned.put(id, signed);
		if (previous == null) {
			return false;
		}

		return previous < 0.0 && signed >= 0.0;
	}

	public boolean crossedTowardSource(UUID id, double signed) {
		Double previous = this.lastSigned.put(id, signed);
		if (previous == null) {
			return false;
		}

		return previous >= 0.0 && signed < 0.0;
	}

	public void forget(UUID id) {
		this.lastSigned.remove(id);
	}

	public static double signedDistance(Vec3 position, BlockPos threshold, Direction facing) {
		double planeX = threshold.getX() + planeOffset(facing.getStepX());
		double planeY = threshold.getY() + planeOffset(facing.getStepY());
		double planeZ = threshold.getZ() + planeOffset(facing.getStepZ());
		return (position.x - planeX) * facing.getStepX()
			+ (position.y - planeY) * facing.getStepY()
			+ (position.z - planeZ) * facing.getStepZ();
	}

	public static boolean inDoorway(Vec3 position, BlockPos threshold, Direction facing) {
		if (facing.getAxis() == Direction.Axis.Y) {
			return position.x >= threshold.getX() - 0.2 && position.x <= threshold.getX() + 1.2
				&& position.z >= threshold.getZ() - 0.2 && position.z <= threshold.getZ() + 1.2
				&& position.y >= threshold.getY() - 4.0 && position.y <= threshold.getY() + 2.0;
		}

		double y = position.y;
		if (y < threshold.getY() - 0.2 || y > threshold.getY() + 3.2) {
			return false;
		}

		if (facing.getAxis() == Direction.Axis.X) {
			return position.z >= threshold.getZ() - 0.15 && position.z <= threshold.getZ() + 1.15;
		}

		return position.x >= threshold.getX() - 0.15 && position.x <= threshold.getX() + 1.15;
	}

	private static double planeOffset(int step) {
		if (step > 0) {
			return 1.0;
		}

		if (step < 0) {
			return 0.0;
		}

		return 0.5;
	}
}

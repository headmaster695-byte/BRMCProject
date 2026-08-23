package com.jamesstevenson.brmc.gate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * A threshold that must read as continuous space, not a loading-screen hop.
 * Source and destination use the same coordinates so Immersive Portals can
 * mount an identity transform through the opening.
 */
public record SeamlessGate(
	GateKind kind,
	ResourceKey<Level> from,
	ResourceKey<Level> to,
	BlockPos threshold,
	Direction facing
) {
	public Vec3 alignedDestination() {
		return Vec3.atBottomCenterOf(this.threshold.relative(this.facing));
	}

	public Vec3 alignedOrigin() {
		return Vec3.atBottomCenterOf(this.threshold);
	}
}

package com.jamesstevenson.brmc.gate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/**
 * Immersive Portals–class contract for one opening: two dimensions, one
 * identity plane, per-portal LOD, dest volume already on the far side.
 *
 * This is the architecture the 26.2 backend implements. A later dual-world
 * renderer should keep this type and replace only the see-through.
 */
public interface LinkedDimensionPortal {
	GateKind kind();

	ResourceKey<Level> from();

	ResourceKey<Level> to();

	BlockPos threshold();

	Direction facing();

	DestinationVolume destVolume();

	default boolean previewLies() {
		return this.kind().previewLies();
	}

	default boolean farSideDistorts() {
		return this.kind().farSideDistorts();
	}

	default PresentationLock.Crossing crossing() {
		return PresentationLock.crossing(this.kind());
	}
}

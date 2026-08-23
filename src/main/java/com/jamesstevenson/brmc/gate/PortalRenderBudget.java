package com.jamesstevenson.brmc.gate;

/**
 * Per-frame, per-portal render budget. Near portals spend FULL slots;
 * farther / many open portals degrade so FPS does not pay a fullscreen
 * dest blit for every threshold.
 */
public final class PortalRenderBudget {
	public static final int FULL_CAP = 2;
	public static final int MESH_CAP = 4;
	public static final double FULL_DISTANCE = 14.0;
	public static final double MESH_DISTANCE = 40.0;

	private int fullUsed;
	private int meshUsed;
	private int visible;

	public void reset() {
		this.fullUsed = 0;
		this.meshUsed = 0;
		this.visible = 0;
	}

	public int visiblePortals() {
		return this.visible;
	}

	public PortalLod allocate(double distance, boolean destSampleReady) {
		this.visible++;
		PortalLod wanted = wanted(distance, destSampleReady);
		if (wanted == PortalLod.FULL && this.fullUsed >= FULL_CAP) {
			wanted = PortalLod.MESH;
		}

		if (wanted == PortalLod.MESH && this.meshUsed >= MESH_CAP) {
			wanted = PortalLod.IMPOSTOR;
		}

		if (wanted == PortalLod.FULL) {
			this.fullUsed++;
		} else if (wanted == PortalLod.MESH) {
			this.meshUsed++;
		}

		return wanted;
	}

	public static PortalLod wanted(double distance, boolean destSampleReady) {
		if (distance <= FULL_DISTANCE && destSampleReady) {
			return PortalLod.FULL;
		}

		if (distance <= MESH_DISTANCE) {
			return PortalLod.MESH;
		}

		return PortalLod.IMPOSTOR;
	}
}

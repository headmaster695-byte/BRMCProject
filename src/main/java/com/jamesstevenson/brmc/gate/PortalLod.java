package com.jamesstevenson.brmc.gate;

/**
 * Per-portal see-through fidelity. Not one forever-fullscreen blit.
 *
 * <ul>
 *   <li>{@link #FULL} — dest-sampled voxels from the linked dimension</li>
 *   <li>{@link #MESH} — dest-climate receding room (cheaper)</li>
 *   <li>{@link #IMPOSTOR} — tinted portal plane only</li>
 * </ul>
 *
 * True Immersive Portals FULL is a second camera into a live dest
 * {@code ClientLevel}. This pass's FULL is dest {@code ServerLevel} voxels
 * synced to the threshold — the correct hook, not a dual-world stencil.
 */
public enum PortalLod {
	FULL,
	MESH,
	IMPOSTOR;

	public boolean drawsDestVoxels() {
		return this == FULL;
	}

	public boolean drawsRoomMesh() {
		return this == MESH;
	}
}

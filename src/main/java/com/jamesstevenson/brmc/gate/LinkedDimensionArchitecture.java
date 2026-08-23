package com.jamesstevenson.brmc.gate;

/**
 * Design lock for First gates on 26.2 (no Immersive Portals artifact).
 *
 * <h2>Must</h2>
 * Continuous travel into another dimension through the plane. Camera and
 * player cross as one motion; the destination is already the other
 * dimension's volume. Not fade, not hop, not see-through wallpaper that
 * then teleports.
 *
 * <h2>This pass proves</h2>
 * <ul>
 *   <li>Per-portal {@link PortalLod} + {@link PortalRenderBudget} (not one
 *       fullscreen blit).</li>
 *   <li>{@link DestinationVolumeSampler} reads the dest {@code ServerLevel}
 *       at identity coords and syncs voxels to the threshold.</li>
 *   <li>{@link PortalCrossTracker} fires only on was-behind → now-through.</li>
 *   <li>Dest chunks are ticketed ({@code TicketType.PORTAL}) before the
 *       identity-pose dimension swap so the player lands in dest volume.</li>
 *   <li>Live First exits (commons, vestibule, utilities, curving,
 *       custodial) use plane-cross + dest sample + LOD + mercy return.
 *       OOB and false floor stay architecture-only.</li>
 *   <li>Commons: clean yellow→yellow dest sample; {@link GateKind#previewLies()}
 *       can draw First wallpaper that dest does not have.</li>
 *   <li>Vestibule door 2: yellow→red dest sample plus far-side
 *       chromatic / heat-haze.</li>
 *   <li>Dest stubs are tiled climate cells ({@code brmc:dest_climate}), not
 *       barren floor slabs.</li>
 * </ul>
 *
 * <h2>Honest gaps</h2>
 * True IP see-through is a second camera into a live dest
 * {@code ClientLevel} with stencil / portal clip and entity transfer.
 * 26.2 BER ({@code SubmitNodeCollector.submitCustomGeometry}) does not
 * give that in this commit. {@link PortalLod#FULL} is dest-sampled voxels,
 * not a dual-world stencil. First still paints a short dest-climate
 * backing alcove on commons / vestibule so IMPOSTOR / missing samples do
 * not show void. OOB / false-floor systems are not live. Dest interiors
 * are still stubs (one climate cell language, not full floors).
 *
 * Hop ({@link LoadingHopBackend}) stays non-default.
 */
public final class LinkedDimensionArchitecture {
	private LinkedDimensionArchitecture() {
	}

	public static boolean dualWorldClientLevel() {
		return false;
	}

	public static boolean destSampledSeeThrough() {
		return true;
	}

	public static boolean planeCrossIdentitySwap() {
		return true;
	}

	public static boolean hopIsDefault() {
		return false;
	}
}

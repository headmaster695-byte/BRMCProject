package com.jamesstevenson.brmc.client;

import net.fabricmc.api.ClientModInitializer;

public class BrmcClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Client portal rendering belongs to the Immersive Portals backend when present.
		// No “Entering X” overlay, fade-to-load, or nether swirl — see PresentationLock.
	}
}

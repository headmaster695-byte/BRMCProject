package com.jamesstevenson.brmc.client;

import net.fabricmc.api.ClientModInitializer;

public class BrmcClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LinkedVolumeClient.initialize();
	}
}

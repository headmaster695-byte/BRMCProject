package com.jamesstevenson.brmc;

import com.jamesstevenson.brmc.block.BrmcBlocks;
import com.jamesstevenson.brmc.command.BrmcCommands;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.gate.SeamlessGateService;
import com.jamesstevenson.brmc.rule.BuildingTracker;
import com.jamesstevenson.brmc.rule.MapsLieRule;
import com.jamesstevenson.brmc.rule.MiningRegenRule;
import com.jamesstevenson.brmc.rule.NavigationLiesRule;
import com.jamesstevenson.brmc.rule.UnresolvedSoundRule;
import com.jamesstevenson.brmc.spawn.ClarkColdOpen;
import com.jamesstevenson.brmc.worldgen.LayoutSanity;
import com.jamesstevenson.brmc.worldgen.YellowMonoChunkGenerator;

import net.minecraft.resources.Identifier;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrmcMod implements ModInitializer {
	public static final String MOD_ID = "brmc";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		BrmcDimensions.bootstrap();
		YellowMonoChunkGenerator.register();
		BrmcBlocks.initialize();
		SeamlessGateService.initialize();
		ClarkColdOpen.initialize();
		MiningRegenRule.initialize();
		BuildingTracker.initialize();
		MapsLieRule.initialize();
		NavigationLiesRule.initialize();
		UnresolvedSoundRule.initialize();
		BrmcCommands.register();
		LayoutSanity.bootstrap();
		LOGGER.info("BRMC First Dimension architecture loaded. Gate backend: {}", SeamlessGateService.backendName());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

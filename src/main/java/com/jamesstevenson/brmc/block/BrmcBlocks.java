package com.jamesstevenson.brmc.block;

import java.util.function.Function;

import com.jamesstevenson.brmc.gate.GateKind;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

public final class BrmcBlocks {
	public static final Block VESTIBULE_THRESHOLD = register(
		BrmcBlockItemIds.VESTIBULE_THRESHOLD,
		properties -> new ThresholdBlock(GateKind.VESTIBULE, properties),
		BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.STONE).noCollision().noOcclusion()
	);
	public static final Block COMMONS_THRESHOLD = register(
		BrmcBlockItemIds.COMMONS_THRESHOLD,
		properties -> new ThresholdBlock(GateKind.COMMONS, properties),
		BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.WOOL).noCollision().noOcclusion()
	);

	private BrmcBlocks() {
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(tab -> {
			tab.accept(VESTIBULE_THRESHOLD.asItem());
			tab.accept(COMMONS_THRESHOLD.asItem());
		});
	}

	private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		Block block = blockFactory.apply(properties.setId(id));
		return Registry.register(BuiltInRegistries.BLOCK, id, block);
	}

	private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		Block block = register(id.block(), blockFactory, properties);
		BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
		Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);
		return block;
	}
}

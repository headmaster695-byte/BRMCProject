package com.jamesstevenson.brmc.block;

import java.util.function.Function;

import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.worldgen.FirstPocket;

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

import org.jspecify.annotations.Nullable;

public final class BrmcBlocks {
	public static final Block VESTIBULE_THRESHOLD = threshold(BrmcBlockItemIds.VESTIBULE_THRESHOLD, GateKind.VESTIBULE, SoundType.STONE);
	public static final Block COMMONS_THRESHOLD = threshold(BrmcBlockItemIds.COMMONS_THRESHOLD, GateKind.COMMONS, SoundType.WOOL);
	public static final Block UTILITIES_THRESHOLD = threshold(BrmcBlockItemIds.UTILITIES_THRESHOLD, GateKind.UTILITIES, SoundType.STONE);
	public static final Block CURVING_HALL_THRESHOLD = threshold(BrmcBlockItemIds.CURVING_HALL_THRESHOLD, GateKind.CURVING_HALL, SoundType.WOOL);
	public static final Block FALSE_FLOOR_THRESHOLD = threshold(BrmcBlockItemIds.FALSE_FLOOR_THRESHOLD, GateKind.FALSE_FLOOR, SoundType.WOOL);
	public static final Block OOB_HOLE = threshold(BrmcBlockItemIds.OOB_HOLE, GateKind.OOB_HOLE, SoundType.STONE);
	public static final Block CUSTODIAL_THRESHOLD = threshold(BrmcBlockItemIds.CUSTODIAL_THRESHOLD, GateKind.CUSTODIAL, SoundType.WOOL);

	private BrmcBlocks() {
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(tab -> {
			tab.accept(VESTIBULE_THRESHOLD.asItem());
			tab.accept(COMMONS_THRESHOLD.asItem());
			tab.accept(UTILITIES_THRESHOLD.asItem());
			tab.accept(CURVING_HALL_THRESHOLD.asItem());
			tab.accept(FALSE_FLOOR_THRESHOLD.asItem());
			tab.accept(OOB_HOLE.asItem());
			tab.accept(CUSTODIAL_THRESHOLD.asItem());
		});
	}

	public static @Nullable Block blockFor(FirstPocket pocket) {
		if (pocket == null || pocket.gate() == null) {
			return null;
		}

		return blockFor(pocket.gate());
	}

	public static @Nullable Block blockFor(GateKind kind) {
		if (kind == null) {
			return null;
		}

		return switch (kind) {
			case VESTIBULE -> VESTIBULE_THRESHOLD;
			case COMMONS -> COMMONS_THRESHOLD;
			case UTILITIES -> UTILITIES_THRESHOLD;
			case CURVING_HALL -> CURVING_HALL_THRESHOLD;
			case FALSE_FLOOR -> FALSE_FLOOR_THRESHOLD;
			case OOB_HOLE -> OOB_HOLE;
			case CUSTODIAL -> CUSTODIAL_THRESHOLD;
		};
	}

	private static Block threshold(BlockItemId id, GateKind kind, SoundType sound) {
		String visibleName = kind.architectureOnly() ? "block.brmc.opening" : "block.brmc.threshold";
		return register(
			id,
			visibleName,
			properties -> new ThresholdBlock(kind, properties.overrideDescription(visibleName)),
			BlockBehaviour.Properties.of().strength(1.5F).sound(sound).noCollision().noOcclusion()
		);
	}

	private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		Block block = blockFactory.apply(properties.setId(id));
		return Registry.register(BuiltInRegistries.BLOCK, id, block);
	}

	private static Block register(
		BlockItemId id,
		String visibleName,
		Function<BlockBehaviour.Properties, Block> blockFactory,
		BlockBehaviour.Properties properties
	) {
		Block block = register(id.block(), blockFactory, properties);
		BlockItem blockItem = new BlockItem(
			block,
			new Item.Properties().overrideDescription(visibleName).setId(id.item())
		);
		Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);
		return block;
	}
}

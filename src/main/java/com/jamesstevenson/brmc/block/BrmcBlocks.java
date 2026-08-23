package com.jamesstevenson.brmc.block;

import java.util.function.Function;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.worldgen.FirstPocket;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import org.jspecify.annotations.Nullable;

public final class BrmcBlocks {
	public static final String WALLPAPER_NAME = "block.brmc.wallpaper";
	public static final String CARPET_NAME = "block.brmc.carpet";
	public static final String CEILING_NAME = "block.brmc.ceiling_tile";
	public static final String LIGHT_NAME = TrofferBlock.VISIBLE_NAME;
	public static final String DOOR_NAME = "block.brmc.door";
	public static final String DOOR_FRAME_NAME = "block.brmc.door_frame";

	public static final Block VESTIBULE_THRESHOLD = threshold(BrmcBlockItemIds.VESTIBULE_THRESHOLD, GateKind.VESTIBULE, SoundType.STONE);
	public static final Block COMMONS_THRESHOLD = threshold(BrmcBlockItemIds.COMMONS_THRESHOLD, GateKind.COMMONS, SoundType.WOOL);
	public static final Block UTILITIES_THRESHOLD = threshold(BrmcBlockItemIds.UTILITIES_THRESHOLD, GateKind.UTILITIES, SoundType.STONE);
	public static final Block CURVING_HALL_THRESHOLD = threshold(BrmcBlockItemIds.CURVING_HALL_THRESHOLD, GateKind.CURVING_HALL, SoundType.WOOL);
	public static final Block FALSE_FLOOR_THRESHOLD = threshold(BrmcBlockItemIds.FALSE_FLOOR_THRESHOLD, GateKind.FALSE_FLOOR, SoundType.WOOL);
	public static final Block OOB_HOLE = threshold(BrmcBlockItemIds.OOB_HOLE, GateKind.OOB_HOLE, SoundType.WOOL);
	public static final Block CUSTODIAL_THRESHOLD = threshold(BrmcBlockItemIds.CUSTODIAL_THRESHOLD, GateKind.CUSTODIAL, SoundType.WOOL);

	public static final Block FIRST_WALLPAPER = palette(BrmcBlockItemIds.FIRST_WALLPAPER, WALLPAPER_NAME, Block::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_WALLPAPER_B = palette(BrmcBlockItemIds.FIRST_WALLPAPER_B, WALLPAPER_NAME, Block::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_WALLPAPER_C = palette(BrmcBlockItemIds.FIRST_WALLPAPER_C, WALLPAPER_NAME, Block::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_WALLPAPER_SEAM = palette(BrmcBlockItemIds.FIRST_WALLPAPER_SEAM, WALLPAPER_NAME, Block::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_WALLPAPER_PEEL = palette(BrmcBlockItemIds.FIRST_WALLPAPER_PEEL, WALLPAPER_NAME, Block::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_WALLPAPER_DEAD = palette(BrmcBlockItemIds.FIRST_WALLPAPER_DEAD, WALLPAPER_NAME, Block::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_CARPET = palette(BrmcBlockItemIds.FIRST_CARPET, CARPET_NAME, CarpetBlock::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_CARPET_TORN = palette(BrmcBlockItemIds.FIRST_CARPET_TORN, CARPET_NAME, CarpetBlock::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_DEBRIS_CARPET = palette(BrmcBlockItemIds.FIRST_DEBRIS_CARPET, CARPET_NAME, Block::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_CARPET_STAINED = palette(BrmcBlockItemIds.FIRST_CARPET_STAINED, CARPET_NAME, CarpetBlock::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_CARPET_DRY = palette(BrmcBlockItemIds.FIRST_CARPET_DRY, CARPET_NAME, CarpetBlock::new, yellowMono(SoundType.WOOL));
	public static final Block FIRST_CEILING_TILE = palette(BrmcBlockItemIds.FIRST_CEILING_TILE, CEILING_NAME, Block::new, yellowMono(SoundType.STONE));
	public static final Block FIRST_TROFFER = palette(
		BrmcBlockItemIds.FIRST_TROFFER,
		LIGHT_NAME,
		TrofferBlock::new,
		yellowMono(SoundType.GLASS).lightLevel(state -> 15)
	);
	public static final Block FIRST_TROFFER_DEAD = palette(
		BrmcBlockItemIds.FIRST_TROFFER_DEAD,
		LIGHT_NAME,
		TrofferBlock::new,
		yellowMono(SoundType.GLASS)
	);
	public static final Block FIRST_TROFFER_HALF = palette(
		BrmcBlockItemIds.FIRST_TROFFER_HALF,
		LIGHT_NAME,
		TrofferBlock::new,
		yellowMono(SoundType.GLASS).lightLevel(state -> 7)
	);
	public static final Block FIRST_DOOR_COMMERCIAL = palette(BrmcBlockItemIds.FIRST_DOOR_COMMERCIAL, DOOR_NAME, Block::new, yellowMono(SoundType.WOOD));
	public static final Block FIRST_DOOR_FRAME = palette(BrmcBlockItemIds.FIRST_DOOR_FRAME, DOOR_FRAME_NAME, Block::new, yellowMono(SoundType.WOOD));
	public static final Block FIRST_DOOR_VESTIBULE = palette(BrmcBlockItemIds.FIRST_DOOR_VESTIBULE, DOOR_NAME, Block::new, yellowMono(SoundType.WOOD));

	public static final Block SECOND_WALLPAPER = palette(BrmcBlockItemIds.SECOND_WALLPAPER, WALLPAPER_NAME, Block::new, crimsonMono(SoundType.WOOL));
	public static final Block SECOND_WALLPAPER_B = palette(BrmcBlockItemIds.SECOND_WALLPAPER_B, WALLPAPER_NAME, Block::new, crimsonMono(SoundType.WOOL));
	public static final Block SECOND_WALLPAPER_C = palette(BrmcBlockItemIds.SECOND_WALLPAPER_C, WALLPAPER_NAME, Block::new, crimsonMono(SoundType.WOOL));
	public static final Block SECOND_CARPET = palette(BrmcBlockItemIds.SECOND_CARPET, CARPET_NAME, CarpetBlock::new, crimsonMono(SoundType.WOOL));
	public static final Block SECOND_CARPET_MOLD = palette(BrmcBlockItemIds.SECOND_CARPET_MOLD, CARPET_NAME, CarpetBlock::new, crimsonMono(SoundType.WOOL));
	public static final Block SECOND_CARPET_TORN = palette(BrmcBlockItemIds.SECOND_CARPET_TORN, CARPET_NAME, CarpetBlock::new, crimsonMono(SoundType.WOOL));
	public static final Block SECOND_DEBRIS_CARPET = palette(BrmcBlockItemIds.SECOND_DEBRIS_CARPET, CARPET_NAME, Block::new, crimsonMono(SoundType.WOOL));
	public static final Block SECOND_CEILING_TILE = palette(BrmcBlockItemIds.SECOND_CEILING_TILE, CEILING_NAME, Block::new, crimsonMono(SoundType.STONE));
	public static final Block SECOND_TROFFER = palette(
		BrmcBlockItemIds.SECOND_TROFFER,
		LIGHT_NAME,
		TrofferBlock::new,
		crimsonMono(SoundType.GLASS).lightLevel(state -> 15)
	);
	public static final Block SECOND_TROFFER_DEAD = palette(
		BrmcBlockItemIds.SECOND_TROFFER_DEAD,
		LIGHT_NAME,
		TrofferBlock::new,
		crimsonMono(SoundType.GLASS)
	);
	public static final Block SECOND_DOOR_COMMERCIAL = palette(BrmcBlockItemIds.SECOND_DOOR_COMMERCIAL, DOOR_NAME, Block::new, crimsonMono(SoundType.WOOD));
	public static final Block SECOND_DOOR_FRAME = palette(BrmcBlockItemIds.SECOND_DOOR_FRAME, DOOR_FRAME_NAME, Block::new, crimsonMono(SoundType.WOOD));

	public static final ResourceKey<CreativeModeTab> BUILDING_TAB = ResourceKey.create(
		Registries.CREATIVE_MODE_TAB,
		Identifier.fromNamespaceAndPath(BrmcMod.MOD_ID, "building")
	);

	private BrmcBlocks() {
	}

	public static void initialize() {
		Registry.register(
			BuiltInRegistries.CREATIVE_MODE_TAB,
			BUILDING_TAB,
			FabricCreativeModeTab.builder()
				.title(Component.translatable("itemGroup.brmc.building"))
				.icon(() -> new ItemStack(FIRST_WALLPAPER.asItem()))
				.displayItems((params, output) -> {
					// Primaries only — variants stay registered, off this muted tab.
					output.accept(FIRST_WALLPAPER);
					output.accept(FIRST_CARPET);
					output.accept(FIRST_CEILING_TILE);
					output.accept(FIRST_TROFFER);
					output.accept(FIRST_DOOR_COMMERCIAL);
					output.accept(FIRST_DOOR_FRAME);
					output.accept(SECOND_WALLPAPER);
					output.accept(SECOND_CARPET);
					output.accept(SECOND_CEILING_TILE);
					output.accept(SECOND_TROFFER);
					output.accept(SECOND_DOOR_COMMERCIAL);
					output.accept(SECOND_DOOR_FRAME);
					output.accept(VESTIBULE_THRESHOLD);
					output.accept(OOB_HOLE);
				})
				.build()
		);
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

	private static BlockBehaviour.Properties yellowMono(SoundType sound) {
		return BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW).strength(0.8F).sound(sound);
	}

	private static BlockBehaviour.Properties crimsonMono(SoundType sound) {
		return BlockBehaviour.Properties.of().mapColor(DyeColor.RED).strength(0.8F).sound(sound);
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

	private static Block palette(
		BlockItemId id,
		String visibleName,
		Function<BlockBehaviour.Properties, Block> blockFactory,
		BlockBehaviour.Properties properties
	) {
		return register(id, visibleName, propertiesIn -> blockFactory.apply(propertiesIn.overrideDescription(visibleName)), properties);
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

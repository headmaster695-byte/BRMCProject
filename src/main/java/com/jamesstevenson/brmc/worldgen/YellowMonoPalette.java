package com.jamesstevenson.brmc.worldgen;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Yellow-mono placeholders: chevron wallpaper, moist carpet, troffer ceiling.
 * Apartment / utilities use a material break from the hub.
 * Vestibule airlock stays yellow; door 2 already frames red mono.
 */
public final class YellowMonoPalette {
	public enum Role {
		BEDROCK,
		SUBFLOOR,
		FLOOR,
		MOIST_CARPET,
		CHEVRON_LIGHT,
		CHEVRON_DARK,
		CEILING_TILE,
		TROFFER,
		VESTIBULE_FRAME,
		COMMONS_FRAME,
		HABITATION_WALL,
		HABITATION_FLOOR,
		HABITATION_BED,
		HABITATION_KITCHEN,
		HABITATION_CLOSET,
		UTILITY_WALL,
		UTILITY_FLOOR,
		UTILITY_OZONE,
		UTILITY_CONTACTOR,
		SECOND_RED_FLOOR,
		SECOND_RED_WALL,
		SECOND_RED_CARPET
	}

	private YellowMonoPalette() {
	}

	public static BlockState wallpaper(int worldX, int y, int worldZ) {
		if (y == YellowMonoLayout.CARPET_Y) {
			return state(Role.CHEVRON_DARK);
		}

		return YellowMonoLayout.chevronDark(worldX, y, worldZ) ? state(Role.CHEVRON_DARK) : state(Role.CHEVRON_LIGHT);
	}

	public static BlockState state(Role role) {
		return switch (role) {
			case BEDROCK -> Blocks.BEDROCK.defaultBlockState();
			case SUBFLOOR -> Blocks.SMOOTH_STONE.defaultBlockState();
			case FLOOR -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case MOIST_CARPET -> Blocks.CARPET.pick(DyeColor.YELLOW).defaultBlockState();
			case CHEVRON_LIGHT -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case CHEVRON_DARK -> Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW).defaultBlockState();
			case CEILING_TILE -> Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case TROFFER -> Blocks.OCHRE_FROGLIGHT.defaultBlockState();
			case VESTIBULE_FRAME -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case COMMONS_FRAME -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case HABITATION_WALL -> Blocks.WOOL.pick(DyeColor.WHITE).defaultBlockState();
			case HABITATION_FLOOR -> Blocks.OAK_PLANKS.defaultBlockState();
			case HABITATION_BED -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case HABITATION_KITCHEN -> Blocks.IRON_BLOCK.defaultBlockState();
			case HABITATION_CLOSET -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case UTILITY_WALL -> Blocks.SMOOTH_STONE.defaultBlockState();
			case UTILITY_FLOOR -> Blocks.IRON_BLOCK.defaultBlockState();
			case UTILITY_OZONE -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.OXIDIZED).defaultBlockState();
			case UTILITY_CONTACTOR -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED).defaultBlockState();
			case SECOND_RED_FLOOR -> Blocks.WOOL.pick(DyeColor.RED).defaultBlockState();
			case SECOND_RED_WALL -> Blocks.DYED_TERRACOTTA.pick(DyeColor.RED).defaultBlockState();
			case SECOND_RED_CARPET -> Blocks.CARPET.pick(DyeColor.RED).defaultBlockState();
		};
	}
}

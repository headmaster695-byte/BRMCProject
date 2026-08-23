package com.jamesstevenson.brmc.worldgen;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Yellow-mono placeholders: chevron wallpaper, moist-carpet *read*, troffer ceiling.
 * Carpet voxel is vanilla yellow display texture — a compromise, not a wet macro.
 * True damp carpet is a documented miss. Apartment / utilities break the hub.
 * Vestibule stays yellow-mono. Dest Second is red via dest sample at door 2
 * only; First-side yellow→red framing is a miss.
 */
public final class YellowMonoPalette {
	public enum Role {
		BEDROCK,
		SUBFLOOR,
		FLOOR,
		YELLOW_CARPET,
		CHEVRON_LIGHT,
		CHEVRON_DARK,
		CEILING_TILE,
		TROFFER,
		HABITATION_WALL,
		HABITATION_FLOOR,
		HABITATION_BED,
		HABITATION_KITCHEN,
		HABITATION_CLOSET,
		UTILITY_WALL,
		UTILITY_FLOOR,
		UTILITY_OZONE,
		UTILITY_CONTACTOR
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
			case YELLOW_CARPET -> Blocks.CARPET.pick(DyeColor.YELLOW).defaultBlockState();
			case CHEVRON_LIGHT -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case CHEVRON_DARK -> Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW).defaultBlockState();
			case CEILING_TILE -> Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case TROFFER -> Blocks.OCHRE_FROGLIGHT.defaultBlockState();
			case HABITATION_WALL -> Blocks.WOOL.pick(DyeColor.WHITE).defaultBlockState();
			case HABITATION_FLOOR -> Blocks.OAK_PLANKS.defaultBlockState();
			case HABITATION_BED -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case HABITATION_KITCHEN -> Blocks.IRON_BLOCK.defaultBlockState();
			case HABITATION_CLOSET -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case UTILITY_WALL -> Blocks.SMOOTH_STONE.defaultBlockState();
			case UTILITY_FLOOR -> Blocks.IRON_BLOCK.defaultBlockState();
			case UTILITY_OZONE -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.OXIDIZED).defaultBlockState();
			case UTILITY_CONTACTOR -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED).defaultBlockState();
		};
	}
}

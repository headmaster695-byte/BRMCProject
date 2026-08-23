package com.jamesstevenson.brmc.worldgen;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Yellow-mono placeholders: chevron wallpaper, moist carpet, troffer ceiling.
 * Apartment / utilities use a material break from the hub.
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
		UTILITY_WALL,
		UTILITY_FLOOR
	}

	private YellowMonoPalette() {
	}

	public static BlockState wallpaper(int worldX, int worldZ) {
		return YellowMonoLayout.chevronDark(worldX, worldZ) ? state(Role.CHEVRON_DARK) : state(Role.CHEVRON_LIGHT);
	}

	public static BlockState state(Role role) {
		return switch (role) {
			case BEDROCK -> Blocks.BEDROCK.defaultBlockState();
			case SUBFLOOR -> Blocks.SMOOTH_STONE.defaultBlockState();
			case FLOOR -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case MOIST_CARPET -> Blocks.CARPET.pick(DyeColor.YELLOW).defaultBlockState();
			case CHEVRON_LIGHT -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case CHEVRON_DARK -> Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW).defaultBlockState();
			case CEILING_TILE -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case TROFFER -> Blocks.OCHRE_FROGLIGHT.defaultBlockState();
			case VESTIBULE_FRAME -> Blocks.SMOOTH_STONE.defaultBlockState();
			case COMMONS_FRAME -> Blocks.WOOL.pick(DyeColor.ORANGE).defaultBlockState();
			case HABITATION_WALL -> Blocks.WOOL.pick(DyeColor.WHITE).defaultBlockState();
			case HABITATION_FLOOR -> Blocks.OAK_PLANKS.defaultBlockState();
			case UTILITY_WALL -> Blocks.SMOOTH_STONE.defaultBlockState();
			case UTILITY_FLOOR -> Blocks.IRON_BLOCK.defaultBlockState();
		};
	}
}

package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.block.BrmcBlocks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

/**
 * First P0 palette plus apartment / utilities material breaks.
 * Wallpaper / carpet / ceiling / troffer / door are {@code brmc:first_*}
 * blocks. Carpet TEMP is beige loop-pile nap (blotch *read* is texture-only).
 * Vestibule door TEMP is glass-airlock grammar, not yellow→red. Wear is noise.
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
		if (YellowMonoLayout.pocketAt(worldX, worldZ) == FirstPocket.FLUORESCENT_DEAD_ZONE) {
			return BrmcBlocks.FIRST_WALLPAPER_DEAD.defaultBlockState();
		}

		if (YellowMonoLayout.isWallpaperPeel(worldX, y, worldZ)) {
			return BrmcBlocks.FIRST_WALLPAPER_PEEL.defaultBlockState();
		}

		if (YellowMonoLayout.isWallpaperDeadWear(worldX, y, worldZ)) {
			return BrmcBlocks.FIRST_WALLPAPER_DEAD.defaultBlockState();
		}

		if (y == YellowMonoLayout.CARPET_Y || YellowMonoLayout.chevronDark(worldX, y, worldZ)) {
			return wallpaperSeamPhase(worldX, y, worldZ).defaultBlockState();
		}

		return BrmcBlocks.FIRST_WALLPAPER.defaultBlockState();
	}

	/** B/C are the same print, phase-shifted so a seam does not tile as a landmark. */
	private static Block wallpaperSeamPhase(int worldX, int y, int worldZ) {
		return Math.floorMod(worldX + worldZ * 2 + (y >> 1), 2) == 0
			? BrmcBlocks.FIRST_WALLPAPER_B
			: BrmcBlocks.FIRST_WALLPAPER_C;
	}

	public static BlockState carpet(int worldX, int worldZ) {
		if (YellowMonoLayout.isCarpetStain(worldX, worldZ)) {
			return BrmcBlocks.FIRST_CARPET_STAINED.defaultBlockState();
		}

		if (YellowMonoLayout.isCarpetDry(worldX, worldZ)) {
			return BrmcBlocks.FIRST_CARPET_DRY.defaultBlockState();
		}

		return BrmcBlocks.FIRST_CARPET.defaultBlockState();
	}

	public static BlockState ceiling() {
		return BrmcBlocks.FIRST_CEILING_TILE.defaultBlockState();
	}

	public static BlockState troffer(int worldX, int worldZ) {
		if (YellowMonoLayout.isDeadTroffer(worldX, worldZ)) {
			return BrmcBlocks.FIRST_TROFFER_DEAD.defaultBlockState();
		}

		if (YellowMonoLayout.isHalfTroffer(worldX, worldZ)) {
			return BrmcBlocks.FIRST_TROFFER_HALF.defaultBlockState();
		}

		return BrmcBlocks.FIRST_TROFFER.defaultBlockState();
	}

	public static BlockState doorSkin(int worldX, int worldZ) {
		if (YellowMonoLayout.isVestibuleDoorFrame(worldX, worldZ)) {
			return BrmcBlocks.FIRST_DOOR_FRAME.defaultBlockState();
		}

		if (YellowMonoLayout.isVestibuleDoor2Plane(worldX, worldZ)) {
			return BrmcBlocks.FIRST_DOOR_VESTIBULE.defaultBlockState();
		}

		return BrmcBlocks.FIRST_DOOR_COMMERCIAL.defaultBlockState();
	}

	public static BlockState state(Role role) {
		return switch (role) {
			case BEDROCK -> Blocks.BEDROCK.defaultBlockState();
			case SUBFLOOR -> Blocks.SMOOTH_STONE.defaultBlockState();
			case FLOOR -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case YELLOW_CARPET -> BrmcBlocks.FIRST_CARPET.defaultBlockState();
			case CHEVRON_LIGHT -> BrmcBlocks.FIRST_WALLPAPER.defaultBlockState();
			case CHEVRON_DARK -> BrmcBlocks.FIRST_WALLPAPER_B.defaultBlockState();
			case CEILING_TILE -> BrmcBlocks.FIRST_CEILING_TILE.defaultBlockState();
			case TROFFER -> BrmcBlocks.FIRST_TROFFER.defaultBlockState();
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

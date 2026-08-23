package com.jamesstevenson.brmc.worldgen;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Yellow-mono aesthetic placeholders for First.
 * Swap these states (or later custom blocks) without changing layout code.
 */
public final class YellowMonoPalette {
	public enum Role {
		BEDROCK,
		SUBFLOOR,
		FLOOR,
		CARPET,
		WALLPAPER,
		CEILING,
		LIGHT,
		VESTIBULE_FRAME,
		COMMONS_FRAME
	}

	private YellowMonoPalette() {
	}

	public static BlockState state(Role role) {
		return switch (role) {
			case BEDROCK -> Blocks.BEDROCK.defaultBlockState();
			case SUBFLOOR -> Blocks.SMOOTH_STONE.defaultBlockState();
			case FLOOR -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case CARPET -> Blocks.CARPET.pick(DyeColor.YELLOW).defaultBlockState();
			case WALLPAPER -> Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW).defaultBlockState();
			case CEILING -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			case LIGHT -> Blocks.OCHRE_FROGLIGHT.defaultBlockState();
			case VESTIBULE_FRAME -> Blocks.SMOOTH_STONE.defaultBlockState();
			case COMMONS_FRAME -> Blocks.WOOL.pick(DyeColor.ORANGE).defaultBlockState();
		};
	}
}

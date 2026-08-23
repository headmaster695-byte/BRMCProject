package com.jamesstevenson.brmc.worldgen;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Thick dest stubs: a recognisable climate cell at identity coords, not a
 * barren floor slab. Still stubs — not full floors.
 */
public enum DestClimate implements StringRepresentable {
	YELLOW_MONO("yellow_mono"),
	RED_MONO("red_mono"),
	SOFT_YELLOW("soft_yellow"),
	PLANT("plant"),
	SPIRAL_WELL("spiral_well"),
	CUSTODIAL("custodial");

	public static final StringRepresentable.EnumCodec<DestClimate> CODEC = StringRepresentable.fromEnum(DestClimate::values);

	private final String id;

	DestClimate(String id) {
		this.id = id;
	}

	@Override
	public String getSerializedName() {
		return this.id;
	}

	public BlockState floor() {
		return switch (this) {
			case YELLOW_MONO, SOFT_YELLOW -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case RED_MONO -> Blocks.WOOL.pick(DyeColor.RED).defaultBlockState();
			case PLANT -> Blocks.IRON_BLOCK.defaultBlockState();
			case SPIRAL_WELL -> Blocks.WOOL.pick(DyeColor.GRAY).defaultBlockState();
			case CUSTODIAL -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
		};
	}

	public BlockState carpet() {
		return switch (this) {
			case YELLOW_MONO -> Blocks.CARPET.pick(DyeColor.YELLOW).defaultBlockState();
			case SOFT_YELLOW -> Blocks.CARPET.pick(DyeColor.LIME).defaultBlockState();
			case RED_MONO -> Blocks.CARPET.pick(DyeColor.RED).defaultBlockState();
			case PLANT -> Blocks.CARPET.pick(DyeColor.GRAY).defaultBlockState();
			case SPIRAL_WELL -> Blocks.CARPET.pick(DyeColor.GRAY).defaultBlockState();
			case CUSTODIAL -> Blocks.CARPET.pick(DyeColor.WHITE).defaultBlockState();
		};
	}

	public BlockState wall(int worldX, int y, int worldZ) {
		return switch (this) {
			case YELLOW_MONO -> YellowMonoLayout.chevronDark(worldX, y, worldZ)
				? Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW).defaultBlockState()
				: Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case SOFT_YELLOW -> Blocks.WOOL.pick(DyeColor.YELLOW).defaultBlockState();
			case RED_MONO -> YellowMonoLayout.chevronDark(worldX, y, worldZ)
				? Blocks.DYED_TERRACOTTA.pick(DyeColor.RED).defaultBlockState()
				: Blocks.WOOL.pick(DyeColor.RED).defaultBlockState();
			case PLANT -> Math.floorMod(worldX + worldZ, 5) == 0
				? Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.OXIDIZED).defaultBlockState()
				: Blocks.SMOOTH_STONE.defaultBlockState();
			case SPIRAL_WELL -> Blocks.DEEPSLATE.defaultBlockState();
			case CUSTODIAL -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
		};
	}

	public BlockState ceiling(boolean light) {
		if (light) {
			return switch (this) {
				case PLANT -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED).defaultBlockState();
				case SPIRAL_WELL -> Blocks.SMOOTH_STONE.defaultBlockState();
				default -> Blocks.OCHRE_FROGLIGHT.defaultBlockState();
			};
		}

		return switch (this) {
			case RED_MONO -> Blocks.WOOL.pick(DyeColor.RED).defaultBlockState();
			case PLANT, SPIRAL_WELL -> Blocks.SMOOTH_STONE.defaultBlockState();
			case CUSTODIAL -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
			default -> Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY).defaultBlockState();
		};
	}

	public BlockState prop() {
		return switch (this) {
			case PLANT -> Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED).defaultBlockState();
			case CUSTODIAL -> Blocks.IRON_BLOCK.defaultBlockState();
			case SOFT_YELLOW -> Blocks.WOOL.pick(DyeColor.LIME).defaultBlockState();
			case SPIRAL_WELL -> Blocks.DEEPSLATE.defaultBlockState();
			default -> this.floor();
		};
	}

	public static boolean isWellShaft(int worldX, int worldZ) {
		int localX = YellowMonoLayout.localInCell(worldX);
		int localZ = YellowMonoLayout.localInCell(worldZ);
		return localX >= 3 && localX <= 5 && localZ >= 3 && localZ <= 5;
	}

	public static boolean isPerimeterWall(int worldX, int worldZ) {
		int localX = YellowMonoLayout.localInCell(worldX);
		int localZ = YellowMonoLayout.localInCell(worldZ);
		boolean edge = localX == 0 || localX == 7 || localZ == 0 || localZ == 7;
		if (!edge) {
			return false;
		}

		boolean opening = (localX == 0 || localX == 7) && localZ >= 1 && localZ <= 6
			|| (localZ == 0 || localZ == 7) && localX >= 1 && localX <= 6;
		return !opening;
	}

	public static boolean isLight(int worldX, int worldZ) {
		int localX = YellowMonoLayout.localInCell(worldX);
		int localZ = YellowMonoLayout.localInCell(worldZ);
		return localZ == 4 && localX >= 3 && localX <= 5;
	}

	public static boolean isProp(int worldX, int worldZ) {
		int localX = YellowMonoLayout.localInCell(worldX);
		int localZ = YellowMonoLayout.localInCell(worldZ);
		return localX == 2 && (localZ == 2 || localZ == 5);
	}

	public static int wellFloorY() {
		return YellowMonoLayout.FLOOR_Y - 8;
	}
}

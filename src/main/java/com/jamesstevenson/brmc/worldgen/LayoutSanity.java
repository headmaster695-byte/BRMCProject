package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.block.BrmcBlocks;
import com.jamesstevenson.brmc.block.ThresholdBlock;
import com.jamesstevenson.brmc.gate.LinkedOpenings;

/**
 * Invariant checks for the First layout lock. Failures log; they do not crash play.
 */
public final class LayoutSanity {
	private LayoutSanity() {
	}

	public static void bootstrap() {
		int errors = 0;
		if (!YellowMonoLayout.inClarkChamber(YellowMonoLayout.SPAWN_X, YellowMonoLayout.SPAWN_Z)) {
			BrmcMod.LOGGER.error("Clark spawn is outside the authored chamber.");
			errors++;
		}

		if (YellowMonoLayout.pocketAt(YellowMonoLayout.SPAWN_X, YellowMonoLayout.SPAWN_Z) != FirstPocket.CLARK_CHAMBER) {
			BrmcMod.LOGGER.error("Clark spawn is not tagged first-clark-chamber.");
			errors++;
		}

		if (YellowMonoLayout.pocketAt(52, 20) != null) {
			BrmcMod.LOGGER.error("East cell before the airlock must stay uncommon yellow, not a vestibule aperture.");
			errors++;
		}

		if (!YellowMonoLayout.isClarkColumn(8, 8)
			|| !YellowMonoLayout.isClarkColumn(9, 9)
			|| YellowMonoLayout.isClarkColumn(10, 10)) {
			BrmcMod.LOGGER.error("Clark columns must be 2×2 structural squares, not 1-wide posts.");
			errors++;
		}

		if (YellowMonoChunkGenerator.columnState(0, YellowMonoLayout.CARPET_Y, 16).isAir()) {
			BrmcMod.LOGGER.error("Clark walls must meet the carpet (no 1-block foot gap).");
			errors++;
		}

		if (!YellowMonoLayout.isTroffer(2, 2)
			|| !YellowMonoLayout.isTroffer(1, 2)
			|| !YellowMonoLayout.isTroffer(3, 2)
			|| !YellowMonoLayout.isTroffer(60, 20)
			|| !YellowMonoLayout.isTroffer(36, 20)
			|| YellowMonoLayout.isTroffer(-28, -20)
			|| !YellowMonoLayout.isDeadTroffer(-28, -20)
			|| !YellowMonoChunkGenerator.columnState(2, YellowMonoLayout.CEILING_Y, 2).is(BrmcBlocks.FIRST_TROFFER)
			|| !YellowMonoChunkGenerator.columnState(-28, YellowMonoLayout.CEILING_Y, -20).is(BrmcBlocks.FIRST_TROFFER_DEAD)) {
			BrmcMod.LOGGER.error("Troffer bars must match Clark/maze language; vestibule is not brighter; dead zone stays unlit.");
			errors++;
		}

		if (YellowMonoLayout.chevronDark(10, 66, 20) == YellowMonoLayout.chevronDark(10, 68, 20)) {
			BrmcMod.LOGGER.error("Chevron wallpaper must step with Y so walls read diagonal, not a flat stamp.");
			errors++;
		}

		if (YellowMonoLayout.CEILING_Y != YellowMonoLayout.CARPET_Y + 5) {
			BrmcMod.LOGGER.error("Yellow-mono layer height lock drifted.");
			errors++;
		}

		errors += expectPocket(60, 20, FirstPocket.VESTIBULE, "vestibule airlock");
		errors += expectPocket(20, 52, FirstPocket.COMMON_EXIT, "south spine commons");
		errors += expectPocket(-28, 20, FirstPocket.APARTMENT, "west spine apartment");
		errors += expectPocket(20, -28, FirstPocket.UTILITIES, "north spine utilities");
		errors += expectPocket(52, 52, FirstPocket.CURVING_HALL, "SE curving hall");
		errors += expectPocket(-20, 52, FirstPocket.FALSE_FLOOR, "SW false floor");
		errors += expectPocket(-28, -20, FirstPocket.FLUORESCENT_DEAD_ZONE, "NW dead zone");

		if (!YellowMonoLayout.isDoorway(31, 19)
			|| !YellowMonoLayout.isDoorway(31, 20)
			|| YellowMonoLayout.isDoorway(31, 18)
			|| YellowMonoLayout.isDoorway(31, 21)
			|| !YellowMonoLayout.isDoorway(19, 0)
			|| YellowMonoLayout.isDoorway(18, 0)) {
			BrmcMod.LOGGER.error("Clark openings must be 2-wide at 19–20 to match maze doors (hallway VP).");
			errors++;
		}

		if (YellowMonoLayout.openWest(7, 2)
			|| !YellowMonoLayout.isVestibulePairedLeaf(56, 19)
			|| !YellowMonoLayout.isVestibulePairedLeaf(56, 20)
			|| !YellowMonoLayout.isVestibulePairedLeaf(70, 19)
			|| !YellowMonoLayout.isVestibulePairedLeaf(70, 20)
			|| !YellowMonoLayout.isVestibuleDoorWall(56, 18)
			|| !YellowMonoLayout.isVestibuleDoorWall(56, 21)
			|| !YellowMonoLayout.isVestibuleDoorWall(70, 18)
			|| !YellowMonoLayout.isVestibuleDoorWall(70, 16)
			|| YellowMonoLayout.isVestibuleDoorWall(70, 19)
			|| !YellowMonoLayout.isDoorway(56, 19)
			|| !YellowMonoLayout.isDoorway(70, 20)
			|| !YellowMonoLayout.isThresholdAnchor(70, 19)
			|| !YellowMonoLayout.isThresholdAnchor(70, 20)) {
			BrmcMod.LOGGER.error("Vestibule must be paired office doors (2-wide leaves, yellow-mono walls), not an open mouth.");
			errors++;
		}

		if (YellowMonoChunkGenerator.columnState(70, YellowMonoLayout.CARPET_Y + 1, 18).is(net.minecraft.world.level.block.Blocks.WOOL.pick(net.minecraft.world.item.DyeColor.RED))
			|| YellowMonoChunkGenerator.columnState(76, YellowMonoLayout.CARPET_Y, 20).is(net.minecraft.world.level.block.Blocks.CARPET.pick(net.minecraft.world.item.DyeColor.RED))
			|| YellowMonoChunkGenerator.columnState(20, YellowMonoLayout.CARPET_Y, 52).is(net.minecraft.world.level.block.Blocks.CARPET.pick(net.minecraft.world.item.DyeColor.RED))
			|| !YellowMonoChunkGenerator.columnState(70, YellowMonoLayout.CARPET_Y + 1, 18).is(BrmcBlocks.FIRST_DOOR_FRAME)
			|| !YellowMonoChunkGenerator.columnState(70, YellowMonoLayout.CARPET_Y + 1, 16).is(BrmcBlocks.FIRST_DOOR_VESTIBULE)
			|| !YellowMonoChunkGenerator.columnState(56, YellowMonoLayout.CARPET_Y + 1, 16).is(BrmcBlocks.FIRST_DOOR_COMMERCIAL)) {
			BrmcMod.LOGGER.error("First must not paint a yellow→red vestibule frame; door 2 uses P0 door/frame blocks.");
			errors++;
		}

		if (!YellowMonoChunkGenerator.columnState(16, YellowMonoLayout.CARPET_Y, 16).is(BrmcBlocks.FIRST_CARPET)
			|| YellowMonoChunkGenerator.columnState(16, YellowMonoLayout.CARPET_Y, 16).is(net.minecraft.world.level.block.Blocks.WET_SPONGE)
			|| YellowMonoChunkGenerator.columnState(16, YellowMonoLayout.CARPET_Y, 16).is(net.minecraft.world.level.block.Blocks.MOSS_CARPET)) {
			BrmcMod.LOGGER.error("Clark field carpet must stay first_carpet — no wet-macro block.");
			errors++;
		}

		if (!YellowMonoChunkGenerator.columnState(0, YellowMonoLayout.CARPET_Y, 16).is(BrmcBlocks.FIRST_WALLPAPER)
			&& !YellowMonoChunkGenerator.columnState(0, YellowMonoLayout.CARPET_Y, 16).is(BrmcBlocks.FIRST_WALLPAPER_B)
			&& !YellowMonoChunkGenerator.columnState(0, YellowMonoLayout.CARPET_Y, 16).is(BrmcBlocks.FIRST_WALLPAPER_C)
			&& !YellowMonoChunkGenerator.columnState(0, YellowMonoLayout.CARPET_Y, 16).is(BrmcBlocks.FIRST_WALLPAPER_SEAM)) {
			BrmcMod.LOGGER.error("Clark walls must use first_wallpaper family, not vanilla wool.");
			errors++;
		}

		if (!YellowMonoChunkGenerator.columnState(16, YellowMonoLayout.CARPET_Y + 1, 16).isAir()
			|| !YellowMonoChunkGenerator.columnState(12, YellowMonoLayout.CARPET_Y + 1, 12).isAir()
			|| YellowMonoLayout.isKitchen(16, 16)
			|| YellowMonoLayout.isBed(16, 16)
			|| YellowMonoLayout.isContactor(16, 16)) {
			BrmcMod.LOGGER.error("Clark must stay an empty segmented volume — no store, props, or furniture.");
			errors++;
		}

		if (!YellowMonoLayout.isWall(-8, 18)
			|| !YellowMonoLayout.isDoorway(-8, 19)
			|| !YellowMonoLayout.isDoorway(-8, 20)
			|| YellowMonoLayout.isDoorway(-8, 17)
			|| !YellowMonoLayout.isWall(-8, 16)
			|| !YellowMonoLayout.isDoorway(32, 19)
			|| !YellowMonoLayout.isWall(32, 18)
			|| YellowMonoLayout.isWall(64, 20)
			|| YellowMonoLayout.isWall(24, 52)) {
			BrmcMod.LOGGER.error("Maze spines must recede through 2-wide doors; pocket interiors stay open; east is not a runway mouth.");
			errors++;
		}

		if (!YellowMonoLayout.isLinkedDestinationVolume(76, 20) || !YellowMonoLayout.isLinkedDestinationVolume(28, 52)) {
			BrmcMod.LOGGER.error("Linked destination volumes missing for vestibule/commons.");
			errors++;
		}

		errors += expectPocket(-39, 22, FirstPocket.APARTMENT_JANITOR, "apartment janitor closet");
		if (!YellowMonoLayout.isThresholdAnchor(19, -40)) {
			BrmcMod.LOGGER.error("Utilities deep door is not a threshold.");
			errors++;
		}

		if (!YellowMonoLayout.isThresholdAnchor(62, 62)) {
			BrmcMod.LOGGER.error("Curving hall seam is not a threshold.");
			errors++;
		}

		if (!YellowMonoLayout.isThresholdAnchor(-20, 52) || !YellowMonoLayout.isFalseFloorHole(-20, 52)) {
			BrmcMod.LOGGER.error("False floor hole is not a threshold.");
			errors++;
		}

		boolean quietBandTorn = false;
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (YellowMonoLayout.isFalseFloorTornCarpet(-20 + dx, 52 + dz)
					|| !YellowMonoLayout.isFalseFloorQuietBand(-20 + dx, 52 + dz)) {
					quietBandTorn = true;
				}
			}
		}

		if (quietBandTorn
			|| YellowMonoLayout.isFalseFloorTornCarpet(-18, 52)
			|| YellowMonoLayout.isFalseFloorTornCarpet(-20, 50)
			|| YellowMonoLayout.isFalseFloorTornCarpet(-22, 50)
			|| YellowMonoLayout.isFalseFloorTornCarpet(-17, 54)
			|| YellowMonoLayout.isFalseFloorTornCarpet(-23, 48)
			|| YellowMonoLayout.isFalseFloorTornCarpet(-17, 51)) {
			BrmcMod.LOGGER.error("False floor wear must not mark the pit — the cell stays field carpet around the hole.");
			errors++;
		}

		if (!YellowMonoChunkGenerator.columnState(-19, YellowMonoLayout.FLOOR_Y, 52).is(net.minecraft.world.level.block.Blocks.WOOL.pick(net.minecraft.world.item.DyeColor.YELLOW))
			|| !YellowMonoChunkGenerator.columnState(-19, YellowMonoLayout.CARPET_Y, 52).is(BrmcBlocks.FIRST_CARPET)
			|| YellowMonoChunkGenerator.columnState(-19, YellowMonoLayout.CARPET_Y, 52).is(BrmcBlocks.FIRST_CARPET_TORN)
			|| !YellowMonoChunkGenerator.columnState(-20, YellowMonoLayout.FLOOR_Y, 52).isAir()
			|| YellowMonoChunkGenerator.columnState(-19, YellowMonoLayout.FLOOR_Y, 52).is(net.minecraft.world.level.block.Blocks.SMOOTH_STONE_SLAB)) {
			BrmcMod.LOGGER.error("False floor hole neighbor must be ordinary first_carpet over yellow wool, not a torn nick or slab stair.");
			errors++;
		}

		net.minecraft.world.level.block.state.BlockState pitLook = YellowMonoChunkGenerator.columnState(-20, YellowMonoLayout.FLOOR_Y - 1, 52);
		if (pitLook.is(net.minecraft.world.level.block.Blocks.SMOOTH_STONE)
			|| !YellowMonoLayout.isFalseFloorPitDebris(-20, YellowMonoLayout.FLOOR_Y - 1, 52)
			|| !pitLook.is(BrmcBlocks.FIRST_DEBRIS_CARPET)) {
			BrmcMod.LOGGER.error("False floor pit must be first_debris_carpet, not gray stone.");
			errors++;
		}

		net.minecraft.world.level.block.state.BlockState oobLook = YellowMonoChunkGenerator.columnState(67, YellowMonoLayout.FLOOR_Y - 1, 20);
		if (oobLook.is(net.minecraft.world.level.block.Blocks.SMOOTH_STONE)
			|| oobLook.isAir()
			|| !YellowMonoLayout.isYellowMonoPitDebris(67, YellowMonoLayout.FLOOR_Y - 1, 20)
			|| !oobLook.is(BrmcBlocks.FIRST_DEBRIS_CARPET)
			|| !YellowMonoChunkGenerator.columnState(67, YellowMonoLayout.FLOOR_Y, 20).isAir()) {
			BrmcMod.LOGGER.error("OOB mouth look-down must be first_debris_carpet, not gray void.");
			errors++;
		}

		if (!YellowMonoLayout.isThresholdAnchor(-39, 22)) {
			BrmcMod.LOGGER.error("Janitor closet is not a threshold.");
			errors++;
		}

		if (DestClimateChunkGenerator.columnState(DestClimate.YELLOW_MONO, 0, 66, 0).isAir()) {
			BrmcMod.LOGGER.error("False First dest cell is missing corner walls.");
			errors++;
		}

		if (!DestClimateChunkGenerator.columnState(DestClimate.YELLOW_MONO, 4, 66, 4).isAir()) {
			BrmcMod.LOGGER.error("False First dest cell center is not walkable.");
			errors++;
		}

		if (DestClimateChunkGenerator.columnState(DestClimate.YELLOW_MONO, 0, YellowMonoLayout.CARPET_Y + 1, 0)
			.is(BrmcBlocks.SECOND_WALLPAPER)
			|| DestClimateChunkGenerator.columnState(DestClimate.YELLOW_MONO, 4, YellowMonoLayout.CARPET_Y, 4)
			.is(BrmcBlocks.SECOND_CARPET)) {
			BrmcMod.LOGGER.error("False First dest must stay yellow-mono this pass — Second palette is vestibule dest only.");
			errors++;
		}

		net.minecraft.world.level.block.state.BlockState secondWall = DestClimateChunkGenerator.columnState(
			DestClimate.RED_MONO,
			64,
			YellowMonoLayout.CARPET_Y + 1,
			16
		);
		net.minecraft.world.level.block.state.BlockState secondCarpet = DestClimateChunkGenerator.columnState(
			DestClimate.RED_MONO,
			68,
			YellowMonoLayout.CARPET_Y,
			20
		);
		net.minecraft.world.level.block.state.BlockState secondFill = DestClimateChunkGenerator.columnState(
			DestClimate.RED_MONO,
			68,
			YellowMonoLayout.FLOOR_Y - 1,
			20
		);
		net.minecraft.world.level.block.state.BlockState secondLight = DestClimateChunkGenerator.columnState(
			DestClimate.RED_MONO,
			67,
			YellowMonoLayout.CEILING_Y,
			20
		);
		if (secondWall.is(net.minecraft.world.level.block.Blocks.WOOL.pick(net.minecraft.world.item.DyeColor.RED))
			|| secondWall.is(net.minecraft.world.level.block.Blocks.DYED_TERRACOTTA.pick(net.minecraft.world.item.DyeColor.RED))
			|| secondCarpet.is(net.minecraft.world.level.block.Blocks.CARPET.pick(net.minecraft.world.item.DyeColor.RED))
			|| secondFill.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)
			|| secondFill.is(net.minecraft.world.level.block.Blocks.SMOOTH_STONE)
			|| !isSecondWallpaper(secondWall)
			|| !secondCarpet.is(BrmcBlocks.SECOND_CARPET)
			|| !secondFill.is(BrmcBlocks.SECOND_DEBRIS_CARPET)
			|| !secondLight.is(BrmcBlocks.SECOND_TROFFER)
			|| !DestClimateChunkGenerator.columnState(
				DestClimate.RED_MONO,
				70,
				YellowMonoLayout.CARPET_Y + 1,
				18
			).is(BrmcBlocks.SECOND_DOOR_FRAME)
			|| !DestClimateChunkGenerator.columnState(
				DestClimate.RED_MONO,
				70,
				YellowMonoLayout.CARPET_Y + 1,
				16
			).is(BrmcBlocks.SECOND_DOOR_COMMERCIAL)
			|| RedMonoPalette.isCarpetMold(70, 19)
			|| RedMonoPalette.isCarpetTorn(70, 20)
			|| RedMonoPalette.isDeadTroffer(67, 20)) {
			BrmcMod.LOGGER.error("Second dest must be crimson P0 mono at vestibule arrival — no vanilla red, no gray fill, no landmark wear.");
			errors++;
		}

		net.minecraft.world.level.block.state.BlockState thirdField = DestClimateChunkGenerator.columnState(
			DestClimate.COLD_GARAGE,
			20,
			YellowMonoLayout.CARPET_Y,
			20
		);
		net.minecraft.world.level.block.state.BlockState thirdFill = DestClimateChunkGenerator.columnState(
			DestClimate.COLD_GARAGE,
			20,
			YellowMonoLayout.FLOOR_Y - 1,
			20
		);
		net.minecraft.world.level.block.state.BlockState thirdLight = DestClimateChunkGenerator.columnState(
			DestClimate.COLD_GARAGE,
			19,
			YellowMonoLayout.CEILING_Y,
			20
		);
		net.minecraft.world.level.block.state.BlockState thirdCorner = DestClimateChunkGenerator.columnState(
			DestClimate.COLD_GARAGE,
			16,
			YellowMonoLayout.CARPET_Y + 1,
			16
		);
		if (thirdField.is(net.minecraft.world.level.block.Blocks.SMOOTH_STONE)
			|| thirdField.is(net.minecraft.world.level.block.Blocks.WOOL.pick(net.minecraft.world.item.DyeColor.GRAY))
			|| thirdFill.is(net.minecraft.world.level.block.Blocks.DEEPSLATE)
			|| thirdFill.is(net.minecraft.world.level.block.Blocks.SMOOTH_STONE)
			|| !thirdField.is(BrmcBlocks.THIRD_CONCRETE)
			|| !thirdFill.is(BrmcBlocks.THIRD_CONCRETE)
			|| !thirdLight.is(BrmcBlocks.THIRD_FLUORESCENT)
			|| !(thirdCorner.is(BrmcBlocks.THIRD_PILLAR) || thirdCorner.is(BrmcBlocks.THIRD_PIPE))
			|| !DestClimateChunkGenerator.columnState(
				DestClimate.COLD_GARAGE,
				23,
				YellowMonoLayout.CARPET_Y + 1,
				19
			).is(BrmcBlocks.THIRD_DOOR_RAMP)
			|| !DestClimateChunkGenerator.columnState(
				DestClimate.COLD_GARAGE,
				23,
				YellowMonoLayout.CARPET_Y + 1,
				18
			).is(BrmcBlocks.THIRD_DOOR_FRAME)
			|| DestClimateChunkGenerator.columnState(
				DestClimate.COLD_GARAGE,
				23,
				YellowMonoLayout.CARPET_Y + 1,
				19
			).getBlock() instanceof ThresholdBlock
			|| ColdGaragePalette.isStallPaint(19, 20)
			|| ColdGaragePalette.isStallPaint(20, 19)
			|| ColdGaragePalette.isDeadFluorescent(19, 20)
			|| DestClimateChunkGenerator.columnState(DestClimate.RED_MONO, 68, YellowMonoLayout.CARPET_Y, 20)
				.is(BrmcBlocks.THIRD_CONCRETE)
			|| DestClimateChunkGenerator.columnState(DestClimate.YELLOW_MONO, 4, YellowMonoLayout.CARPET_Y, 4)
				.is(BrmcBlocks.THIRD_CONCRETE)) {
			BrmcMod.LOGGER.error("Third dest must be cold-garage P0 — concrete family, stall lie, ramp liar not EXIT, First/Second untouched.");
			errors++;
		}

		if (!DestClimateChunkGenerator.columnState(DestClimate.SPIRAL_WELL, -20, 64, 52).isAir()) {
			BrmcMod.LOGGER.error("Spiral well shaft is sealed at the false-floor identity column.");
			errors++;
		}

		for (LinkedOpenings.Opening opening : LinkedOpenings.ALL) {
			for (net.minecraft.core.BlockPos identity : opening.identities()) {
				if (!YellowMonoLayout.isThresholdAnchor(identity.getX(), identity.getZ())) {
					BrmcMod.LOGGER.error("Live opening {} identity {},{} is not a First threshold.", opening.kind(), identity.getX(), identity.getZ());
					errors++;
				}

				if (!(DestClimateChunkGenerator.columnState(
					opening.destClimate(),
					identity.getX(),
					YellowMonoLayout.CARPET_Y,
					identity.getZ()
				).getBlock() instanceof ThresholdBlock)) {
					BrmcMod.LOGGER.error("Dest return plane missing for {} at {},{}.", opening.kind(), identity.getX(), identity.getZ());
					errors++;
				}
			}
		}

		if (errors == 0) {
			BrmcMod.LOGGER.info("First layout sanity: Clark 32×32, cardinal spines, authored pockets placed. Second dest is crimson P0. Third dest is cold garage P0.");
		} else {
			BrmcMod.LOGGER.error("First layout sanity failed with {} issue(s).", errors);
		}
	}

	private static int expectPocket(int x, int z, FirstPocket expected, String label) {
		if (YellowMonoLayout.inClarkChamber(x, z)) {
			BrmcMod.LOGGER.error("Pocket {} at {},{} is inside Clark.", label, x, z);
			return 1;
		}

		FirstPocket actual = YellowMonoLayout.pocketAt(x, z);
		if (actual != expected) {
			BrmcMod.LOGGER.error("Pocket {} at {},{} is {} (expected {}).", label, x, z, actual, expected);
			return 1;
		}

		return 0;
	}

	private static boolean isSecondWallpaper(net.minecraft.world.level.block.state.BlockState state) {
		return state.is(BrmcBlocks.SECOND_WALLPAPER)
			|| state.is(BrmcBlocks.SECOND_WALLPAPER_B)
			|| state.is(BrmcBlocks.SECOND_WALLPAPER_C);
	}
}

package com.jamesstevenson.brmc.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/**
 * Infinite procedural yellow-mono hub plus authored anchors (anti-noise-soup).
 * Clark chamber is the cold-open spawn: post-threshold yellow room only.
 * Cardinal spines make pockets learnable from the first chamber.
 */
public final class YellowMonoLayout {
	public static final int MIN_Y = 0;
	public static final int HEIGHT = 256;
	public static final int FLOOR_Y = 64;
	public static final int CARPET_Y = 65;
	public static final int ROOM_HEIGHT = 4;
	public static final int CEILING_Y = CARPET_Y + ROOM_HEIGHT;
	public static final int CELL = 8;

	public static final int CLARK_MIN = 0;
	public static final int CLARK_MAX = 31;
	public static final int SPAWN_X = 16;
	public static final int SPAWN_Z = 16;
	public static final float SPAWN_Y_ROT = -90.0F;

	public static final int SPINE_CELL = 2;

	private YellowMonoLayout() {
	}

	public static int cellCoord(int world) {
		return Math.floorDiv(world, CELL);
	}

	public static int localInCell(int world) {
		int local = world % CELL;
		return local < 0 ? local + CELL : local;
	}

	public static int cellOrigin(int cell) {
		return cell * CELL;
	}

	public static Vec3 cellCenter(int cellX, int cellZ) {
		return new Vec3(cellOrigin(cellX) + 4.5, CARPET_Y + 1, cellOrigin(cellZ) + 4.5);
	}

	public static boolean inClarkChamber(int worldX, int worldZ) {
		return worldX >= CLARK_MIN && worldX <= CLARK_MAX && worldZ >= CLARK_MIN && worldZ <= CLARK_MAX;
	}

	public static boolean isClarkColumn(int worldX, int worldZ) {
		return inClarkChamber(worldX, worldZ)
			&& (worldX == 8 || worldX == 23)
			&& (worldZ == 8 || worldZ == 23);
	}

	public static FirstPocket pocketAt(int worldX, int worldZ) {
		if (inClarkChamber(worldX, worldZ)) {
			return FirstPocket.CLARK_CHAMBER;
		}

		int cellX = cellCoord(worldX);
		int cellZ = cellCoord(worldZ);

		if (cellZ == SPINE_CELL && cellX >= 6 && cellX <= 9) {
			if (cellX == 8 && localInCell(worldX) == 3 && localInCell(worldZ) == 4) {
				return FirstPocket.VESTIBULE_OOB;
			}

			return FirstPocket.VESTIBULE;
		}

		if (cellZ == 6 && (cellX == SPINE_CELL || cellX == 3)) {
			return FirstPocket.COMMON_EXIT;
		}

		if (cellZ == SPINE_CELL && (cellX == -4 || cellX == -5)) {
			if (isJanitorCloset(worldX, worldZ)) {
				return FirstPocket.APARTMENT_JANITOR;
			}

			return FirstPocket.APARTMENT;
		}

		if (cellX == SPINE_CELL && (cellZ == -4 || cellZ == -5)) {
			return FirstPocket.UTILITIES;
		}

		if (isSoftCurveCell(cellX, cellZ)) {
			return FirstPocket.CURVING_HALL;
		}

		if (cellX == -3 && cellZ == 6) {
			return FirstPocket.FALSE_FLOOR;
		}

		if (isFluorescentDeadRun(cellX, cellZ)) {
			return FirstPocket.FLUORESCENT_DEAD_ZONE;
		}

		return null;
	}

	public static boolean isWall(int worldX, int worldZ) {
		if (inClarkChamber(worldX, worldZ)) {
			if (isClarkColumn(worldX, worldZ)) {
				return true;
			}

			return worldX == CLARK_MIN || worldX == CLARK_MAX || worldZ == CLARK_MIN || worldZ == CLARK_MAX;
		}

		int localX = localInCell(worldX);
		int localZ = localInCell(worldZ);
		int cellX = cellCoord(worldX);
		int cellZ = cellCoord(worldZ);
		boolean westWall = localX == 0 && !openWest(cellX, cellZ);
		boolean southWall = localZ == 0 && !openSouth(cellX, cellZ);
		return westWall || southWall;
	}

	public static boolean isDoorway(int worldX, int worldZ) {
		if (inClarkChamber(worldX, worldZ)) {
			boolean east = worldX == CLARK_MAX && worldZ >= 18 && worldZ <= 21;
			boolean south = worldZ == CLARK_MAX && worldX >= 18 && worldX <= 21;
			boolean west = worldX == CLARK_MIN && worldZ >= 18 && worldZ <= 21;
			boolean north = worldZ == CLARK_MIN && worldX >= 18 && worldX <= 21;
			return east || south || west || north;
		}

		int localX = localInCell(worldX);
		int localZ = localInCell(worldZ);
		int cellX = cellCoord(worldX);
		int cellZ = cellCoord(worldZ);

		if (localX == 0 && localZ >= 3 && localZ <= 4 && openWest(cellX, cellZ)) {
			return true;
		}

		return localZ == 0 && localX >= 3 && localX <= 4 && openSouth(cellX, cellZ);
	}

	public static boolean isThresholdAnchor(int worldX, int worldZ) {
		FirstPocket pocket = pocketAt(worldX, worldZ);
		if (pocket == null || pocket.gate() == null) {
			return false;
		}

		int localX = localInCell(worldX);
		int localZ = localInCell(worldZ);

		return switch (pocket) {
			case VESTIBULE -> cellCoord(worldX) == 8 && localX == 6 && localZ == 4;
			case VESTIBULE_OOB -> true;
			case COMMON_EXIT -> localX == 7 && localZ == 4;
			case UTILITIES -> isDeepDoor(worldX, worldZ);
			case CURVING_HALL -> cellCoord(worldX) == 7 && cellCoord(worldZ) == 7 && localX == 6 && localZ == 6;
			case FALSE_FLOOR -> localX == 4 && localZ == 4;
			case APARTMENT_JANITOR -> localX == 1 && localZ == 6;
			default -> false;
		};
	}

	public static boolean isAirlockInterior(int worldX, int worldZ) {
		return cellCoord(worldX) == 7 && cellCoord(worldZ) == SPINE_CELL;
	}

	/**
	 * Door 2 already frames red mono. One stride through this strip is
	 * yellow→red. Commons must never use this.
	 */
	public static boolean isDoor2RedFrame(int worldX, int worldZ) {
		int cellX = cellCoord(worldX);
		int cellZ = cellCoord(worldZ);
		if (cellZ != SPINE_CELL) {
			return false;
		}

		return cellX == 9 || cellX == 8 && localInCell(worldX) >= 6;
	}

	/**
	 * First-side backing alcove matching dest climate. The dest volume itself
	 * is sampled from the dest ServerLevel; this fill only hides void when
	 * LOD is IMPOSTOR or the dest sample has not arrived yet.
	 */
	public static boolean isLinkedDestinationVolume(int worldX, int worldZ) {
		int cellX = cellCoord(worldX);
		int cellZ = cellCoord(worldZ);
		return cellZ == SPINE_CELL && cellX == 9 || cellZ == 6 && cellX == 3;
	}

	public static boolean isJanitorCloset(int worldX, int worldZ) {
		return cellCoord(worldX) == -5 && cellCoord(worldZ) == SPINE_CELL
			&& localInCell(worldX) <= 3 && localInCell(worldZ) >= 4;
	}

	public static boolean isKitchen(int worldX, int worldZ) {
		return pocketAt(worldX, worldZ) == FirstPocket.APARTMENT
			&& cellCoord(worldX) == -4
			&& localInCell(worldX) >= 5 && localInCell(worldX) <= 7
			&& localInCell(worldZ) >= 1 && localInCell(worldZ) <= 3;
	}

	public static boolean isBed(int worldX, int worldZ) {
		return pocketAt(worldX, worldZ) == FirstPocket.APARTMENT
			&& cellCoord(worldX) == -4
			&& localInCell(worldX) >= 1 && localInCell(worldX) <= 3
			&& localInCell(worldZ) >= 1 && localInCell(worldZ) <= 2;
	}

	public static boolean isPlantRun(int worldX, int worldZ) {
		return pocketAt(worldX, worldZ) == FirstPocket.UTILITIES
			&& localInCell(worldX) >= 3 && localInCell(worldX) <= 4;
	}

	public static boolean isDeepDoor(int worldX, int worldZ) {
		return pocketAt(worldX, worldZ) == FirstPocket.UTILITIES
			&& cellCoord(worldZ) == -5
			&& localInCell(worldZ) == 0
			&& localInCell(worldX) >= 3 && localInCell(worldX) <= 4;
	}

	public static boolean isOzoneStain(int worldX, int worldZ) {
		return pocketAt(worldX, worldZ) == FirstPocket.UTILITIES
			&& Math.floorMod(worldX + worldZ, 5) == 0;
	}

	public static boolean isContactor(int worldX, int worldZ) {
		return pocketAt(worldX, worldZ) == FirstPocket.UTILITIES
			&& localInCell(worldX) == 2
			&& (localInCell(worldZ) == 2 || localInCell(worldZ) == 5);
	}

	public static boolean isSoftCurveCell(int cellX, int cellZ) {
		return cellX == 6 && cellZ == 6
			|| cellX == 7 && cellZ == 6
			|| cellX == 7 && cellZ == 7;
	}

	public static boolean isFluorescentDeadRun(int cellX, int cellZ) {
		return cellZ == -3 && cellX <= -3 && cellX >= -5;
	}

	public static boolean isLayerShell(int y) {
		return y <= FLOOR_Y || y >= CEILING_Y;
	}

	public static boolean isLight(int worldX, int worldZ) {
		FirstPocket pocket = pocketAt(worldX, worldZ);
		if (pocket == FirstPocket.FLUORESCENT_DEAD_ZONE) {
			return false;
		}

		if (inClarkChamber(worldX, worldZ)) {
			return worldX % 4 == 2 && worldZ % 4 == 2;
		}

		int localX = localInCell(worldX);
		int localZ = localInCell(worldZ);
		return localX == 4 && localZ == 4;
	}

	/** Two-wide diagonal chevron bands — wallpaper family, not a 1-block checker. */
	public static boolean chevronDark(int worldX, int worldZ) {
		int stripe = Math.floorMod(worldX - (worldZ & ~1), 4);
		return stripe <= 1;
	}

	public static boolean openWest(int cellX, int cellZ) {
		if (pocketAt(cellX * CELL + 1, cellZ * CELL + 1) != null) {
			return true;
		}

		if (spineOpenWest(cellX, cellZ)) {
			return true;
		}

		if (mergedWithWest(cellX, cellZ)) {
			return true;
		}

		return (hash(cellX, cellZ) & 1) == 0;
	}

	public static boolean openSouth(int cellX, int cellZ) {
		if (pocketAt(cellX * CELL + 1, cellZ * CELL + 1) != null) {
			return true;
		}

		if (spineOpenSouth(cellX, cellZ)) {
			return true;
		}

		if (mergedWithSouth(cellX, cellZ)) {
			return true;
		}

		return (hash(cellX, cellZ) & 1) == 1;
	}

	public static Vec3 warp(FirstPocket pocket) {
		return switch (pocket) {
			case CLARK_CHAMBER -> new Vec3(SPAWN_X + 0.5, CARPET_Y + 1, SPAWN_Z + 0.5);
			case VESTIBULE, VESTIBULE_OOB -> cellCenter(6, SPINE_CELL);
			case COMMON_EXIT -> cellCenter(SPINE_CELL, 6);
			case APARTMENT, APARTMENT_JANITOR -> cellCenter(-4, SPINE_CELL);
			case UTILITIES -> cellCenter(SPINE_CELL, -4);
			case CURVING_HALL -> cellCenter(6, 6);
			case FALSE_FLOOR -> cellCenter(-3, 6);
			case FLUORESCENT_DEAD_ZONE -> cellCenter(-4, -3);
		};
	}

	public static BlockPos warpBlock(FirstPocket pocket) {
		Vec3 vec = warp(pocket);
		return BlockPos.containing(vec.x, vec.y, vec.z);
	}

	private static boolean spineOpenWest(int cellX, int cellZ) {
		if (cellZ == SPINE_CELL && cellX == 4) {
			return true;
		}

		if (cellZ == SPINE_CELL && cellX >= 6 && cellX <= 9) {
			return true;
		}

		if (cellZ == 3 && (cellX == 5 || cellX == 6)) {
			return true;
		}

		if (cellZ == 6 && cellX == 3) {
			return true;
		}

		if (cellZ == SPINE_CELL && cellX <= -1 && cellX >= -5) {
			return true;
		}

		if (cellZ == 6 && cellX == 7) {
			return true;
		}

		if (cellZ == 6 && cellX <= 1 && cellX >= -3) {
			return true;
		}

		return cellZ == -3 && cellX <= 1 && cellX >= -5;
	}

	private static boolean spineOpenSouth(int cellX, int cellZ) {
		if (cellX == 4 && cellZ == 3 || cellX == 6 && cellZ == 3) {
			return true;
		}

		if (cellX == SPINE_CELL && cellZ >= 4 && cellZ <= 6) {
			return true;
		}

		if (cellX == SPINE_CELL && cellZ <= -1 && cellZ >= -5) {
			return true;
		}

		if (cellX == 6 && cellZ >= 3 && cellZ <= 6) {
			return true;
		}

		return cellX == 7 && cellZ == 7;
	}

	private static boolean mergedWithWest(int cellX, int cellZ) {
		return isMergedRoom(cellX, cellZ) && (cellX & 1) == 1 && isMergedRoom(cellX - 1, cellZ);
	}

	private static boolean mergedWithSouth(int cellX, int cellZ) {
		return isMergedRoom(cellX, cellZ) && (cellZ & 1) == 1 && isMergedRoom(cellX, cellZ - 1);
	}

	private static boolean isMergedRoom(int cellX, int cellZ) {
		if (Math.abs(cellX) <= 3 && Math.abs(cellZ) <= 3) {
			return false;
		}

		if (pocketAt(cellX * CELL + 1, cellZ * CELL + 1) != null) {
			return false;
		}

		int mx = Math.floorDiv(cellX, 2);
		int mz = Math.floorDiv(cellZ, 2);
		return (hash(mx * 31, mz * 17) & 7) == 0;
	}

	private static int hash(int cellX, int cellZ) {
		int h = cellX * 734287 + cellZ * 912931;
		h ^= h >> 16;
		h *= 0x45d9f3b;
		h ^= h >> 16;
		return h;
	}
}

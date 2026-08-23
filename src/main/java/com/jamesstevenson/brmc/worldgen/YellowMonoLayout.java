package com.jamesstevenson.brmc.worldgen;

/**
 * Infinite procedural yellow-mono hub plus authored anchors (anti-noise-soup).
 * Clark chamber is the cold-open spawn: post-threshold yellow room only.
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
	public static final int CLARK_MAX = 15;
	public static final int SPAWN_X = 8;
	public static final int SPAWN_Z = 8;

	private YellowMonoLayout() {
	}

	public static int cellCoord(int world) {
		return Math.floorDiv(world, CELL);
	}

	public static int localInCell(int world) {
		int local = world % CELL;
		return local < 0 ? local + CELL : local;
	}

	public static boolean inClarkChamber(int worldX, int worldZ) {
		return worldX >= CLARK_MIN && worldX <= CLARK_MAX && worldZ >= CLARK_MIN && worldZ <= CLARK_MAX;
	}

	public static FirstPocket pocketAt(int worldX, int worldZ) {
		if (inClarkChamber(worldX, worldZ)) {
			return FirstPocket.CLARK_CHAMBER;
		}

		int cellX = cellCoord(worldX);
		int cellZ = cellCoord(worldZ);

		if (cellX == 5 && cellZ == 0) {
			return FirstPocket.VESTIBULE;
		}

		if (cellX == 6 && cellZ == 0) {
			int localX = localInCell(worldX);
			int localZ = localInCell(worldZ);
			if (localX == 3 && localZ == 4) {
				return FirstPocket.VESTIBULE_OOB;
			}

			return FirstPocket.VESTIBULE;
		}

		if (cellX == 4 && cellZ == 0) {
			return FirstPocket.VESTIBULE;
		}

		if (cellX == 0 && cellZ == 4) {
			return FirstPocket.COMMON_EXIT;
		}

		if (cellX == -6 && cellZ == 0) {
			return FirstPocket.APARTMENT;
		}

		if (cellX == 0 && cellZ == -6) {
			return FirstPocket.UTILITIES;
		}

		if (cellX == 6 && cellZ == 6) {
			return FirstPocket.CURVING_HALL;
		}

		if (cellX == -4 && cellZ == 4) {
			return FirstPocket.FALSE_FLOOR;
		}

		if (cellX == -4 && cellZ == -4) {
			return FirstPocket.FLUORESCENT_DEAD_ZONE;
		}

		return null;
	}

	public static boolean isWall(int worldX, int worldZ) {
		if (inClarkChamber(worldX, worldZ)) {
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
			return (worldX == CLARK_MAX && worldZ >= 7 && worldZ <= 8)
				|| (worldZ == CLARK_MAX && worldX >= 7 && worldX <= 8);
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
			case VESTIBULE -> cellCoord(worldX) == 6 && localX == 6 && localZ == 4;
			case VESTIBULE_OOB -> true;
			case COMMON_EXIT -> localX == 7 && localZ == 4;
			case UTILITIES -> localX == 4 && localZ == 1;
			case CURVING_HALL -> localX == 6 && localZ == 6;
			case FALSE_FLOOR -> localX == 4 && localZ == 4;
			default -> false;
		};
	}

	public static boolean isAirlockInterior(int worldX, int worldZ) {
		return cellCoord(worldX) == 5 && cellCoord(worldZ) == 0;
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

	public static boolean chevronDark(int worldX, int worldZ) {
		return ((worldX + worldZ) & 1) == 0;
	}

	public static boolean openWest(int cellX, int cellZ) {
		if (pocketAt(cellX * CELL + 1, cellZ * CELL + 1) != null) {
			return true;
		}

		return (hash(cellX, cellZ) & 1) == 0;
	}

	public static boolean openSouth(int cellX, int cellZ) {
		if (pocketAt(cellX * CELL + 1, cellZ * CELL + 1) != null) {
			return true;
		}

		return (hash(cellX, cellZ) & 1) == 1;
	}

	private static int hash(int cellX, int cellZ) {
		int h = cellX * 734287 + cellZ * 912931;
		h ^= h >> 16;
		h *= 0x45d9f3b;
		h ^= h >> 16;
		return h;
	}
}

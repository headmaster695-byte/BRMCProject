package com.jamesstevenson.brmc.worldgen;

/**
 * Infinite procedural layout hook for First.
 *
 * Current stub: an 8-block cell grid with a binary-tree maze (open south or west).
 * Vestibule and commons cells are fixed so gates can be aligned across dimensions.
 */
public final class YellowMonoLayout {
	public static final int MIN_Y = 0;
	public static final int HEIGHT = 256;
	public static final int FLOOR_Y = 64;
	public static final int CARPET_Y = 65;
	public static final int ROOM_HEIGHT = 4;
	public static final int CEILING_Y = CARPET_Y + ROOM_HEIGHT;
	public static final int CELL = 8;

	public static final int VESTIBULE_CELL_X = 4;
	public static final int VESTIBULE_CELL_Z = 0;
	public static final int COMMONS_CELL_X = 0;
	public static final int COMMONS_CELL_Z = 4;

	public static final int SPAWN_X = 4;
	public static final int SPAWN_Z = 4;

	public enum CellKind {
		ROOM,
		VESTIBULE,
		COMMONS
	}

	private YellowMonoLayout() {
	}

	public static int cellCoord(int world) {
		return Math.floorDiv(world, CELL);
	}

	public static int localInCell(int world) {
		int local = world % CELL;
		return local < 0 ? local + CELL : local;
	}

	public static CellKind cellKind(int cellX, int cellZ) {
		if (cellX == VESTIBULE_CELL_X && cellZ == VESTIBULE_CELL_Z) {
			return CellKind.VESTIBULE;
		}

		if (cellX == COMMONS_CELL_X && cellZ == COMMONS_CELL_Z) {
			return CellKind.COMMONS;
		}

		return CellKind.ROOM;
	}

	public static boolean isWall(int worldX, int worldZ) {
		int localX = localInCell(worldX);
		int localZ = localInCell(worldZ);
		int cellX = cellCoord(worldX);
		int cellZ = cellCoord(worldZ);
		boolean westWall = localX == 0 && !openWest(cellX, cellZ);
		boolean southWall = localZ == 0 && !openSouth(cellX, cellZ);
		return westWall || southWall;
	}

	public static boolean isDoorway(int worldX, int worldZ) {
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
		int localX = localInCell(worldX);
		int localZ = localInCell(worldZ);
		CellKind kind = cellKind(cellCoord(worldX), cellCoord(worldZ));

		if (kind == CellKind.VESTIBULE) {
			return localX == 4 && localZ == 7;
		}

		if (kind == CellKind.COMMONS) {
			return localX == 7 && localZ == 4;
		}

		return false;
	}

	public static boolean isLight(int worldX, int worldZ) {
		int localX = localInCell(worldX);
		int localZ = localInCell(worldZ);
		return localX == 4 && localZ == 4;
	}

	/**
	 * Binary-tree maze: every cell opens west or south so the grid stays connected
	 * and infinite. Replace this with a richer Backrooms graph later.
	 */
	public static boolean openWest(int cellX, int cellZ) {
		if (cellKind(cellX, cellZ) == CellKind.VESTIBULE) {
			return true;
		}

		return (hash(cellX, cellZ) & 1) == 0;
	}

	public static boolean openSouth(int cellX, int cellZ) {
		if (cellKind(cellX, cellZ) == CellKind.COMMONS) {
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

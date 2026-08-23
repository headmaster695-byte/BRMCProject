package com.jamesstevenson.brmc.worldgen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.block.BrmcBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

public class YellowMonoChunkGenerator extends ChunkGenerator {
	public static final MapCodec<YellowMonoChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(YellowMonoChunkGenerator::getBiomeSource)
		).apply(instance, instance.stable(YellowMonoChunkGenerator::new))
	);

	public YellowMonoChunkGenerator(BiomeSource biomeSource) {
		super(biomeSource);
	}

	public static void register() {
		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, BrmcMod.id("yellow_mono"), CODEC);
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void applyCarvers(
		WorldGenRegion region,
		long seed,
		RandomState randomState,
		BiomeManager biomeManager,
		StructureManager structureManager,
		ChunkAccess chunk
	) {
	}

	@Override
	public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
		// First has no entities.
	}

	@Override
	public int getGenDepth() {
		return YellowMonoLayout.HEIGHT;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(
		Blender blender,
		RandomState randomState,
		StructureManager structureManager,
		ChunkAccess centerChunk
	) {
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		Heightmap oceanFloor = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
		Heightmap worldSurface = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
		int minBlockX = centerChunk.getPos().getMinBlockX();
		int minBlockZ = centerChunk.getPos().getMinBlockZ();

		for (int localX = 0; localX < 16; localX++) {
			for (int localZ = 0; localZ < 16; localZ++) {
				int worldX = minBlockX + localX;
				int worldZ = minBlockZ + localZ;
				boolean wall = YellowMonoLayout.isWall(worldX, worldZ);
				boolean doorway = YellowMonoLayout.isDoorway(worldX, worldZ);
				boolean threshold = YellowMonoLayout.isThresholdAnchor(worldX, worldZ);
				boolean light = YellowMonoLayout.isLight(worldX, worldZ);
				YellowMonoLayout.CellKind kind = YellowMonoLayout.cellKind(
					YellowMonoLayout.cellCoord(worldX),
					YellowMonoLayout.cellCoord(worldZ)
				);

				for (int y = YellowMonoLayout.MIN_Y; y <= YellowMonoLayout.CEILING_Y; y++) {
					BlockState state = columnState(y, wall, doorway, threshold, light, kind);
					if (state.isAir()) {
						continue;
					}

					centerChunk.setBlockState(cursor.set(localX, y, localZ), state);
					oceanFloor.update(localX, y, localZ, state);
					worldSurface.update(localX, y, localZ, state);
				}
			}
		}

		return CompletableFuture.completedFuture(centerChunk);
	}

	private static BlockState columnState(
		int y,
		boolean wall,
		boolean doorway,
		boolean threshold,
		boolean light,
		YellowMonoLayout.CellKind kind
	) {
		if (y == YellowMonoLayout.MIN_Y) {
			return YellowMonoPalette.state(YellowMonoPalette.Role.BEDROCK);
		}

		if (y < YellowMonoLayout.FLOOR_Y) {
			return YellowMonoPalette.state(YellowMonoPalette.Role.SUBFLOOR);
		}

		if (y == YellowMonoLayout.FLOOR_Y) {
			if (kind == YellowMonoLayout.CellKind.VESTIBULE) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.VESTIBULE_FRAME);
			}

			if (kind == YellowMonoLayout.CellKind.COMMONS) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.COMMONS_FRAME);
			}

			return YellowMonoPalette.state(YellowMonoPalette.Role.FLOOR);
		}

		if (y == YellowMonoLayout.CARPET_Y && !wall) {
			if (threshold) {
				return kind == YellowMonoLayout.CellKind.VESTIBULE
					? BrmcBlocks.VESTIBULE_THRESHOLD.defaultBlockState()
					: BrmcBlocks.COMMONS_THRESHOLD.defaultBlockState();
			}

			return YellowMonoPalette.state(YellowMonoPalette.Role.CARPET);
		}

		if (y > YellowMonoLayout.CARPET_Y && y < YellowMonoLayout.CEILING_Y) {
			if (wall && !doorway) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.WALLPAPER);
			}

			return Blocks.AIR.defaultBlockState();
		}

		if (y == YellowMonoLayout.CEILING_Y) {
			if (light) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.LIGHT);
			}

			return YellowMonoPalette.state(YellowMonoPalette.Role.CEILING);
		}

		return Blocks.AIR.defaultBlockState();
	}

	@Override
	public int getSeaLevel() {
		return YellowMonoLayout.FLOOR_Y;
	}

	@Override
	public int getMinY() {
		return YellowMonoLayout.MIN_Y;
	}

	@Override
	public int getSpawnHeight(LevelHeightAccessor heightAccessor) {
		return YellowMonoLayout.CARPET_Y + 1;
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
		return YellowMonoLayout.CARPET_Y + 1;
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) {
		int height = Math.min(heightAccessor.getHeight(), YellowMonoLayout.CEILING_Y + 1);
		BlockState[] column = new BlockState[height];
		boolean wall = YellowMonoLayout.isWall(x, z);
		boolean doorway = YellowMonoLayout.isDoorway(x, z);
		boolean threshold = YellowMonoLayout.isThresholdAnchor(x, z);
		boolean light = YellowMonoLayout.isLight(x, z);
		YellowMonoLayout.CellKind kind = YellowMonoLayout.cellKind(YellowMonoLayout.cellCoord(x), YellowMonoLayout.cellCoord(z));

		for (int i = 0; i < height; i++) {
			int y = heightAccessor.getMinY() + i;
			column[i] = columnState(y, wall, doorway, threshold, light, kind);
		}

		return new NoiseColumn(heightAccessor.getMinY(), column);
	}

	@Override
	public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {
		result.add(
			"BRMC first "
				+ YellowMonoLayout.cellKind(
					YellowMonoLayout.cellCoord(feetPos.getX()),
					YellowMonoLayout.cellCoord(feetPos.getZ())
				)
		);
	}
}

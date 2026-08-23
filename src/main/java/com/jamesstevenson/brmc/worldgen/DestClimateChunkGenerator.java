package com.jamesstevenson.brmc.worldgen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jamesstevenson.brmc.BrmcMod;
import com.jamesstevenson.brmc.gate.LinkedOpenings;
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

/**
 * Thick dest stub: tiled climate cells at First's Y band so identity
 * sampling sees a room, not a barren slab.
 */
public class DestClimateChunkGenerator extends ChunkGenerator {
	public static final MapCodec<DestClimateChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(DestClimateChunkGenerator::getBiomeSource),
			DestClimate.CODEC.fieldOf("climate").forGetter(DestClimateChunkGenerator::climate)
		).apply(instance, instance.stable(DestClimateChunkGenerator::new))
	);

	private final DestClimate climate;

	public DestClimateChunkGenerator(BiomeSource biomeSource, DestClimate climate) {
		super(biomeSource);
		this.climate = climate;
	}

	public DestClimate climate() {
		return this.climate;
	}

	public static void register() {
		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, BrmcMod.id("dest_climate"), CODEC);
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
				for (int y = YellowMonoLayout.MIN_Y; y <= YellowMonoLayout.CEILING_Y; y++) {
					BlockState state = columnState(this.climate, worldX, y, worldZ);
					if (state.isAir()) {
						continue;
					}

					cursor.set(localX, y, localZ);
					centerChunk.setBlockState(cursor, state);
					if (state.getBlock() instanceof net.minecraft.world.level.block.EntityBlock entityBlock) {
						var blockEntity = entityBlock.newBlockEntity(new BlockPos(worldX, y, worldZ), state);
						if (blockEntity != null) {
							centerChunk.setBlockEntity(blockEntity);
						}
					}

					oceanFloor.update(localX, y, localZ, state);
					worldSurface.update(localX, y, localZ, state);
				}
			}
		}

		return CompletableFuture.completedFuture(centerChunk);
	}

	static BlockState columnState(DestClimate climate, int worldX, int y, int worldZ) {
		boolean well = climate == DestClimate.SPIRAL_WELL && DestClimate.isWellShaft(worldX, worldZ);
		int wellFloor = DestClimate.wellFloorY();

		if (y == YellowMonoLayout.MIN_Y) {
			return Blocks.BEDROCK.defaultBlockState();
		}

		if (y < wellFloor) {
			return Blocks.DEEPSLATE.defaultBlockState();
		}

		if (well && y >= wellFloor && y < YellowMonoLayout.CEILING_Y) {
			if (y == wellFloor) {
				return climate.floor();
			}

			if (y == wellFloor + 1) {
				return climate.carpet();
			}

			return Blocks.AIR.defaultBlockState();
		}

		if (y < YellowMonoLayout.FLOOR_Y) {
			return climate == DestClimate.RED_MONO || climate == DestClimate.SPIRAL_WELL
				? Blocks.DEEPSLATE.defaultBlockState()
				: Blocks.SMOOTH_STONE.defaultBlockState();
		}

		if (y == YellowMonoLayout.FLOOR_Y) {
			return climate.floor();
		}

		boolean wall = DestClimate.isPerimeterWall(worldX, worldZ);
		if (!wall
			&& LinkedOpenings.isDestReturnColumn(climate, worldX, worldZ)
			&& y >= YellowMonoLayout.CARPET_Y
			&& y <= YellowMonoLayout.CARPET_Y + 2) {
			var marker = LinkedOpenings.destReturnBlock(climate);
			if (marker != null) {
				return marker.defaultBlockState();
			}
		}

		if (y == YellowMonoLayout.CARPET_Y) {
			if (wall) {
				return climate.wall(worldX, worldZ);
			}

			if (DestClimate.isProp(worldX, worldZ)) {
				return climate.prop();
			}

			return climate.carpet();
		}

		if (y > YellowMonoLayout.CARPET_Y && y < YellowMonoLayout.CEILING_Y) {
			if (wall) {
				return climate.wall(worldX, worldZ);
			}

			return Blocks.AIR.defaultBlockState();
		}

		if (y == YellowMonoLayout.CEILING_Y) {
			return climate.ceiling(DestClimate.isLight(worldX, worldZ));
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
		for (int i = 0; i < height; i++) {
			column[i] = columnState(this.climate, x, heightAccessor.getMinY() + i, z);
		}

		return new NoiseColumn(heightAccessor.getMinY(), column);
	}

	@Override
	public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {
	}
}

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
import net.minecraft.world.level.block.Block;
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
					BlockState state = columnState(worldX, y, worldZ);
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

	private static BlockState columnState(int worldX, int y, int worldZ) {
		boolean wall = YellowMonoLayout.isWall(worldX, worldZ);
		boolean doorway = YellowMonoLayout.isDoorway(worldX, worldZ);
		boolean threshold = YellowMonoLayout.isThresholdAnchor(worldX, worldZ);
		boolean light = YellowMonoLayout.isLight(worldX, worldZ);
		FirstPocket pocket = YellowMonoLayout.pocketAt(worldX, worldZ);

		if (y == YellowMonoLayout.MIN_Y) {
			return YellowMonoPalette.state(YellowMonoPalette.Role.BEDROCK);
		}

		if (y < YellowMonoLayout.FLOOR_Y) {
			if (pocket == FirstPocket.VESTIBULE_OOB && y >= YellowMonoLayout.FLOOR_Y - 2) {
				return Blocks.AIR.defaultBlockState();
			}

			return YellowMonoPalette.state(YellowMonoPalette.Role.SUBFLOOR);
		}

		if (y == YellowMonoLayout.FLOOR_Y) {
			if (pocket == FirstPocket.VESTIBULE_OOB || pocket == FirstPocket.FALSE_FLOOR && threshold) {
				return Blocks.AIR.defaultBlockState();
			}

			if (YellowMonoLayout.isDoor2RedFrame(worldX, worldZ)) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.SECOND_RED_FLOOR);
			}

			if (pocket == FirstPocket.APARTMENT || pocket == FirstPocket.APARTMENT_JANITOR) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.HABITATION_FLOOR);
			}

			if (pocket == FirstPocket.UTILITIES) {
				if (YellowMonoLayout.isPlantRun(worldX, worldZ)) {
					return YellowMonoPalette.state(YellowMonoPalette.Role.UTILITY_FLOOR);
				}

				if (YellowMonoLayout.isOzoneStain(worldX, worldZ)) {
					return YellowMonoPalette.state(YellowMonoPalette.Role.UTILITY_OZONE);
				}

				return YellowMonoPalette.state(YellowMonoPalette.Role.UTILITY_FLOOR);
			}

			return YellowMonoPalette.state(YellowMonoPalette.Role.FLOOR);
		}

		if (y == YellowMonoLayout.CARPET_Y && !wall) {
			if (pocket == FirstPocket.VESTIBULE_OOB || pocket == FirstPocket.FALSE_FLOOR && threshold) {
				Block marker = BrmcBlocks.blockFor(pocket);
				return marker != null ? marker.defaultBlockState() : Blocks.AIR.defaultBlockState();
			}

			if (threshold) {
				Block marker = BrmcBlocks.blockFor(pocket);
				if (marker != null) {
					return marker.defaultBlockState();
				}
			}

			if (YellowMonoLayout.isKitchen(worldX, worldZ)) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.HABITATION_KITCHEN);
			}

			if (YellowMonoLayout.isBed(worldX, worldZ)) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.HABITATION_BED);
			}

			if (YellowMonoLayout.isContactor(worldX, worldZ)) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.UTILITY_CONTACTOR);
			}

			if (pocket == FirstPocket.APARTMENT
				|| pocket == FirstPocket.APARTMENT_JANITOR
				|| pocket == FirstPocket.UTILITIES) {
				return Blocks.AIR.defaultBlockState();
			}

			if (YellowMonoLayout.isDoor2RedFrame(worldX, worldZ)) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.SECOND_RED_CARPET);
			}

			return YellowMonoPalette.state(YellowMonoPalette.Role.MOIST_CARPET);
		}

		if (y > YellowMonoLayout.CARPET_Y && y < YellowMonoLayout.CEILING_Y) {
			if (threshold && y <= YellowMonoLayout.CARPET_Y + 2) {
				Block marker = BrmcBlocks.blockFor(pocket);
				if (marker != null) {
					return marker.defaultBlockState();
				}
			}

			if (wall && !doorway) {
				if (YellowMonoLayout.isDoor2RedFrame(worldX, worldZ)) {
					return YellowMonoPalette.state(YellowMonoPalette.Role.SECOND_RED_WALL);
				}

				if (pocket == FirstPocket.APARTMENT_JANITOR) {
					return YellowMonoPalette.state(YellowMonoPalette.Role.HABITATION_CLOSET);
				}

				if (pocket == FirstPocket.APARTMENT) {
					return YellowMonoPalette.state(YellowMonoPalette.Role.HABITATION_WALL);
				}

				if (pocket == FirstPocket.UTILITIES) {
					if (YellowMonoLayout.isOzoneStain(worldX, worldZ)) {
						return YellowMonoPalette.state(YellowMonoPalette.Role.UTILITY_OZONE);
					}

					return YellowMonoPalette.state(YellowMonoPalette.Role.UTILITY_WALL);
				}

				return YellowMonoPalette.wallpaper(worldX, worldZ);
			}

			return Blocks.AIR.defaultBlockState();
		}

		if (y == YellowMonoLayout.CEILING_Y) {
			if (YellowMonoLayout.isDoor2RedFrame(worldX, worldZ)) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.SECOND_RED_WALL);
			}

			if (light) {
				return YellowMonoPalette.state(YellowMonoPalette.Role.TROFFER);
			}

			return YellowMonoPalette.state(YellowMonoPalette.Role.CEILING_TILE);
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
			column[i] = columnState(x, heightAccessor.getMinY() + i, z);
		}

		return new NoiseColumn(heightAccessor.getMinY(), column);
	}

	@Override
	public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {
		FirstPocket pocket = YellowMonoLayout.pocketAt(feetPos.getX(), feetPos.getZ());
		result.add("BRMC first " + (pocket == null ? "hub" : pocket.slug()));
	}
}

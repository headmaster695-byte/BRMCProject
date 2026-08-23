package com.jamesstevenson.brmc.block;

import java.util.Set;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class BrmcBlockEntities {
	public static final BlockEntityType<ThresholdBlockEntity> LINKED_VOLUME = Registry.register(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, BrmcMod.id("linked_volume")),
		new BlockEntityType<>(
			ThresholdBlockEntity::new,
			Set.of(
				BrmcBlocks.VESTIBULE_THRESHOLD,
				BrmcBlocks.COMMONS_THRESHOLD,
				BrmcBlocks.UTILITIES_THRESHOLD,
				BrmcBlocks.CURVING_HALL_THRESHOLD,
				BrmcBlocks.FALSE_FLOOR_THRESHOLD,
				BrmcBlocks.OOB_HOLE,
				BrmcBlocks.CUSTODIAL_THRESHOLD
			)
		)
	);

	private BrmcBlockEntities() {
	}

	public static void initialize() {
	}
}

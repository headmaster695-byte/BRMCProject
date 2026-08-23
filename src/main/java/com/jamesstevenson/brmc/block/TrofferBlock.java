package com.jamesstevenson.brmc.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * Fluorescent fixture. Block entity is the hum-buzz hook — no mobs, no sound yet.
 */
public class TrofferBlock extends net.minecraft.world.level.block.Block implements EntityBlock {
	public static final String VISIBLE_NAME = "block.brmc.light";

	public TrofferBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public MutableComponent getName() {
		return Component.translatable(VISIBLE_NAME);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TrofferBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (type != BrmcBlockEntities.TROFFER) {
			return null;
		}

		return (lvl, pos, st, be) -> TrofferBlockEntity.tick(lvl, pos, st, (TrofferBlockEntity) be);
	}
}

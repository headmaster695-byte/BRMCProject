package com.jamesstevenson.brmc.mixin;

import com.jamesstevenson.brmc.rule.BuildingTracker;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(method = "place", at = @At("RETURN"))
	private void brmc$markBuilt(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
		if (cir.getReturnValue() instanceof InteractionResult.Success && context.getLevel() instanceof ServerLevel level) {
			BuildingTracker.markPlayerBuilt(level, context.getClickedPos());
		}
	}
}

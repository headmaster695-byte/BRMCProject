package com.jamesstevenson.brmc.mixin;

import java.util.List;

import com.jamesstevenson.brmc.block.ThresholdBlock;
import com.jamesstevenson.brmc.rule.NavigationLiesRule;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * F3+H / advanced tooltips print {@code BuiltInRegistries.ITEM.getKey}.
 * Internal ids like {@code brmc:vestibule_threshold} must not show.
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "getTooltipLines", at = @At("RETURN"))
	private void brmc$hideRegistryIds(
		Item.TooltipContext context,
		@Nullable Player player,
		TooltipFlag tooltipFlag,
		CallbackInfoReturnable<List<Component>> cir
	) {
		ItemStack self = (ItemStack) (Object) this;
		if (!(self.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof ThresholdBlock)) {
			return;
		}

		List<Component> lines = cir.getReturnValue();
		if (lines == null || lines.isEmpty()) {
			return;
		}

		lines.removeIf(line -> NavigationLiesRule.isHiddenDebugLine(line.getString()));
	}
}

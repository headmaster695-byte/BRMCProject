package com.jamesstevenson.brmc.mixin;

import com.jamesstevenson.brmc.rule.NavigationLiesRule;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CompassItem.class)
public class CompassItemMixin {
	@Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
	private void brmc$compassLies(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot, CallbackInfo ci) {
		if (NavigationLiesRule.shouldLie(level)) {
			ci.cancel();
		}
	}
}

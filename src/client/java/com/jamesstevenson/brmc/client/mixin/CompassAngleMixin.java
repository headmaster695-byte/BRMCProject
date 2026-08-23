package com.jamesstevenson.brmc.client.mixin;

import com.jamesstevenson.brmc.rule.NavigationLiesRule;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CompassAngle.class)
public class CompassAngleMixin {
	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	private void brmc$compassSpins(ItemStack itemStack, ClientLevel level, ItemOwner owner, int seed, CallbackInfoReturnable<Float> cir) {
		if (level != null && NavigationLiesRule.shouldLieCompass(level)) {
			cir.setReturnValue((level.getGameTime() % 80L) / 80.0F);
		}
	}
}

package com.jamesstevenson.brmc.mixin;

import com.jamesstevenson.brmc.rule.MapsLieRule;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItem.class)
public class MapItemMixin {
	@Inject(method = "update", at = @At("HEAD"), cancellable = true)
	private void brmc$mapsLie(Level level, Entity player, MapItemSavedData data, CallbackInfo ci) {
		if (MapsLieRule.shouldLie(level)) {
			ci.cancel();
		}
	}
}

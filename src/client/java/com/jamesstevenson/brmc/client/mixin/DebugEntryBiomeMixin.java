package com.jamesstevenson.brmc.client.mixin;

import com.jamesstevenson.brmc.rule.NavigationLiesRule;

import net.minecraft.client.gui.components.debug.DebugEntryBiome;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugEntryBiome.class)
public class DebugEntryBiomeMixin {
	@Inject(method = "display", at = @At("HEAD"), cancellable = true)
	private void brmc$noBiomeSlug(
		DebugScreenDisplayer displayer,
		Level serverOrClientLevel,
		LevelChunk clientChunk,
		LevelChunk serverChunk,
		CallbackInfo ci
	) {
		if (NavigationLiesRule.shouldLie(serverOrClientLevel)) {
			ci.cancel();
		}
	}
}

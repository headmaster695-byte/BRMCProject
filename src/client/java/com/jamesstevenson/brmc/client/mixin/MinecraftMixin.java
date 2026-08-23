package com.jamesstevenson.brmc.client.mixin;

import com.jamesstevenson.brmc.client.LinkedVolumeClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	public ClientLevel level;

	@Inject(method = "setLevel", at = @At("HEAD"))
	private void brmc$markLinkedCross(ClientLevel newLevel, CallbackInfo ci) {
		if (this.level == null || newLevel == null) {
			return;
		}

		LinkedVolumeClient.markLinkedDimensionSwap(this.level.dimension(), newLevel.dimension());
	}

	@Inject(method = "setScreenAndShow", at = @At("HEAD"), cancellable = true)
	private void brmc$holdLoadingFrame(Screen screen, CallbackInfo ci) {
		if (screen instanceof LevelLoadingScreen && LinkedVolumeClient.isCrossing()) {
			ci.cancel();
		}
	}
}

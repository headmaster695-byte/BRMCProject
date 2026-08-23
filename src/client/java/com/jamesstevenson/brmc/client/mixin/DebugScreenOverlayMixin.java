package com.jamesstevenson.brmc.client.mixin;

import java.util.List;

import com.jamesstevenson.brmc.rule.NavigationLiesRule;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Belt-and-suspenders: the position mixin already omits vanilla's
 * {@code dimension().identifier()} line. This strips any leftover
 * {@code brmc:} resource-id text (dim, biome, targeted block) on BRMC worlds.
 */
@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
	@Inject(method = "extractLines", at = @At("HEAD"))
	private void brmc$hideDimensionIds(GuiGraphicsExtractor graphics, List<String> lines, boolean alignLeft, CallbackInfo ci) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null || !NavigationLiesRule.shouldLie(minecraft.level)) {
			return;
		}

		lines.removeIf(NavigationLiesRule::isHiddenDebugLine);
	}
}

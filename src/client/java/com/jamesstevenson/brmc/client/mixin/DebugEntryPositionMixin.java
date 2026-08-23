package com.jamesstevenson.brmc.client.mixin;

import java.util.List;
import java.util.Locale;

import com.jamesstevenson.brmc.rule.NavigationLiesRule;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugEntryPosition.class)
public class DebugEntryPositionMixin {
	@Inject(method = "display", at = @At("HEAD"), cancellable = true)
	private void brmc$coordinatesLie(
		DebugScreenDisplayer displayer,
		Level serverOrClientLevel,
		LevelChunk clientChunk,
		LevelChunk serverChunk,
		CallbackInfo ci
	) {
		Minecraft minecraft = Minecraft.getInstance();
		Level level = minecraft.level;
		if (level == null || !NavigationLiesRule.shouldLieCoordinates(level)) {
			return;
		}

		Entity entity = minecraft.getCameraEntity();
		if (entity == null) {
			ci.cancel();
			return;
		}

		int shift = NavigationLiesRule.coordinateShift(level);
		BlockPos feet = entity.blockPosition();
		double x = entity.getX() + shift;
		double y = entity.getY();
		double z = entity.getZ() - shift;
		displayer.addToGroup(
			DebugEntryPosition.GROUP,
			List.of(
				String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", x, y, z),
				String.format(Locale.ROOT, "Block: %d %d %d", feet.getX() + shift, feet.getY(), feet.getZ() - shift),
				"Chunk: -- -- --",
				"Facing: --"
			)
		);
		ci.cancel();
	}
}

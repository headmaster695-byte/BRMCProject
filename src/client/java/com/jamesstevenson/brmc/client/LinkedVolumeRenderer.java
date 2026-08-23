package com.jamesstevenson.brmc.client;

import com.jamesstevenson.brmc.block.ThresholdBlockEntity;
import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.gate.PresentationLock;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import org.jspecify.annotations.Nullable;

/**
 * Portal-plane + receding-room mesh. Approximated linked volume, not a second
 * ClientLevel. Commons stays yellow; vestibule door 2 is red climate.
 */
public class LinkedVolumeRenderer implements BlockEntityRenderer<ThresholdBlockEntity, LinkedVolumeRenderState> {
	private static final Identifier WOOL = Identifier.withDefaultNamespace("textures/block/white_wool.png");
	private static final float DEPTH = 6.0F;

	@Override
	public LinkedVolumeRenderState createRenderState() {
		return new LinkedVolumeRenderState();
	}

	@Override
	public void extractRenderState(
		ThresholdBlockEntity blockEntity,
		LinkedVolumeRenderState state,
		float partialTicks,
		Vec3 cameraPosition,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
	) {
		BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);
		state.kind = blockEntity.kind();
		state.crossing = PresentationLock.crossing(state.kind);
	}

	@Override
	public void submit(LinkedVolumeRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.kind != GateKind.VESTIBULE && state.kind != GateKind.COMMONS) {
			return;
		}

		boolean red = state.crossing == PresentationLock.Crossing.YELLOW_TO_RED;
		int plane = red ? ARGB.color(110, 138, 32, 32) : ARGB.color(90, 201, 180, 88);
		int volume = red ? ARGB.color(230, 138, 32, 32) : ARGB.color(220, 201, 180, 88);
		var type = RenderTypes.entityTranslucent(WOOL);

		poseStack.pushPose();
		collector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
			quad(pose, buffer, plane, 0.98F, 0.0F, 0.0F, 0.98F, 3.0F, 1.0F);
			quad(pose, buffer, volume, 0.98F, 0.0F, 0.0F, 0.98F + DEPTH, 0.02F, 1.0F);
			quad(pose, buffer, volume, 0.98F, 2.98F, 0.0F, 0.98F + DEPTH, 3.0F, 1.0F);
			quad(pose, buffer, volume, 0.98F, 0.0F, 0.0F, 0.98F + DEPTH, 3.0F, 0.02F);
			quad(pose, buffer, volume, 0.98F, 0.0F, 0.98F, 0.98F + DEPTH, 3.0F, 1.0F);
		});
		poseStack.popPose();
	}

	private static void quad(
		PoseStack.Pose pose,
		VertexConsumer buffer,
		int color,
		float x0,
		float y0,
		float z0,
		float x1,
		float y1,
		float z1
	) {
		if (Math.abs(x1 - x0) < 0.05F) {
			vertex(pose, buffer, color, x0, y0, z0, 0.0F, 1.0F);
			vertex(pose, buffer, color, x0, y1, z0, 0.0F, 0.0F);
			vertex(pose, buffer, color, x0, y1, z1, 1.0F, 0.0F);
			vertex(pose, buffer, color, x0, y0, z1, 1.0F, 1.0F);
			return;
		}

		if (Math.abs(y1 - y0) < 0.05F) {
			vertex(pose, buffer, color, x0, y0, z0, 0.0F, 0.0F);
			vertex(pose, buffer, color, x1, y0, z0, 1.0F, 0.0F);
			vertex(pose, buffer, color, x1, y0, z1, 1.0F, 1.0F);
			vertex(pose, buffer, color, x0, y0, z1, 0.0F, 1.0F);
			return;
		}

		vertex(pose, buffer, color, x0, y0, z0, 0.0F, 1.0F);
		vertex(pose, buffer, color, x0, y1, z0, 0.0F, 0.0F);
		vertex(pose, buffer, color, x1, y1, z0, 1.0F, 0.0F);
		vertex(pose, buffer, color, x1, y0, z0, 1.0F, 1.0F);
	}

	private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, int color, float x, float y, float z, float u, float v) {
		buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public boolean shouldRenderOffScreen() {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 96;
	}
}

package com.jamesstevenson.brmc.client;

import com.jamesstevenson.brmc.block.ThresholdBlockEntity;
import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.gate.PortalLod;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import org.jspecify.annotations.Nullable;

/**
 * Per-portal LOD see-through of the linked dest volume. FULL draws dest-sampled
 * voxels. MESH is a dest-climate room. IMPOSTOR is a tinted plane. Vestibule
 * far side can chromatic / heat-haze. Commons can lie about walls.
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
		state.previewLies = blockEntity.previewLies();
		state.farSideDistorts = blockEntity.farSideDistorts();
		state.destVolume = blockEntity.destVolume();
		state.destReady = state.destVolume.isReady();
		state.anim = (blockEntity.hasLevel() ? blockEntity.getLevel().getGameTime() : 0L) + partialTicks;
		state.cameraDistance = (float) cameraPosition.distanceTo(Vec3.atCenterOf(blockEntity.getBlockPos()));
		state.draw = blockEntity.isPortalAnchor() && state.kind.hasLinkedVolumeThisPass();
		state.lod = state.draw
			? LinkedVolumeClient.budget().allocate(state.cameraDistance, state.destReady)
			: PortalLod.IMPOSTOR;
	}

	@Override
	public void submit(LinkedVolumeRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		if (!state.draw || state.kind != GateKind.VESTIBULE && state.kind != GateKind.COMMONS) {
			return;
		}

		boolean red = state.crossing == PresentationLock.Crossing.YELLOW_TO_RED;
		int plane = red ? ARGB.color(110, 138, 32, 32) : ARGB.color(90, 201, 180, 88);
		int volume = red ? ARGB.color(230, 138, 32, 32) : ARGB.color(220, 201, 180, 88);
		var type = RenderTypes.entityTranslucent(WOOL);

		poseStack.pushPose();
		collector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
			drawPlane(pose, buffer, plane, state);
			if (state.lod == PortalLod.FULL && state.destReady) {
				drawDestVoxels(pose, buffer, state);
			} else if (state.lod != PortalLod.IMPOSTOR) {
				drawClimateRoom(pose, buffer, volume, state);
			}

			if (state.previewLies && state.lod != PortalLod.IMPOSTOR) {
				drawLieWalls(pose, buffer, state);
			}

			if (state.farSideDistorts && state.lod != PortalLod.IMPOSTOR) {
				drawHaze(pose, buffer, state);
			}
		});
		poseStack.popPose();
	}

	private static void drawPlane(PoseStack.Pose pose, VertexConsumer buffer, int color, LinkedVolumeRenderState state) {
		float wobble = state.farSideDistorts ? haze(state.anim, 0.0F) : 0.0F;
		quad(pose, buffer, color, 0.98F + wobble, 0.0F, 0.0F, 0.98F + wobble, 3.0F, 1.0F);
	}

	private static void drawClimateRoom(PoseStack.Pose pose, VertexConsumer buffer, int volume, LinkedVolumeRenderState state) {
		float depth = state.lod == PortalLod.MESH ? DEPTH : 3.0F;
		quad(pose, buffer, volume, 0.98F, 0.0F, 0.0F, 0.98F + depth, 0.02F, 1.0F);
		quad(pose, buffer, volume, 0.98F, 2.98F, 0.0F, 0.98F + depth, 3.0F, 1.0F);
		quad(pose, buffer, volume, 0.98F, 0.0F, 0.0F, 0.98F + depth, 3.0F, 0.02F);
		quad(pose, buffer, volume, 0.98F, 0.0F, 0.98F, 0.98F + depth, 3.0F, 1.0F);
	}

	private static void drawDestVoxels(PoseStack.Pose pose, VertexConsumer buffer, LinkedVolumeRenderState state) {
		state.destVolume.forEach((dx, dy, dz, blockState) -> {
			int color = voxelColor(blockState);
			if (ARGB.alpha(color) == 0) {
				return;
			}

			cube(pose, buffer, color, dx, dy, dz, dx + 1.0F, dy + 1.0F, dz + 1.0F);
		});
	}

	private static void drawLieWalls(PoseStack.Pose pose, VertexConsumer buffer, LinkedVolumeRenderState state) {
		int light = ARGB.color(210, 201, 180, 88);
		int dark = ARGB.color(210, 176, 140, 48);
		for (int i = 1; i <= 6; i++) {
			int color = (i & 1) == 0 ? dark : light;
			quad(pose, buffer, color, i, 0.0F, -0.02F, i + 1.0F, 3.0F, 0.0F);
			quad(pose, buffer, color, i, 0.0F, 1.0F, i + 1.0F, 3.0F, 1.02F);
		}

		quad(pose, buffer, light, 6.98F, 0.0F, 0.0F, 7.0F, 3.0F, 1.0F);
	}

	private static void drawHaze(PoseStack.Pose pose, VertexConsumer buffer, LinkedVolumeRenderState state) {
		float shift = 0.04F + Math.abs(haze(state.anim, 1.7F)) * 0.06F;
		int red = ARGB.color(50, 220, 40, 40);
		int cyan = ARGB.color(50, 40, 80, 200);
		quad(pose, buffer, red, 1.2F + shift, 0.1F, 0.05F, 1.2F + shift, 2.9F, 0.95F);
		quad(pose, buffer, cyan, 1.2F - shift, 0.1F, 0.05F, 1.2F - shift, 2.9F, 0.95F);
		float far = 4.5F + haze(state.anim, 3.1F) * 0.2F;
		quad(pose, buffer, ARGB.color(40, 160, 30, 30), far, 0.2F, 0.1F, far + 0.4F, 2.8F, 0.9F);
	}

	private static float haze(float anim, float seed) {
		return (float) Math.sin(anim * 0.17 + seed) * 0.08F;
	}

	private static int voxelColor(BlockState state) {
		if (state.isAir()) {
			return 0;
		}

		if (state.getBlock() == Blocks.WOOL.pick(DyeColor.RED) || state.getBlock() == Blocks.CARPET.pick(DyeColor.RED)) {
			return ARGB.color(230, 138, 32, 32);
		}

		if (state.getBlock() == Blocks.WOOL.pick(DyeColor.YELLOW) || state.getBlock() == Blocks.CARPET.pick(DyeColor.YELLOW)) {
			return ARGB.color(220, 201, 180, 88);
		}

		if (state.getBlock() == Blocks.DEEPSLATE || state.getBlock() == Blocks.SMOOTH_STONE) {
			return ARGB.color(235, 42, 42, 46);
		}

		return ARGB.color(200, 160, 150, 130);
	}

	private static void cube(PoseStack.Pose pose, VertexConsumer buffer, int color, float x0, float y0, float z0, float x1, float y1, float z1) {
		quad(pose, buffer, color, x0, y0, z0, x1, y0, z1);
		quad(pose, buffer, color, x0, y1, z0, x1, y1, z1);
		quad(pose, buffer, color, x0, y0, z0, x0, y1, z1);
		quad(pose, buffer, color, x1, y0, z0, x1, y1, z1);
		quad(pose, buffer, color, x0, y0, z0, x1, y1, z0);
		quad(pose, buffer, color, x0, y0, z1, x1, y1, z1);
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

package com.jamesstevenson.brmc.client;

import com.jamesstevenson.brmc.block.BrmcBlocks;
import com.jamesstevenson.brmc.block.ThresholdBlockEntity;
import com.jamesstevenson.brmc.gate.PortalLod;
import com.jamesstevenson.brmc.gate.PresentationLock;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import org.jspecify.annotations.Nullable;

/**
 * Per-portal LOD see-through of the linked dest volume. FULL draws dest-sampled
 * voxels. MESH is a dest-climate room. IMPOSTOR is a tinted plane. Oriented to
 * the gate facing (including DOWN for false floor).
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
		state.facing = blockEntity.facing();
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
		if (!state.draw) {
			return;
		}

		int plane = planeColor(state.crossing);
		int volume = volumeColor(state.crossing);
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

	private static int planeColor(PresentationLock.Crossing crossing) {
		return switch (crossing) {
			case YELLOW_TO_RED -> ARGB.color(110, 138, 32, 32);
			case CONTINUOUS_PLANT -> ARGB.color(90, 90, 100, 96);
			case DROP -> ARGB.color(80, 36, 36, 40);
			case RARE_ENCOUNTER -> ARGB.color(90, 180, 180, 176);
			default -> ARGB.color(90, 201, 180, 88);
		};
	}

	private static int volumeColor(PresentationLock.Crossing crossing) {
		return switch (crossing) {
			case YELLOW_TO_RED -> ARGB.color(230, 138, 32, 32);
			case CONTINUOUS_PLANT -> ARGB.color(220, 96, 108, 104);
			case DROP -> ARGB.color(220, 48, 48, 52);
			case RARE_ENCOUNTER -> ARGB.color(220, 188, 188, 184);
			default -> ARGB.color(220, 201, 180, 88);
		};
	}

	private static void drawPlane(PoseStack.Pose pose, VertexConsumer buffer, int color, LinkedVolumeRenderState state) {
		float wobble = state.farSideDistorts ? haze(state.anim, 0.0F) : 0.0F;
		face(pose, buffer, color, state.facing, 0.0F, wobble);
	}

	private static void drawClimateRoom(PoseStack.Pose pose, VertexConsumer buffer, int volume, LinkedVolumeRenderState state) {
		float depth = state.lod == PortalLod.MESH ? DEPTH : 3.0F;
		Direction facing = state.facing;
		if (facing.getAxis() == Direction.Axis.Y) {
			quad(pose, buffer, volume, 0.0F, 0.02F, 0.0F, 1.0F, 0.02F - depth, 1.0F);
			return;
		}

		int sx = facing.getStepX();
		int sz = facing.getStepZ();
		float start = sx + sz > 0 ? 0.98F : 0.02F;
		if (facing.getAxis() == Direction.Axis.X) {
			float x1 = start + sx * depth;
			quad(pose, buffer, volume, start, 0.0F, 0.0F, x1, 0.02F, 1.0F);
			quad(pose, buffer, volume, start, 2.98F, 0.0F, x1, 3.0F, 1.0F);
			quad(pose, buffer, volume, start, 0.0F, 0.0F, x1, 3.0F, 0.02F);
			quad(pose, buffer, volume, start, 0.0F, 0.98F, x1, 3.0F, 1.0F);
			return;
		}

		float z1 = start + sz * depth;
		quad(pose, buffer, volume, 0.0F, 0.0F, start, 1.0F, 0.02F, z1);
		quad(pose, buffer, volume, 0.0F, 2.98F, start, 1.0F, 3.0F, z1);
		quad(pose, buffer, volume, 0.0F, 0.0F, start, 0.02F, 3.0F, z1);
		quad(pose, buffer, volume, 0.98F, 0.0F, start, 1.0F, 3.0F, z1);
	}

	private static void face(PoseStack.Pose pose, VertexConsumer buffer, int color, Direction facing, float along, float wobble) {
		if (facing == Direction.DOWN) {
			quad(pose, buffer, color, 0.0F, 0.02F + wobble, 0.0F, 1.0F, 0.02F + wobble, 1.0F);
			return;
		}

		if (facing.getAxis() == Direction.Axis.X) {
			float x = facing.getStepX() > 0 ? 0.98F + along + wobble : 0.02F - along - wobble;
			quad(pose, buffer, color, x, 0.0F, 0.0F, x, 3.0F, 1.0F);
			return;
		}

		float z = facing.getStepZ() > 0 ? 0.98F + along + wobble : 0.02F - along - wobble;
		quad(pose, buffer, color, 0.0F, 0.0F, z, 1.0F, 3.0F, z);
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
		face(pose, buffer, red, state.facing, 0.22F + shift, 0.0F);
		face(pose, buffer, cyan, state.facing, 0.22F - shift, 0.0F);
		float far = 3.5F + haze(state.anim, 3.1F) * 0.2F;
		face(pose, buffer, ARGB.color(40, 160, 30, 30), state.facing, far, 0.0F);
	}

	private static float haze(float anim, float seed) {
		return (float) Math.sin(anim * 0.17 + seed) * 0.08F;
	}

	private static int voxelColor(BlockState state) {
		if (state.isAir()) {
			return 0;
		}

		var block = state.getBlock();
		if (block == BrmcBlocks.SECOND_WALLPAPER
			|| block == BrmcBlocks.SECOND_WALLPAPER_B
			|| block == BrmcBlocks.SECOND_WALLPAPER_C
			|| block == BrmcBlocks.SECOND_CARPET
			|| block == BrmcBlocks.SECOND_CARPET_MOLD
			|| block == BrmcBlocks.SECOND_CARPET_TORN
			|| block == BrmcBlocks.SECOND_DEBRIS_CARPET
			|| block == BrmcBlocks.SECOND_CEILING_TILE
			|| block == BrmcBlocks.SECOND_TROFFER
			|| block == BrmcBlocks.SECOND_TROFFER_DEAD
			|| block == BrmcBlocks.SECOND_DOOR_COMMERCIAL
			|| block == BrmcBlocks.SECOND_DOOR_FRAME
			|| block == Blocks.WOOL.pick(DyeColor.RED)
			|| block == Blocks.CARPET.pick(DyeColor.RED)
			|| block == Blocks.DYED_TERRACOTTA.pick(DyeColor.RED)) {
			return ARGB.color(230, 138, 32, 32);
		}

		if (block == Blocks.WOOL.pick(DyeColor.YELLOW) || block == Blocks.CARPET.pick(DyeColor.YELLOW)
			|| block == Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW)) {
			return ARGB.color(220, 201, 180, 88);
		}

		if (block == Blocks.WOOL.pick(DyeColor.LIME) || block == Blocks.CARPET.pick(DyeColor.LIME)) {
			return ARGB.color(210, 160, 200, 80);
		}

		if (block == Blocks.WOOL.pick(DyeColor.GRAY) || block == Blocks.CARPET.pick(DyeColor.GRAY)) {
			return ARGB.color(220, 72, 72, 76);
		}

		if (block == Blocks.WOOL.pick(DyeColor.LIGHT_GRAY) || block == Blocks.CARPET.pick(DyeColor.WHITE)) {
			return ARGB.color(220, 196, 196, 192);
		}

		if (block == Blocks.IRON_BLOCK) {
			return ARGB.color(230, 150, 150, 156);
		}

		if (block == Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.OXIDIZED)
			|| block == Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED)) {
			return ARGB.color(230, 72, 140, 96);
		}

		if (block == Blocks.OCHRE_FROGLIGHT) {
			return ARGB.color(240, 230, 210, 120);
		}

		if (block == Blocks.DEEPSLATE || block == Blocks.SMOOTH_STONE) {
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

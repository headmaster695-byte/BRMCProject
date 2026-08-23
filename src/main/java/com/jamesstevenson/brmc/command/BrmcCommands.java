package com.jamesstevenson.brmc.command;

import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.spawn.ClarkColdOpen;
import com.jamesstevenson.brmc.worldgen.FirstPocket;
import com.jamesstevenson.brmc.worldgen.YellowMonoLayout;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class BrmcCommands {
	private BrmcCommands() {
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register(BrmcCommands::register);
	}

	private static void register(
		CommandDispatcher<CommandSourceStack> dispatcher,
		CommandBuildContext buildContext,
		Commands.CommandSelection selection
	) {
		dispatcher.register(
			Commands.literal("brmc")
				.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
				.then(Commands.literal("first").executes(ctx -> enter(ctx, BrmcDimensions.FIRST)))
				.then(Commands.literal("second").executes(ctx -> enter(ctx, BrmcDimensions.SECOND)))
				.then(Commands.literal("third").executes(ctx -> enter(ctx, BrmcDimensions.THIRD)))
				.then(Commands.literal("false_first").executes(ctx -> enter(ctx, BrmcDimensions.FALSE_FIRST)))
				.then(Commands.literal("pocket")
					.then(Commands.literal("clark").executes(ctx -> pocket(ctx, FirstPocket.CLARK_CHAMBER)))
					.then(Commands.literal("apartment").executes(ctx -> pocket(ctx, FirstPocket.APARTMENT)))
					.then(Commands.literal("janitor").executes(ctx -> pocket(ctx, FirstPocket.APARTMENT_JANITOR)))
					.then(Commands.literal("utilities").executes(ctx -> pocket(ctx, FirstPocket.UTILITIES)))
					.then(Commands.literal("commons").executes(ctx -> pocket(ctx, FirstPocket.COMMON_EXIT)))
					.then(Commands.literal("vestibule").executes(ctx -> pocket(ctx, FirstPocket.VESTIBULE)))
					.then(Commands.literal("curving").executes(ctx -> pocket(ctx, FirstPocket.CURVING_HALL)))
					.then(Commands.literal("false_floor").executes(ctx -> pocket(ctx, FirstPocket.FALSE_FLOOR)))
					.then(Commands.literal("dead_zone").executes(ctx -> pocket(ctx, FirstPocket.FLUORESCENT_DEAD_ZONE)))
				)
		);
	}

	private static int pocket(CommandContext<CommandSourceStack> context, FirstPocket pocket) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		ServerLevel first = player.level().getServer().getLevel(BrmcDimensions.FIRST);
		if (first == null) {
			context.getSource().sendFailure(Component.literal(BrmcDimensions.FIRST.identifier().toString()));
			return 0;
		}

		Vec3 pos = pocket == FirstPocket.APARTMENT_JANITOR
			? new Vec3(YellowMonoLayout.cellOrigin(-5) + 2.5, YellowMonoLayout.CARPET_Y + 1, YellowMonoLayout.cellOrigin(2) + 5.5)
			: YellowMonoLayout.warp(pocket);
		player.teleport(new TeleportTransition(
			first,
			pos,
			Vec3.ZERO,
			player.getYRot(),
			player.getXRot(),
			TeleportTransition.DO_NOTHING
		));
		player.setAttached(ClarkColdOpen.ARRIVED, true);
		return Command.SINGLE_SUCCESS;
	}

	private static int enter(CommandContext<CommandSourceStack> context, ResourceKey<Level> dimension) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		ServerLevel destination = player.level().getServer().getLevel(dimension);
		if (destination == null) {
			context.getSource().sendFailure(Component.literal(dimension.identifier().toString()));
			return 0;
		}

		Vec3 pos = dimension == BrmcDimensions.FIRST
			? new Vec3(YellowMonoLayout.SPAWN_X + 0.5, YellowMonoLayout.CARPET_Y + 1, YellowMonoLayout.SPAWN_Z + 0.5)
			: Vec3.atBottomCenterOf(new BlockPos(
				YellowMonoLayout.SPAWN_X,
				YellowMonoLayout.CARPET_Y + 1,
				YellowMonoLayout.SPAWN_Z
			));
		float yRot = dimension == BrmcDimensions.FIRST ? YellowMonoLayout.SPAWN_Y_ROT : player.getYRot();
		player.teleport(new TeleportTransition(
			destination,
			pos,
			Vec3.ZERO,
			yRot,
			dimension == BrmcDimensions.FIRST ? 0.0F : player.getXRot(),
			TeleportTransition.DO_NOTHING
		));
		if (dimension == BrmcDimensions.FIRST) {
			player.setAttached(ClarkColdOpen.ARRIVED, true);
		}

		return Command.SINGLE_SUCCESS;
	}
}

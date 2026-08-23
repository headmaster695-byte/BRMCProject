package com.jamesstevenson.brmc.gate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jamesstevenson.brmc.block.BrmcBlocks;
import com.jamesstevenson.brmc.dimension.BrmcDimensions;
import com.jamesstevenson.brmc.worldgen.DestClimate;
import com.jamesstevenson.brmc.worldgen.YellowMonoLayout;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jspecify.annotations.Nullable;

/**
 * The five live First exits and the dest-side return planes that match them.
 *
 * <p>Outbound and walk-back share this list. First worldgen and dest climate
 * rooms both stamp the same identity coordinates so the return plane is as
 * hard to miss as the outbound — not an invisible air pocket at the same XYZ.
 */
public final class LinkedOpenings {
	public record Opening(
		GateKind kind,
		ResourceKey<Level> destination,
		DestClimate destClimate,
		Direction firstOutboundFacing,
		List<BlockPos> identities
	) {
		public Direction destReturnFacing() {
			return this.firstOutboundFacing.getOpposite();
		}

		public List<SeamlessGate> firstOutboundGates() {
			List<SeamlessGate> gates = new ArrayList<>(this.identities.size());
			for (BlockPos identity : this.identities) {
				gates.add(SeamlessGateService.gate(this.kind, identity, this.firstOutboundFacing));
			}

			return List.copyOf(gates);
		}

		public List<SeamlessGate> destReturnGates() {
			List<SeamlessGate> gates = new ArrayList<>(this.identities.size());
			for (BlockPos identity : this.identities) {
				gates.add(new SeamlessGate(
					this.kind,
					this.destination,
					BrmcDimensions.FIRST,
					identity,
					this.destReturnFacing()
				));
			}

			return List.copyOf(gates);
		}

		public boolean hasIdentity(int worldX, int worldZ) {
			for (BlockPos identity : this.identities) {
				if (identity.getX() == worldX && identity.getZ() == worldZ) {
					return true;
				}
			}

			return false;
		}
	}

	public static final List<Opening> ALL = List.of(
		new Opening(
			GateKind.COMMONS,
			BrmcDimensions.FALSE_FIRST,
			DestClimate.YELLOW_MONO,
			Direction.EAST,
			List.of(new BlockPos(23, YellowMonoLayout.CARPET_Y, 52))
		),
		new Opening(
			GateKind.VESTIBULE,
			BrmcDimensions.SECOND,
			DestClimate.RED_MONO,
			Direction.EAST,
			List.of(
				new BlockPos(70, YellowMonoLayout.CARPET_Y, 19),
				new BlockPos(70, YellowMonoLayout.CARPET_Y, 20)
			)
		),
		new Opening(
			GateKind.UTILITIES,
			BrmcDimensions.BUTTONS,
			DestClimate.PLANT,
			Direction.NORTH,
			List.of(
				new BlockPos(19, YellowMonoLayout.CARPET_Y, -40),
				new BlockPos(20, YellowMonoLayout.CARPET_Y, -40)
			)
		),
		new Opening(
			GateKind.CURVING_HALL,
			BrmcDimensions.SECOND_FALSE_FIRST,
			DestClimate.SOFT_YELLOW,
			Direction.NORTH,
			List.of(new BlockPos(62, YellowMonoLayout.CARPET_Y, 62))
		),
		new Opening(
			GateKind.CUSTODIAL,
			BrmcDimensions.CUSTODIAL,
			DestClimate.CUSTODIAL,
			Direction.WEST,
			List.of(new BlockPos(-39, YellowMonoLayout.CARPET_Y, 22))
		)
	);

	private LinkedOpenings() {
	}

	public static Optional<Opening> byKind(GateKind kind) {
		for (Opening opening : ALL) {
			if (opening.kind() == kind) {
				return Optional.of(opening);
			}
		}

		return Optional.empty();
	}

	public static Optional<Opening> forDestClimate(DestClimate climate) {
		for (Opening opening : ALL) {
			if (opening.destClimate() == climate) {
				return Optional.of(opening);
			}
		}

		return Optional.empty();
	}

	public static Optional<Opening> forDestination(ResourceKey<Level> destination) {
		for (Opening opening : ALL) {
			if (opening.destination().equals(destination)) {
				return Optional.of(opening);
			}
		}

		return Optional.empty();
	}

	public static List<SeamlessGate> firstOutboundGates() {
		List<SeamlessGate> gates = new ArrayList<>();
		for (Opening opening : ALL) {
			gates.addAll(opening.firstOutboundGates());
		}

		return List.copyOf(gates);
	}

	public static List<SeamlessGate> destReturnGates() {
		List<SeamlessGate> gates = new ArrayList<>();
		for (Opening opening : ALL) {
			gates.addAll(opening.destReturnGates());
		}

		return List.copyOf(gates);
	}

	public static boolean isDestReturnColumn(DestClimate climate, int worldX, int worldZ) {
		return forDestClimate(climate).filter(opening -> opening.hasIdentity(worldX, worldZ)).isPresent();
	}

	public static @Nullable Block destReturnBlock(DestClimate climate) {
		return forDestClimate(climate).map(opening -> BrmcBlocks.blockFor(opening.kind())).orElse(null);
	}
}

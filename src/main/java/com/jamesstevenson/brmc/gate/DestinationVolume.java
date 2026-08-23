package com.jamesstevenson.brmc.gate;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * Packed dest-dimension voxels at identity coordinates, sampled from the
 * dest {@code ServerLevel}. Client draws these through the portal plane at
 * {@link PortalLod#FULL}. This is not a live dest {@code ClientLevel}.
 */
public final class DestinationVolume {
	public static final DestinationVolume EMPTY = new DestinationVolume("", new int[0], false);

	private final String dimensionId;
	private final int[] packed;
	private final boolean complete;

	public DestinationVolume(String dimensionId, int[] packed, boolean complete) {
		this.dimensionId = dimensionId == null ? "" : dimensionId;
		this.packed = packed == null ? new int[0] : packed;
		this.complete = complete && this.packed.length >= 4;
	}

	public boolean isReady() {
		return this.complete && this.packed.length >= 4;
	}

	public boolean isEmpty() {
		return this.packed.length < 4;
	}

	public String dimensionId() {
		return this.dimensionId;
	}

	public int voxelCount() {
		return this.packed.length / 4;
	}

	public @Nullable ResourceKey<Level> dimension() {
		if (this.dimensionId.isEmpty()) {
			return null;
		}

		Identifier id = Identifier.tryParse(this.dimensionId);
		if (id == null) {
			return null;
		}

		return ResourceKey.create(Registries.DIMENSION, id);
	}

	public void forEach(VoxelConsumer consumer) {
		for (int i = 0; i + 3 < this.packed.length; i += 4) {
			consumer.accept(
				this.packed[i],
				this.packed[i + 1],
				this.packed[i + 2],
				Block.stateById(this.packed[i + 3])
			);
		}
	}

	public void write(ValueOutput output) {
		output.putString("dest_dim", this.dimensionId);
		output.putIntArray("dest_voxels", this.packed);
		output.putBoolean("dest_complete", this.complete);
	}

	public static DestinationVolume read(ValueInput input) {
		String dim = input.getStringOr("dest_dim", "");
		int[] packed = input.getIntArray("dest_voxels").orElseGet(() -> new int[0]);
		boolean complete = input.getBooleanOr("dest_complete", packed.length >= 4);
		return new DestinationVolume(dim, packed, complete);
	}

	@FunctionalInterface
	public interface VoxelConsumer {
		void accept(int dx, int dy, int dz, BlockState state);
	}
}

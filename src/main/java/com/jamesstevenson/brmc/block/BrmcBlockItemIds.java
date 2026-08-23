package com.jamesstevenson.brmc.block;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public final class BrmcBlockItemIds {
	public static final BlockItemId VESTIBULE_THRESHOLD = create("vestibule_threshold");
	public static final BlockItemId COMMONS_THRESHOLD = create("commons_threshold");
	public static final BlockItemId UTILITIES_THRESHOLD = create("utilities_threshold");
	public static final BlockItemId CURVING_HALL_THRESHOLD = create("curving_hall_threshold");
	public static final BlockItemId FALSE_FLOOR_THRESHOLD = create("false_floor_threshold");
	public static final BlockItemId OOB_HOLE = create("oob_hole");
	public static final BlockItemId CUSTODIAL_THRESHOLD = create("custodial_threshold");

	private BrmcBlockItemIds() {
	}

	private static BlockItemId create(String name) {
		Identifier id = BrmcMod.id(name);
		return BlockItemId.create(id, id);
	}
}

package com.jamesstevenson.brmc.block;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public final class BrmcBlockItemIds {
	public static final BlockItemId VESTIBULE_THRESHOLD = create("vestibule_threshold");
	public static final BlockItemId COMMONS_THRESHOLD = create("commons_threshold");

	private BrmcBlockItemIds() {
	}

	private static BlockItemId create(String name) {
		Identifier id = BrmcMod.id(name);
		return BlockItemId.create(id, id);
	}
}

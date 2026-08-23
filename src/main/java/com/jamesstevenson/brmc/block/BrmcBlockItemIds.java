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

	public static final BlockItemId FIRST_WALLPAPER = create("first_wallpaper");
	public static final BlockItemId FIRST_WALLPAPER_SEAM = create("first_wallpaper_seam");
	public static final BlockItemId FIRST_WALLPAPER_PEEL = create("first_wallpaper_peel");
	public static final BlockItemId FIRST_WALLPAPER_DEAD = create("first_wallpaper_dead");
	public static final BlockItemId FIRST_CARPET = create("first_carpet");
	public static final BlockItemId FIRST_CARPET_TORN = create("first_carpet_torn");
	public static final BlockItemId FIRST_DEBRIS_CARPET = create("first_debris_carpet");
	public static final BlockItemId FIRST_CARPET_STAINED = create("first_carpet_stained");
	public static final BlockItemId FIRST_CARPET_DRY = create("first_carpet_dry");
	public static final BlockItemId FIRST_CEILING_TILE = create("first_ceiling_tile");
	public static final BlockItemId FIRST_TROFFER = create("first_troffer");
	public static final BlockItemId FIRST_TROFFER_DEAD = create("first_troffer_dead");
	public static final BlockItemId FIRST_TROFFER_HALF = create("first_troffer_half");
	public static final BlockItemId FIRST_DOOR_COMMERCIAL = create("first_door_commercial");
	public static final BlockItemId FIRST_DOOR_FRAME = create("first_door_frame");
	public static final BlockItemId FIRST_DOOR_VESTIBULE = create("first_door_vestibule");

	private BrmcBlockItemIds() {
	}

	private static BlockItemId create(String name) {
		Identifier id = BrmcMod.id(name);
		return BlockItemId.create(id, id);
	}
}

package com.jamesstevenson.brmc.worldgen;

import com.jamesstevenson.brmc.BrmcMod;

import net.minecraft.resources.Identifier;

/**
 * Authored pocket structure hooks. Geometry is stubbed in {@link YellowMonoLayout};
 * jigsaws and aesthetic boards land later under {@code floors/<slug>/}.
 */
public enum PocketStructure {
	CLARK_CHAMBER("first-clark-chamber", "Authored 32×32 cold-open. Chevron family, beige loop-pile carpet, troffer grid, empty segmented volume into the labyrinth. No store/basement/portal/tutorial."),
	APARTMENT_LOBE("first-apartment-pocket", "Habitation lobe. Drywall / bed / kitchen material break. Not Bounded."),
	JANITOR_CLOSET("first-apartment-pocket", "Rare janitor hook inside the habitation lobe. Leads to Custodial, never Bounded."),
	UTILITIES_MOUTH("first-utilities", "Infrastructure mouth. Ozone/contactor plant continues through the deep door into Buttons."),
	COMMONS_LITERACY("first-common-exit", "Literacy teacher. Single yellow→yellow threshold. No airlock, red, or OOB hole."),
	VESTIBULE_TRUE_EXIT("first-vestibule", "Paired office doors: door 1 → yellow airlock → door 2 → Second. Yellow→red First-side frame is a documented miss."),
	VESTIBULE_OOB_HOLE("first-vestibule", "Floor hole before door 2. Stateful, late. Puzzle not built."),
	CURVING_HALL_SEAM("first-curving-hall", "Invisible seam. Yellow continues around a soft plan into Second False First."),
	FALSE_FLOOR_DROP("first-false-floor", "Fall/coil drop. Architecture only. One rim nick; pit is yellow-mono debris, not gray. Not a landmark."),
	FLUORESCENT_DEAD_RUN("first-fluorescent-dead-zone", "Anti-noise-soup pacing run. Troffers off.");

	private final String slug;
	private final String hook;

	PocketStructure(String slug, String hook) {
		this.slug = slug;
		this.hook = hook;
	}

	public String slug() {
		return this.slug;
	}

	public String hook() {
		return this.hook;
	}

	/** Reserved structure id. No datapack structure is registered in this pass. */
	public Identifier structureId() {
		return BrmcMod.id("pockets/" + this.slug);
	}

	/** Worldgen/art boards land later. Do not invent them here. */
	public String aestheticBoardDir() {
		return "floors/" + this.slug + "/";
	}
}

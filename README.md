# BRMC

James Stevenson’s Backrooms / liminal-spaces Fabric mod. This pass is a **playable First Dimension vertical slice**: authored Clark cold-open, infinite yellow-mono maze with learnable pocket spines, and First gameplay rules.

Minecraft **26.2**, Fabric Loader **0.19.3**, Fabric API **0.158.0+26.2**, Java **25**.

## What is in this pass

- First Dimension registered as `brmc:first` with a `brmc:yellow_mono` chunk generator.
- Clark cold-open is a **32×32** authored chamber (post-threshold yellow room only). Fidelity target: Clark’s first Backrooms room from A24 *Backrooms* (2026). Diagonal chevron wallpaper (Y-stepped two-yellow family + A/B/C seam phases), beige loop-pile `first_carpet` (blotch *read* is texture-only — not a name), P0 troffer block-entity bars, 2×2 columns, empty segmented volume, four **2-wide** cardinal openings at 19–20. Maze spines keep those 2-wide doors in the wall so hallways recede; pocket interiors stay open rooms. East stays a yellow dogleg — not a vestibule runway. No store, basement, inbound portal, or tutorial UI. Spawn faces east. **Voxel honesty:** P0 `brmc:first_*` blocks are wired; TEMP 16² textures follow the Architect UV-hex pack (not printed EXIT, not a wet macro). True damp carpet is a documented miss. **Regen First chunks** so the new atlas shows.
- Cardinal spines make authored pockets learnable (not noise soup):
  - East → `first-vestibule` (true exit)
  - South → `first-common-exit` (literacy teacher)
  - West → `first-apartment-pocket` (habitation lobe, not Bounded)
  - North → `first-utilities` (infrastructure mouth)
  - SE L → `first-curving-hall`
  - SW → `first-false-floor`
  - NW → `first-fluorescent-dead-zone`
- Pocket details:
  - Apartment — drywall / bed / kitchen. Rare janitor closet → Custodial.
  - Utilities — ozone / contactor plant continues through the deep door → Buttons.
  - Commons — single yellow→yellow threshold → False First. No airlock, red, or OOB hole.
  - Vestibule — paired office doors: door 1 → yellow airlock → door 2 → Second. First-side jambs stay yellow-mono. Dest sample is red; a painted yellow→red frame is a documented miss. Floor hole before door 2 is OOB (present, **not live**; Lost Island / SCRAM / soul not built). Look-down is `first_debris_carpet`, not a gray void.
  - Curving hall — invisible seam, yellow around a soft plan → Second False First.
  - False floor — architecture-only drop (no Spiral swap). Carpet around the hole stays field — no rim nick teaching the pit. Looking down the pit is yellow-mono debris (wool / terracotta), not gray stone. Not an L, not a landmark.
- Destinations other than First are **thick stubs** (`brmc:dest_climate` cells): False First yellow-mono room, Second red-mono room, Buttons plant, Second False First soft yellow, Spiral well, Custodial closet. Still not full floors. Digital is sealed. Fourth does not exist.
- No entities. Distant unresolved cave-mood sounds foreshadow only.
- First rules: generated fabric regenerates (floor/ceiling faster so you cannot dig out of the layer); player-built / pillared blocks persist on the chunk; maps freeze; compass spins; F3 coordinates lie.
- First P0 block palette is registered (`floors/first-dimension/block-palette.md`). Clark / maze use `brmc:first_*` wallpaper, carpet, ceiling, troffer, and door blocks. TEMP 16² atlas is the Architect UV-hex pack (`first_wallpaper_b` / `_c` are seam phases). Creative tab is muted `Building` and lists primaries only. Item names are mundane (Wallpaper / Carpet / Light / Door). Troffer is a block entity for a later hum-buzz. Wear variants are sparse noise, not landmarks. Apartment / utilities still vanilla material-break. No First entities.
- Aesthetic boards land later under `floors/<slug>/`. Four wiki heroes stay unchanged this pass (oshkosh / troffer / common-exit / dead-zone) — no restyle, no invented board art.

### Voxel look-targets (Architect, not AI)

- **Hallway vanishing point** — Clark and maze doors share a 2-wide local 3–4 / world 19–20 grammar so corridors recede. Spine/hash links are doors in walls, not missing walls. East dogleg is unchanged.
- **Carpet texture close** — beige loop-pile nap TEMP on `first_carpet`. Blotch moist *read* is texture-only; ids and hover stay “Carpet”. Not a wet macro.
- **Double-door vestibule grammar** — paired leaves at `(56,19–20)` and `(70,19–20)`. TEMP doors read as glass airlock cousins (metal frame + glass), not yellow framing red. Dest sample at door 2 is still red Second.

Documented misses that stay misses: **true damp carpet** and **yellow→red vestibule frame**.

## Seamless gates (26.2, Immersive Portals class — not a door overlay)

The mod stays on 26.2. Immersive Portals is not downported. The player-facing path is **`LinkedVolumeBackend`**: a linked-dimension portal with per-portal LOD, dest-volume sampling, and a plane-cross identity swap. Hop is never the default.

**Must:** walk into another dimension through the plane as one camera/player motion. Not fade, not hop, not “see-through wallpaper that then teleports.”

### What this pass proves (all live First exits)

- **Architecture:** `LinkedDimensionPortal` + `PortalLod` (`FULL` / `MESH` / `IMPOSTOR`) + `PortalRenderBudget` (near portals spend FULL slots; farther / many open portals degrade). Not one forever-fullscreen blit.
- **See-through:** `DestinationVolumeSampler` reads the dest `ServerLevel` at identity coordinates (wider box, including DOWN wells) and syncs voxels to the threshold. Near portal = dest voxels. Mid = dest-climate room mesh oriented to facing. Far = tinted plane.
- **Walk-through:** `PortalCrossTracker` fires only on was-behind → now-through (horizontal or down). Dest chunks are held with `TicketType.PORTAL` before the identity-pose swap (`TeleportTransition.DO_NOTHING`). Loading swirl / “Downloading terrain” / “Entering X” are held off while a crossing is flagged.
- **Wired exits (with mercy return both ways):** commons → False First, vestibule door 2 → Second, utilities → Buttons, curving hall → Second False First, janitor closet → Custodial. Dest climate rooms stamp the same 3-high threshold at identity so the walk-back plane is as visible as the outbound. OOB hole and false-floor drop are architecture only — no live swap. `OutOfBoundsStub.holeLive()` is the OOB refuse switch.
- **Visual tells (no tutorials):**
  - Commons: clean yellow→yellow dest room. `previewLies()` can invent First wallpaper dest does not have.
  - Vestibule door 2: paired yellow-mono leaves; look-through dest room is red. Far side may chromatic-shift / heat-haze. First does not paint a red frame.
  - Utilities: plant continues (iron / copper).
  - Curving: yellow around a soft (lime) plan.
  - Custodial: light-gray closet.
  - East is not a marked compass: Clark→vestibule uses a yellow dogleg, then uncommon yellow. The airlock starts one cell later — same yellow language, not a cleaner runway.

### Honest gaps

True Immersive Portals see-through is a **second camera** into a live dest `ClientLevel` with stencil / portal clip and entity transfer. 26.2 BER (`submitCustomGeometry`) does not land that here. `FULL` is dest-sampled voxels, not a dual-world stencil. First still paints a short dest-climate backing alcove on commons / vestibule so IMPOSTOR / missing samples do not show void — vestibule backing stays yellow-mono (the yellow→red First-side frame is a miss). Carpet is dry vanilla display texture (true damp carpet is a miss). Dest interiors are tiled climate cells, not full floors. OOB / false-floor systems are not live. Janitor NPC encounter is not built. F3 does not print pocket or dimension slugs.

Fallback order: IP if present → linked volume → hop only if `brmc.devAllowHopGates=true` → `RefusingGateBackend` (last resort, loud error).

## Entering First

New players arrive in the Clark chamber facing east. Moderators: `/brmc first`. QA pocket warps: `/brmc pocket clark|apartment|janitor|utilities|commons|vestibule|curving|false_floor|dead_zone`.

Build: Java 25, then `./gradlew build`.

## Verify next (playtester)

- Wake in a large empty yellow room: diagonal chevron wallpaper, beige loop-pile carpet (TEMP), troffer bars, 2×2 columns, 2-wide openings on four sides (19–20). No tutorial text, store, or portal remnant. Vestibule airlock uses glass-door grammar + the same yellow-mono lights — not a brighter runway and not a yellow→red First block.
- Walk a cardinal: west/south/north hallways recede through 2-wide doors in yellow-mono walls. East dogleg then paired vestibule doors (yellow jambs; dest through door 2 is red). South commons stays yellow. West apartment is a white/oak break. North utilities is iron/copper plant.
- SE curve stays yellow. SW false-floor hole. NW stretch has no lights.
- Mine a wall: it comes back. Mine the floor: it comes back faster. Place / pillar blocks: they stay after regen and after relog.
- Maps do not chart First. Compass needle spins. F3 XYZ is wrong.
- Occasional distant wrong sound; nothing arrives.
- Commons door: look through — yellow dest **room**. Walk *through the plane*: same camera into False First. Walls through the door may be a lie.
- Vestibule door 2: look through — red dest room, possible haze. Walk through the plane into Second.
- Utilities deep door: look through — iron/copper plant. Walk north through the plane into Buttons.
- Curving seam: look through — yellow + lime plan. Walk north through the plane into Second False First.
- False-floor hole and vestibule OOB hole are architecture only — they do not dimension-cross. False-floor carpet around the hole stays ordinary field; the pit is the same yellow-mono family, not gray subfloor. Not a stair, letter, or marked exit; step back onto the floor.
- `/brmc pocket janitor` (moderators only): walk west through the closet into Custodial; walk back east to First. No janitor NPC.
- Dest stubs: regen dest chunks so return doors are visible. Mercy still fires in air at the identities if the old chunk has no block. Walk back through the matching dest-side threshold at the same XYZ. Do not use `/brmc second` (or other off-identity dest warps) to verify returns. No F3 pocket / dest / dim slugs (`brmc:first`) on public walks. Creative hover says Threshold / Opening, not vestibule_threshold / oob_hole.
- Hop stays off unless `brmc.devAllowHopGates=true`.

## Moderator-only mercy-return verify

`/brmc` is moderator-only. Desk cannot prove client feel — check that each dest room shows a return plane at the same identity as the outbound, then walk back:

1. `/brmc pocket commons` — east into False First; dest threshold at `(23, 65, 52)`; walk west back to First.
2. `/brmc pocket vestibule` — east through door 2 into Second; dest thresholds at `(70, 65, 19)` and `(70, 65, 20)`; walk west back.
3. `/brmc pocket utilities` — north into Buttons; dest thresholds at `(19, 65, -40)` and `(20, 65, -40)`; walk south back.
4. `/brmc pocket curving` — north into Second False First; dest threshold at `(62, 65, 62)`; walk south back.
5. `/brmc pocket janitor` — west into Custodial; dest threshold at `(-39, 65, 22)`; walk east back.

Already-generated dest chunks need a new world (or dest-chunk regen) to stamp the return thresholds. Mercy walk-back still fires in air at those identities if the old chunk has no block. Do not use `/brmc second` / `/brmc false_first` off-identity warps for this check — those land at Clark XYZ, not the gate identities.

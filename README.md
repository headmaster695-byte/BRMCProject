# BRMC

James Stevenson’s Backrooms / liminal-spaces Fabric mod. This pass is a **playable First Dimension vertical slice**: authored Clark cold-open, infinite yellow-mono maze with learnable pocket spines, and First gameplay rules.

Minecraft **26.2**, Fabric Loader **0.19.3**, Fabric API **0.158.0+26.2**, Java **25**.

## What is in this pass

- First Dimension registered as `brmc:first` with a `brmc:yellow_mono` chunk generator.
- Clark cold-open is a **32×32** authored chamber (post-threshold yellow room only). Fidelity target: Clark’s first Backrooms room from A24 *Backrooms* (2026). Chevron wallpaper bands, moist carpet, fluorescent troffer grid, four structural columns, four cardinal openings into the labyrinth. No store, basement, inbound portal, or tutorial UI. Spawn faces east.
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
  - Vestibule — door 1 → yellow airlock → door 2 already frames **red mono** → Second. Floor hole before door 2 is OOB (present, **not live**; Lost Island / SCRAM / soul not built).
  - Curving hall — invisible seam, yellow around a soft plan → Second False First.
- Destinations other than First are **thick stubs** (`brmc:dest_climate` cells): False First yellow-mono room, Second red-mono room, Buttons plant, Second False First soft yellow, Spiral well, Custodial closet. Still not full floors. Digital is sealed. Fourth does not exist.
- No entities. Distant unresolved cave-mood sounds foreshadow only.
- First rules: generated fabric regenerates (floor/ceiling faster so you cannot dig out of the layer); player-built / pillared blocks persist on the chunk; maps freeze; compass spins; F3 coordinates lie.
- Aesthetic boards land later under `floors/<slug>/`.

## Seamless gates (26.2, Immersive Portals class — not a door overlay)

The mod stays on 26.2. Immersive Portals is not downported. The player-facing path is **`LinkedVolumeBackend`**: a linked-dimension portal with per-portal LOD, dest-volume sampling, and a plane-cross identity swap. Hop is never the default.

**Must:** walk into another dimension through the plane as one camera/player motion. Not fade, not hop, not “see-through wallpaper that then teleports.”

### What this pass proves (all live First exits)

- **Architecture:** `LinkedDimensionPortal` + `PortalLod` (`FULL` / `MESH` / `IMPOSTOR`) + `PortalRenderBudget` (near portals spend FULL slots; farther / many open portals degrade). Not one forever-fullscreen blit.
- **See-through:** `DestinationVolumeSampler` reads the dest `ServerLevel` at identity coordinates (wider box, including DOWN wells) and syncs voxels to the threshold. Near portal = dest voxels. Mid = dest-climate room mesh oriented to facing. Far = tinted plane.
- **Walk-through:** `PortalCrossTracker` fires only on was-behind → now-through (horizontal or down). Dest chunks are held with `TicketType.PORTAL` before the identity-pose swap (`TeleportTransition.DO_NOTHING`). Loading swirl / “Downloading terrain” / “Entering X” are held off while a crossing is flagged.
- **Wired exits (with mercy return):** commons → False First, vestibule door 2 → Second, utilities → Buttons, curving hall → Second False First, janitor closet → Custodial. Walk back through the same plane to First. OOB hole and false-floor drop are architecture only — no live swap.
- **Visual tells (no tutorials):**
  - Commons: clean yellow→yellow dest room. `previewLies()` can invent First wallpaper dest does not have.
  - Vestibule door 2: yellow→red dest room; far side may chromatic-shift / heat-haze.
  - Utilities: plant continues (iron / copper).
  - Curving: yellow around a soft (lime) plan.
  - Custodial: light-gray closet.
  - East is not a marked compass: Clark→vestibule uses a yellow dogleg, not a cleaner runway.

### Honest gaps

True Immersive Portals see-through is a **second camera** into a live dest `ClientLevel` with stencil / portal clip and entity transfer. 26.2 BER (`submitCustomGeometry`) does not land that here. `FULL` is dest-sampled voxels, not a dual-world stencil. First still paints a short dest-climate backing alcove on commons / vestibule so IMPOSTOR / missing samples do not show void. Dest interiors are tiled climate cells, not full floors. OOB / false-floor systems are not live. Janitor NPC encounter is not built. F3 does not print pocket or dimension slugs.

Fallback order: IP if present → linked volume → hop only if `brmc.devAllowHopGates=true` → `RefusingGateBackend` (last resort, loud error).

## Entering First

New players arrive in the Clark chamber facing east. Moderators: `/brmc first`. QA pocket warps: `/brmc pocket clark|apartment|janitor|utilities|commons|vestibule|curving|false_floor|dead_zone`.

Build: Java 25, then `./gradlew build`.

## Verify next (playtester)

- Wake in a large empty yellow room: chevron wallpaper, moist carpet, troffer grid, columns, openings on four sides. No tutorial text, store, or portal remnant.
- Walk a cardinal: east vestibule (yellow then red-framed door 2), south commons (stays yellow), west apartment (white/oak break), north utilities (iron/copper plant).
- SE curve stays yellow. SW false-floor hole. NW stretch has no lights.
- Mine a wall: it comes back. Mine the floor: it comes back faster. Place / pillar blocks: they stay after regen and after relog.
- Maps do not chart First. Compass needle spins. F3 XYZ is wrong.
- Occasional distant wrong sound; nothing arrives.
- Commons door: look through — yellow dest **room**. Walk *through the plane*: same camera into False First. Walls through the door may be a lie.
- Vestibule door 2: look through — red dest room, possible haze. Walk through the plane into Second.
- Utilities deep door: look through — iron/copper plant. Walk north through the plane into Buttons.
- Curving seam: look through — yellow + lime plan. Walk north through the plane into Second False First.
- False-floor hole and vestibule OOB hole are architecture only — they do not dimension-cross.
- `/brmc pocket janitor` (moderators only): walk west through the closet into Custodial; walk back east to First. No janitor NPC.
- Dest stubs: walk back through the same plane. No F3 pocket / dest slugs on public walks.
- Hop stays off unless `brmc.devAllowHopGates=true`.

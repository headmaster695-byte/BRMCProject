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
- Destinations other than First are **stubs**. Digital is sealed. Fourth does not exist.
- No entities. Distant unresolved cave-mood sounds foreshadow only.
- First rules: generated fabric regenerates (floor/ceiling faster so you cannot dig out of the layer); player-built / pillared blocks persist on the chunk; maps freeze; compass spins; F3 coordinates lie.
- Aesthetic boards land later under `floors/<slug>/`.

## Seamless gates (26.2, no Immersive Portals)

The mod stays on 26.2. Immersive Portals is not downported.

Player-facing path is **`LinkedVolumeBackend`**:

- **See-through** is an *approximated linked volume*, not a second `ClientLevel`. Destination climate is generated through the door plane in First (commons yellow; vestibule door 2 red). `LinkedVolumeRenderer` draws a portal-plane + receding-room mesh on the invisible threshold.
- **Walk-through** keeps the same camera pose (identity transform, `TeleportTransition.DO_NOTHING`, no portal sound). Nether swirl / “Downloading terrain” / “Entering X” are suppressed while a linked-volume crossing is flagged.
- This is **not** true dual-world stencil rendering. There can still be a brief dest-chunk hitch; the last frame is held instead of a fade/swirl.

Fallback order: IP if present → linked volume → hop only if `brmc.devAllowHopGates=true` → `RefusingGateBackend` (last resort, loud error). Hop is never the default.

## Entering First

New players arrive in the Clark chamber facing east. Moderators: `/brmc first`. QA pocket warps: `/brmc pocket clark|apartment|utilities|commons|vestibule|curving|false_floor|dead_zone`.

Build: Java 25, then `./gradlew build`.

## Verify next (playtester)

- Wake in a large empty yellow room: chevron wallpaper, moist carpet, troffer grid, columns, openings on four sides. No tutorial text, store, or portal remnant.
- Walk a cardinal: east vestibule (yellow then red-framed door 2), south commons (stays yellow), west apartment (white/oak break), north utilities (iron/copper plant).
- SE curve stays yellow. SW false-floor hole. NW stretch has no lights.
- Mine a wall: it comes back. Mine the floor: it comes back faster. Place / pillar blocks: they stay after regen and after relog.
- Maps do not chart First. Compass needle spins. F3 XYZ is wrong.
- Occasional distant wrong sound; nothing arrives.
- Commons door: look through — yellow continues. Walk through — same camera, no swirl/fade; you are in False First (yellow stub).
- Vestibule door 2: look through — red climate already framed. Walk through — same camera into Second (red stub).
- Hop stays off unless `brmc.devAllowHopGates=true`.

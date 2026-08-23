# BRMC

James Stevenson’s Backrooms / liminal-spaces Fabric mod. This pass scaffolds the **First Dimension** (yellow mono, procedural infinite + authored anchors) and the gate architecture First needs.

Minecraft **26.2**, Fabric Loader **0.19.3**, Fabric API **0.158.0+26.2**, Java **25**.

## What is in this pass

- First Dimension registered as `brmc:first` with a `brmc:yellow_mono` chunk generator.
- Clark cold-open spawn is an authored chamber (post-threshold yellow room only). Fidelity target: Clark’s first Backrooms room from A24 *Backrooms* (2026). No store, basement, or inbound portal. Materials: mono-yellow chevron wallpaper, moist carpet, fluorescent troffer grid.
- Authored pocket slugs on the hub (anti-noise-soup), not distant worlds:
  - `first-apartment-pocket` — habitation lobe; drywall / bed / kitchen material break. **Not Bounded.** Rare janitor closet → Custodial.
  - `first-utilities` — infrastructure mouth → Buttons (ozone / contactor). Plant continues through the deep door.
  - `first-common-exit` — literacy teacher → False First. Single-threshold yellow→yellow. No airlock, red, or OOB hole. Not False Gate.
  - `first-vestibule` — true exit. Door 1 → yellow airlock → door 2 already frames **red mono** → Second (only true exit; door 2 always works once found). Floor hole before door 2 → Out of Bounds (stateful, late). Puzzle chain later: Lost Island → reactor SCRAM → Metaverse soul — not built here.
  - `first-curving-hall` — invisible seam → Second False First. Yellow continues around a soft plan.
  - `first-false-floor` → Spiral
  - `first-fluorescent-dead-zone` — anti-noise-soup pacing run, no lights
- Lore threads are architecture hooks only (no player tutorial text): habitation stack, maintenance stack, exit literacy stack, curve/nest, fall/coil, spine leave.
- Destinations other than First are **stubs**. Second is only via vestibule door 2. Branches off First are sub-dimensions. Digital is sealed — no First pocket opens it. Fourth does not exist.
- No entities. Distant unresolved sounds are a foreshadowing hook only.
- First rules: mining regenerates; building is tracked; maps / compass lie.
- Aesthetic boards land later under `floors/<slug>/`. This pass only reserves the path on `PocketStructure`.

## Immersive Portals

Seamless continuous space is a hard requirement: no teleport sting, fade-to-load, nether swirl, or “Entering X” UI. Prefer honest linked volumes (Immersive Portals–class). Commons is yellow→yellow on one stride. Vestibule door 2 is yellow→red material break on one stride.

Immersive Portals last published for Fabric **1.21.1** and the upstream repo is archived. A hard 26.2 dependency is not practical.

Gates go through `SeamlessGateBackend`:

- Prefer `ImmersivePortalsBackend` if an IP-class API is on the classpath.
- Otherwise `LoadingHopBackend` identity-teleports (same coordinates). That is a **development fallback**, not the target.

When an IP-compatible 26.2 artifact exists, implement `ensureOpening` as a see-through portal with an identity transform.

## Entering First

New players arrive in the Clark chamber. Moderators can use `/brmc first`.

Build: Java 25, then `./gradlew build`.

## Verify next

- Wake in the authored yellow chamber, no tutorial UI.
- Pockets read as anchors, not noise soup. Apartment / utilities break the yellow mono. Apartment is not Bounded.
- Vestibule is a two-door airlock; door 2 already shows red mono framed; the floor hole is OOB-only and late.
- Commons stays yellow→yellow into False First, never False Gate, with no airlock/red/OOB.
- Curving hall stays yellow around the soft L.
- Utilities plant continues through the deep door.
- Mined generated blocks return; placed blocks stay. Maps and compass fail in First.
- Deeper destination interiors stay stubbed until their own passes.

# BRMC

James Stevenson’s Backrooms / liminal-spaces Fabric mod. This pass scaffolds the **First Dimension** (yellow mono, procedural infinite + authored anchors) and the gate architecture First needs.

Minecraft **26.2**, Fabric Loader **0.19.3**, Fabric API **0.158.0+26.2**, Java **25**.

## What is in this pass

- First Dimension registered as `brmc:first` with a `brmc:yellow_mono` chunk generator.
- Clark cold-open spawn is an authored chamber (post-threshold yellow room only). Fidelity target: Clark’s first Backrooms room from A24 *Backrooms* (2026). No store, basement, or inbound portal. Materials: mono-yellow chevron wallpaper, moist carpet, fluorescent troffer grid.
- Authored pocket slugs on the hub (anti-noise-soup), not distant worlds:
  - `first-apartment-pocket` — habitation material break
  - `first-utilities` → Buttons
  - `first-common-exit` → False First (single yellow→yellow threshold; not False Gate)
  - `first-vestibule` — door → yellow airlock → door 2 → **Second** (only true exit). Hole before door 2 → Out of Bounds (stateful, late; puzzle chain not implemented)
  - `first-curving-hall` → Second False First (invisible seam)
  - `first-false-floor` → Spiral
  - `first-fluorescent-dead-zone` — pacing run, no lights
- Destinations other than First are **stubs**. Second is only via vestibule door 2. Branches off First are sub-dimensions. Digital is sealed. Fourth does not exist.
- No entities. Distant unresolved sounds are a foreshadowing hook only.
- First rules: mining regenerates; building is tracked; maps / compass lie.

## Immersive Portals

Seamless continuous space is a hard requirement: no teleport sting, fade-to-load, nether swirl, or “Entering X” UI. Commons is yellow→yellow on one stride. Vestibule door 2 is yellow→red material break.

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
- Pockets read as anchors, not noise soup. Apartment / utilities break the yellow mono.
- Vestibule is a two-door airlock; door 2 is Second; the floor hole is OOB-only and late.
- Commons goes to False First, never False Gate.
- Mined generated blocks return; placed blocks stay. Maps and compass fail in First.
- Deeper destination interiors stay stubbed until their own passes.

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

## Immersive Portals

Seamless continuous space is the only player-facing gate language: no teleport sting, fade-to-load, nether swirl, or “Entering X” UI. Commons is yellow→yellow. Vestibule door 2 is yellow→red.

- Prefer `ImmersivePortalsBackend` if an IP-class API is on the classpath.
- Otherwise gates **refuse** to hop (`RefusingGateBackend`) and log an error.
- `LoadingHopBackend` only if no IP-class backend **and** `brmc.devAllowHopGates=true` (`-Dbrmc.devAllowHopGates=true`, `BRMC_DEV_ALLOW_HOP_GATES=true`, or `config/brmc.properties`). A Fabric development workspace is not enough.

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
- Stepping a threshold does **not** hop unless an IP-class backend is present or `brmc.devAllowHopGates` is on.

# BRMC

James Stevenson’s Backrooms / liminal-spaces Fabric mod. This pass scaffolds the **First Dimension** (yellow mono, procedural infinite) and the gate architecture First needs.

Minecraft **26.2**, Fabric Loader **0.19.3**, Fabric API **0.158.0+26.2**, Java **25**.

## What is in this pass

- First Dimension registered as `brmc:first` with a `brmc:yellow_mono` chunk generator.
- Second (`brmc:second`) and False First (`brmc:false_first`) exist only as gate destinations. They are flat stubs.
- Vestibule (true exit → Second) and Commons (→ False First) as placeable threshold blocks, also stamped into First’s layout.
- Clark cold-open: first join sends the player to First. No tutorial text.
- First rules as code hooks: mining regenerates; building is tracked; maps do not update.

## Immersive Portals

Seamless, continuous space through the threshold is a hard design requirement.

Immersive Portals last published for Fabric **1.21.1** and the upstream repo is archived. A hard compile/runtime dependency is not practical on 26.2.

Gates go through `SeamlessGateBackend`:

- Prefer `ImmersivePortalsBackend` if an IP-class API is on the classpath.
- Otherwise `LoadingHopBackend` identity-teleports (same coordinates). That is a **development fallback**, not the target.

When an IP-compatible 26.2 artifact exists, implement `ensureOpening` as a see-through portal with an identity transform at the threshold. Do not invent a second hop protocol.

## Entering First

New players arrive in First via Clark cold-open. Moderators can also use:

```
/brmc first
/brmc second
/brmc false_first
```

Vestibule cell: `(4, 0)` in the 8-block grid. Commons cell: `(0, 4)`.

## Build

Java 25 is required.

```
./gradlew build
```

IDE setup follows the [Fabric getting-started guide](https://docs.fabricmc.net/develop/getting-started/creating-a-project#setting-up).

## Verify next

- World create: player wakes in yellow mono, no tutorial UI.
- Walk the maze; mined generated blocks return; placed blocks stay.
- Maps carried in First stay stale.
- Vestibule / Commons thresholds fire the gate service.
- Confirm destination floors stay aligned at Y 64–65 for a future seamless opening.
- Do not add dimensions or gates beyond First + Second + False First without a new design lock.

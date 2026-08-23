# Second Dimension block palette v1 (P0)

Architect owns look and naming. Fabric owns registry and models.

Namespace: `brmc`. Creative tab: `brmc:building` (muted “Building”). The tab lists **primaries only** (Wallpaper, Carpet, Ceiling Tile, Light, Door, Door Frame — First and Second). Variants stay registered. Item names are mundane. No Second entities on the mono for P0. Wear is noise, not landmarks.

Same grammar as First, crimson blood-tone — not Halloween. No destination-coded doors. No honest EXIT. No safelight on the mono body (P2 lobe-only later). Yellow-bleed stays P1 <<1% — do **not** register or place bleed in P0.

Textures in this pass are **TEMP** 16² Architect pack **v2** (lifted crimson — v1 ran darker than §7). Exact PNG bytes. `second_carpet_torn` IDAT recovered from the supplied stream (bad Adler-32 in the handoff); pixels unchanged. Regen dest / Second chunks after pull.

Look-targets (texture only — not block names or tooltips):
- Carpet: crimson field. Mold / torn are sparse dest wear, never a hole marker and never adjacent to vestibule arrival.
- Wallpaper: crimson print; B/C are seam phases. World-stepped chevron still uses the two-block family.
- Troffer: housing + bar. Dead is an unlit cousin. BE stub only — hum later, lower / wetter pitch.
- Door / frame: commercial crimson. Dest-side vestibule plane only. First-side door 2 stays glass.

## P0 registry

| Id | Visible name | Role |
|---|---|---|
| `second_wallpaper` | Wallpaper | Dest / Second field |
| `second_wallpaper_b` | Wallpaper | Seam phase (print shift) |
| `second_wallpaper_c` | Wallpaper | Seam phase (print shift) |
| `second_carpet` | Carpet | Crimson field |
| `second_carpet_mold` | Carpet | Sparse dest wear — never a landmark |
| `second_carpet_torn` | Carpet | Sparse dest wear — never a hole marker |
| `second_debris_carpet` | Carpet | Dest fill / underlayer (crimson mono only, never gray) |
| `second_ceiling_tile` | Ceiling Tile | Layer lid |
| `second_troffer` | Light | Lit fixture + hum-buzz block-entity stub |
| `second_troffer_dead` | Light | Unlit fixture + same BE |
| `second_door_commercial` | Door | Dest-side commercial panels |
| `second_door_frame` | Door Frame | Dest-side jambs |

False First stays yellow-mono vanilla this pass. Vestibule arrival from First is the material break into this crimson dest climate.

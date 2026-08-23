# First Dimension block palette v1 (P0)

Architect owns look and naming. Fabric owns registry and models.

Namespace: `brmc`. Creative tab: `brmc:building` (muted “Building”). The tab lists **primaries only** (Wallpaper, Carpet, Ceiling Tile, Light, Door, Door Frame, one Threshold, one Opening). Variants stay registered. Item names are mundane. No First entities. Wear is noise, not landmarks.

Textures in this pass are **TEMP** 16² from the Architect UV-hex pack (diamond wallpaper A/B/C, nap carpet, troffer housing/bar, commercial door). No EXIT bake. Regen First chunks after pull.

Look-targets (texture only — not block names or tooltips):
- Carpet: beige loop-pile nap with blotch moist *read*. Do not bake “moist” into ids or hover text.
- Wallpaper: mustard diamond print; B/C are seam phases. World-stepped chevron still uses the two-block family.
- Troffer: housing + bar. Dead / half stay dimmer cousins.
- Door 1 / frame: commercial beige + metal. Door 2 stays glass airlock cousin. Not a yellow→red First block.

## P0 registry

| Id | Visible name | Role |
|---|---|---|
| `first_wallpaper` | Wallpaper | Clark / maze field |
| `first_wallpaper_b` | Wallpaper | Seam phase (print shift) |
| `first_wallpaper_c` | Wallpaper | Seam phase (print shift) |
| `first_wallpaper_seam` | Wallpaper | Alias of phase B |
| `first_wallpaper_peel` | Wallpaper | Sparse wear |
| `first_wallpaper_dead` | Wallpaper | Dead-zone field + sparse wear |
| `first_carpet` | Carpet | Beige loop-pile field (blotch *read* in texture only) |
| `first_carpet_torn` | Carpet | Sparse maze wear — never a hole marker |
| `first_debris_carpet` | Carpet | Pit look-down |
| `first_carpet_stained` | Carpet | Sparse wear |
| `first_carpet_dry` | Carpet | Sparse wear |
| `first_ceiling_tile` | Ceiling Tile | Layer lid |
| `first_troffer` | Light | Lit fixture + hum-buzz block entity |
| `first_troffer_dead` | Light | Unlit dead-zone fixture + same BE |
| `first_troffer_half` | Light | Sparse maze noise + same BE |
| `first_door_commercial` | Door | Door 1 panels (commercial beige) |
| `first_door_frame` | Door Frame | Vestibule jambs (metal, not yellow→red) |
| `first_door_vestibule` | Door | Door 2 panels (glass airlock cousin) |

Apartment / utilities still use vanilla material-break stand-ins. Dest stubs stay climate vanilla this pass.

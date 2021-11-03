# Cuboid Resource Generators

This mod is based on the resource generators from CuboidDroid's Support Mod, which was specifically written for my first mod pack Cuboid Outpost.

Since Cuboid Outpost was released, I've had several enquiries about whether the resource generators were available separately, so I've decided to re-jig them a bit to make them available in a slightly more "generic" form for consumption in other modpacks.

Primarily this means that instead of the special ores used in Cuboid Outpost, the generators in this pack are based on a more familiar set of tiers, namely:

- Iron Singularity Resource Generator
- Gold Singularity Resource Generator
- Diamond Singularity Resource Generator
- Emerald Singularity Resource Generator
- Netherite Singularity Resource Generator

I've tweaked the colouring and texture of these SRGS to more closely match their new tiers, but apart from that, they work in the same way.

The main changes are around configuration and setup.

To use this mod, you will also need to have the Cuboid Quantum Singularities mod installed, which is what allows you to make the quantum singularities that act as "fuel" for these generators. Once you have that installed, and have set up a bunch of quantum singularities, you can then set up this mod to allow those quantum singularities to be used in these Singularity Resource Generators.

For each quantum singularity detected by this mod, a configuration file will be created (if missing) with default values in it when you first run the game with this mod installed.

Assuming you've created a coal quantum singularity using the Cuboid Quantum Singularities mod and have given it a resource location/key of "cuboidsingulariries:qs_coal", then the generated configuration file will be named `cuboidsingularities.qs_coal.json` and will look a bit like this:

``` json
{
  "input": "cuboidsingularities:qs_coal",
  "output": "minecraft:coal",
  "enabled": false,
  "iron": {
    "count": 1,
    "ticks": 160
  },
  "gold": {
    "count": 2,
    "ticks": 80
  },
  "diamond": {
    "count": 4,
    "ticks": 40
  }
  "emerald": {
    "count": 8,
    "ticks": 20
  },
  "netherite": {
    "count": 16,
    "ticks": 10
  }
}
```

Essentially, "input" is the quantum singularity being put into the generator, "output" is the item to be produced, and then for each of the 5 available tiers, "count" is the number of items to create per operation and "ticks" is the number of ticks between operations (where 20 ticks = 1 second). The "enabled" setting will default to false, meaning that the quantum singularity will not produce any items (this is useful for when you want to use some quantum singularities for reasons other than resource generation, or have not configured them yet).

In the above example, if a coal quantum singularity is placed inside an SRG, here's what will be produced:

* **Iron tier:** 1 item every 8 seconds = 0.125 items per second.
* **Gold tier:** 2 items every 4 seconds = 0.5 items per second.
* **Diamond tier:** 4 items every 2 seconds = 2 items per second.
* **Emerald tier:** 8 items every second = 8 items per second.
* **Netherite tier:** 16 items twice per second = 32 items per second.

You can, of course, mess around with the defaults, but in general it is better to increase the number of items rather than decrease the number of ticks per operation so as to keep TPS under control.

Other options for configuration are in the TOML file `cuboiddroid/cuboidresgen-common.toml` in the config folder. These include (per tier settings):

* **Auto eject** - if `false`, then items must be piped out to manually taken from the UI. If `true`, then if an inventory is placed directly above the SRG, items will be automatically piped upward (e.g. a chest or a storage drawer)
* **Maximum extract speed** - the maximum number of items to allow being piped out per operation
* **Maximum ejection speed** - the maximum speed at which items will be auto-ejected if enabled per operation

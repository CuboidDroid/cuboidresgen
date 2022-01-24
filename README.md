# CuboidDroid's Quantum Singularity Resource Generators

This mod is based on the resource generators from CuboidDroid's Support Mod, which was specifically written for my first mod pack Cuboid Outpost.

Since Cuboid Outpost was released, I've had several enquiries about whether the resource generators were available separately, so I've decided to re-jig them a bit to make them available in a slightly more "generic" form for consumption in other modpacks.

Primarily this means that instead of the special ores used in Cuboid Outpost, the generators in this pack are based on a more familiar set of tiers, namely:

- **Iron** Singularity Resource Generator
- **Gold** Singularity Resource Generator
- **Diamond** Singularity Resource Generator
- **Emerald** Singularity Resource Generator
- **Netherite** Singularity Resource Generator

I've tweaked the colouring and texture of these SRG's to more closely match their new vanilla tiers, but apart from that, they work in the same way.

The main changes are around configuration and setup.

## Using this mod in your own mod pack

You're welcome to use this mod in your own mod pack if you choose.

To use this mod, you will also need to have my Quantum Singularities mod installed, which is what allows you to make the quantum singularities that act as "fuel" for these generators. Once you have that installed, and have set up a bunch of quantum singularities, you can then set up this mod to allow those quantum singularities to be used in these Singularity Resource Generators.

### The generator "recipe" JSON files

When running an instance of Minecraft with this mod installed (and with the Quantum Singularities mod installed and configured), for each quantum singularity detected by this mod, a configuration file will be created (if missing) with default values in it when you next run the game.

Assuming you've created a coal quantum singularity using the Cuboid Quantum Singularities 
mod and have given it a name of "coal_qs", then the generated configuration file will
be named `config/cuboiddroid/cuboidsrg/generators/coal_qs.json` and will look a bit 
like this:

``` json
{
  "input": "cuboidqs:coal_qs",
  "output": "minecraft:coal",
  "enabled": false,
  "tickMultiplier": 1.0,
  "countMultiplier": 1.0
}
```

The values in this file are as follows:

* **input** - the ID (shown in tooltips in-game) of the quantum singularity being put into the generator.
* **output** - the item to be produced.
* **enabled** - if `true`, then the singularity can be used to generate resources. If `false` then it will 
  not produce resources. This setting will default to `true`, but can be set to `false` meaning that the quantum singularity 
  will not produce any items (this is useful for when you want to use some quantum singularities 
  for reasons other than resource generation, or have not configured them yet).
* **tickMultiplier** - multiplies the base number of ticks per operation for the tier of
  generator in use by this value. This can be used to make certain outputs slower to obtain.
  For example, you might want to make emeralds be produced 4 times slower than other items, so 
  you could set this to 4.0
* **countMultiplier** - multiplies the base number of items produced per operation for the tier
  of generator in use by this value. This can be used to make resources be generated at a quicker
  rate (e.g. for cobble, setting this to 2.0 would mean twice as many items as other resources
  would be generated per operation) or a slower rate (e.g. you might want fewer diamonds being 
  produced, so could set this to 0.5 to halve the amount output). Note that fractional results 
  are always rounded down! So setting this to a value < 1 might mean no items are generated for
  tiers where the item count per operation is 1.

In the above example, if a coal quantum singularity is placed inside an SRG, here's what will 
be produced:

* **Iron tier:** 1 item every 8 seconds = 0.125 items per second.
* **Gold tier:** 2 items every 4 seconds = 0.5 items per second.
* **Diamond tier:** 4 items every 2 seconds = 2 items per second.
* **Emerald tier:** 8 items every second = 8 items per second.
* **Netherite tier:** 16 items twice per second = 32 items per second.

You can, of course, mess around with the defaults, but in general it is better to increase the number of items rather than decrease the number of ticks per operation if you want more being produced so as to keep TPS under control.

### Configuring the SRG tiers

Other options for configuration are in the TOML file `cuboiddroid/cuboidsrg/cuboidsrg.toml` in the config folder. These include (per tier settings):

* **`auto_eject`** - if `false`, then items must be piped out or manually taken from the UI (as in the Cuboid Outpost mod pack). If `true`, then if an inventory is placed directly above the SRG, items will be automatically piped upward (e.g. a chest or a storage drawer)
* **`ticks`** - the default number of ticks per operation for the specific tier when generating outputs
* **`buffer`** - the size of the hidden internal buffer for the output items in addition to the stack in the output slot
* **`items`** - the default number of items to produce per operation when generating items
* **`dimensions`** - a semi-colon separated list of dimension resource locations. If blank, then no whitelist/blacklist behaviour is used, and the SRG will work in all dimensions.
* **`is_blacklist`** - indicates whether the `dimensions` list is a blacklist (if this is set to `true`) or a whitelist (if this is set to `false`).

The last two settings let you determine whether the SRG, by tier, should be restricted to only work in specific dimensions (or disallowed from working in specific dimensions), which could allow you to ensure that the SRG's only become useful to the player once sufficient progress through your pack has been made.

An example for how to ONLY allow an SRG tier to work in the End would be to use these settings:
- `dimensions = "minecraft:the_end"`
- `is_blacklist = false`

An example for how to disable an SRG tier in the Nether, and the End, but allow it to be used in the overworld and any other modded dimensions would be to use a blacklist as follows:
- `dimensions = "minecraft:the_end;minecraft:the_nether"`
- `is_blacklist = true`

## FAQ

### I need help configuring this mod in my pack. Where can I ask questions?

If you need assistance getting things running in your modpack, then reach out to me on [my Discord](https://discord.gg/j9zWKFuBtU) and look for the channel dedicated to this mod, and I'll do what I can to help.

### I've found a bug or have a suggestion for improvement, where can I tell you about these?

If you find bugs, have suggestions or requests for enhancement, please use the [Issue Tracker](https://github.com/CuboidDroid/cuboidresgen/issues) on GitHub for this mod.

### Can I use this mod in my own mod pack?

Yes. Please go ahead!

### Where did you get the idea for this mod?

The Quantum Singularity idea and implementation were heavily inspired by the Singularities and Compressor from [BlakeBr0's Extended Crafting](https://www.curseforge.com/minecraft/mc-mods/extended-crafting) mod, which is aimed at a more end-game scenario, whereas I created these primarily as a way to make the starting game slightly different (when used in conjunction with Quantum Singularities) in my Cuboid Outpost mod pack.

### How can I support you in making more mods / mod packs?

If I'm totally honest, I just do this for fun in my very limited spare time, so I'm really not expecting any financial support. A nice "thank you" on [my Discord](https://discord.gg/j9zWKFuBtU) if more than sufficient!

However, I have learnt that sometimes people just want to express their thanks in other ways, so in response to people asking for it, I've gone ahead and made it possible to support me financially anyway.

On the CurseForge page for this mod you should find a button to [donate using PayPal](https://www.paypal.com/donate/?hosted_button_id=3NREHL7EUD5JG) if that's your preference for a one-off donation / thank you, or if you'd like to really encourage me to keep at it, you can also support me on a monthly basis via [Patreon](https://www.patreon.com/cuboiddroid).

Let me say it again though - I don't expect any financial compensation - a "thank you" is sufficient!

---
## Have fun!

I hope you find using this mod (and/or the companion mods for Quantum Singularities and Singularity Power Generators (coming soon hopefully!)) simple and easy, and wish you all the best in building your next successful mod pack!

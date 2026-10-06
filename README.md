# Emerald Tools

A Fabric mod for Minecraft 26.1.2 that turns emeralds into a full set of tools and armor, plus an emerald golem. Emerald gear hits harder, mines faster and protects better than diamond, but wears out in half the time.

## Tools

Emerald sword, shovel, pickaxe, axe and hoe. Compared to diamond, each tool:

- deals **twice the attack damage**
- mines **twice as fast** (mining speed 16 vs. 8)
- has **half the durability** (780 vs. 1561)

Attack speed, mining level and enchantability (10) are the same as diamond, so the emerald pickaxe can mine obsidian.

| Tool | Attack damage (diamond → emerald) | Attack speed |
|---|---|---|
| Sword | 7 → 14 | 1.6 |
| Shovel | 5.5 → 11 | 1.0 |
| Pickaxe | 5 → 10 | 1.2 |
| Axe | 9 → 18 | 1.0 |
| Hoe | 1 → 2 | 4.0 |

## Armor

Emerald helmet, chestplate, leggings and boots. Compared to diamond, each piece has **twice the armor points and armor toughness** and **half the durability**.

| Piece | Armor (diamond → emerald) | Toughness | Durability (diamond → emerald) |
|---|---|---|---|
| Helmet | 3 → 6 | 2 → 4 | 363 → 181 |
| Chestplate | 8 → 16 | 2 → 4 | 528 → 264 |
| Leggings | 6 → 12 | 2 → 4 | 495 → 247 |
| Boots | 3 → 6 | 2 → 4 | 429 → 214 |

Enchantability (10), knockback resistance (none) and the equip sound are the same as diamond. Emerald armor can be enchanted and decorated with armor trims.

A full emerald set adds up to 40 armor points, but Minecraft caps a player's armor at 30, and its damage formula only counts up to 20. So a full set protects noticeably more than full diamond (20 points), but not twice as much.

## Ender arrow

Craft an arrow and an ender pearl together (shapeless) to get one ender arrow. Shoot it from a bow or crossbow, and you teleport to wherever it lands, whether it hits a block or a mob. Like an ender pearl, the teleport deals 5 damage and resets your fall distance. The arrow is used up when it lands.

If you're in a different dimension when the arrow lands, nothing happens. Ender arrows fired from a dispenser have no shooter, so they just land and break.

In creative mode, the ender arrow is next to the spectral arrow in the Combat tab.

## Emerald golem

Build it like an iron golem, with emerald blocks: a T of four emerald blocks with a carved pumpkin or jack o'lantern on top. A dispenser can place the pumpkin too.

The emerald golem is 4 blocks tall, with thicker arms than the iron golem. Compared to the iron golem it:

- deals **twice the attack damage** (30 vs. 15)
- moves **twice as fast** (movement speed 0.5 vs. 0.25)
- attacks **creepers** as well as every monster an iron golem attacks

Health (100), knockback resistance and village behavior are the same as the iron golem, and like a player-built iron golem it never attacks players unless they hit it first. Feed it an emerald to heal it by 25. When it dies it drops 3-5 emeralds and up to 2 poppies.

## Crafting and repair

Every tool and armor piece uses the same recipe shape as its diamond counterpart, with emeralds in place of diamonds. Recipes unlock in the recipe book once you pick up an emerald.

Tools and armor are repaired with emeralds in an anvil, or by combining two damaged items of the same kind.

In creative mode, the shovel, pickaxe, axe and hoe are next to the diamond tools in the Tools & Utilities tab. The sword, the axe and the armor are next to their diamond counterparts in the Combat tab.

## Requirements

- Minecraft 26.1.2
- Fabric Loader 0.19.3 or newer
- Fabric API
- Java 25

## Building from source

```sh
./gradlew build
```

The mod jar ends up in `build/libs/`. `build` also runs the game tests, which check every tool and armor piece against its diamond counterpart, check that the ender arrow crafts, fires and teleports, and check the emerald golem's stats, summoning and targeting against the iron golem.

Models, recipes, tags, translations, the golem's loot table and the worn-armor definition are generated into `src/main/generated/`. After changing an item, regenerate them with:

```sh
./gradlew runDatagen
```

To try the mod in a development client, run `./gradlew runClient`.

## License

Released under [CC0 1.0](LICENSE).

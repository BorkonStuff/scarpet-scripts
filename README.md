# Scarpet-scripts

Collection of various scarpet scripts that might be useful to others besides me.

You need the [Carpet mod](https://www.curseforge.com/minecraft/mc-mods/carpet) to be able to run those.

[See here](https://github.com/gnembon/fabric-carpet/wiki/Installing-carpet-scripts-in-your-world) for how to install scarpet scripts in your world. The simple instructions are: go to your save, make a directory called "scripts" if it doesn't exist, copy script there. Some scripts might need to be loaded manually in Minecraft with the command "/script load <name of script>".

## Voids

[voids.sc](voids.sc) contains the creative tool for managing multiple dimensions inside one creative world.
The commands it provides are:
 - `/voids add "Name of dimension"` - Creates a dimension named "Name of dimension" with no blocks or structures and the default plains biome. It also prints a clickable teleport link in chat that you can immediately click to teleport to your new dimension.
 - `/voids add "Name of dimension" <biome>` - Just like above, but you can specify which biome the dimension should have.
 - `/voids tp "Name of dimension"` - Teleports player to the dimension. Beware, even though there should be a block under your feet where you teleport, Minecraft is sometimes buggy and you will fall to your death.
 - `/voids list` - Lists all managed dimensions with clickable teleport links.

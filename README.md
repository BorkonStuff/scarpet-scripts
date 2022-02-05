# Scarpet-scripts

Collection of various scarpet scripts that might be useful to others besides me.

You need the [Carpet mod](https://www.curseforge.com/minecraft/mc-mods/carpet) to be able to run those.

[See here](https://github.com/gnembon/fabric-carpet/wiki/Installing-carpet-scripts-in-your-world) for how to install scarpet scripts in your world. The simple instructions are: go to your world save, make a directory called `scripts` if it doesn't exist, copy script there. Some scripts might need to be loaded manually in Minecraft with the command `/script load <name of script>`.

## Voids

[voids.sc](voids.sc) contains the creative tool for managing multiple dimensions inside one creative world.
The commands it provides are:
 - `/voids add "Name of dimension"` - Creates a dimension named "Name of dimension" with no blocks or structures and the default plains biome. It also prints a clickable teleport link in chat that you can immediately click to teleport to your new dimension.
 - `/voids add "Name of dimension" <biome>` - Just like above, but you can specify which biome the dimension should have.
 - `/voids tp "Name of dimension"` - Teleports player to the dimension. Beware, even though there should be a block under your feet where you teleport, Minecraft is sometimes buggy and you will fall to your death.
 - `/voids list` - Lists all managed dimensions with clickable teleport links.

Quick explanation video: https://youtu.be/eF4W2_BvKCc

## Vdist

[vdist.sc](vdist.sc) contains a tool for coloring chunks based on hostile mobs for testing view distance settings.
The commands are:
 - `/vdist center <pos>` sets the position of the zombie villager.
 - `/vdist c <pos>` color the chunk at position based on count of hostile mobs.
 - `/vdist pscan <number of chunks>` teleport the player around in a 2*nX2*n area coloring chunks based on hostile mob count.

Explanation video: https://youtu.be/tLIje9-RxN8

## Scope

[scope.sc](scope.sc) is an oscilloscope for redstone signals.
The commands are:
 - `/scope pos <pos>` - set the coordinates for the lower left block of the display, the display is currently always facing in the negative z direction.
 - `/scope stop` - stop everything, both data collection and rendering.
 - `/scope probe <pos> <name>` - measure block power levels at position `<pos>`.
 - `/scope toggle_scroll` - toggle between scrolling and sweeping the display.
 - `/scope reset` - resets all data and sets all display options to defaults.
 - `/scope single` - perform a single recording of 200 ticks and freeze.
 - `/scope trigger <name>` - trigger a single recording when the probe called `<name>` reads >0.
 - `/scope vgrid <ticks>` - change the density of vertical grid lines. Default is 10, which gives us one grid line per 10 ticks of recorded data.
 - `/scope toggle_draw` - toggle rendering of the display while not affecting if the data is recorded.
 - `/scope sprobe <pos>` - power level of the block at `<pos>` decides the scaling of the display (0-smallest, 8-normal size).
 - `/scope dprobe <pos>` - power level of the block at `<pos>` toggles rendering.

Explanation video: https://youtu.be/Hq32fcvOdVw

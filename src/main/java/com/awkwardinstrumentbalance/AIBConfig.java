package com.awkwardinstrumentbalance;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "awkward-instrument-balance")
public class AIBConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public int woodenDurability = 4;

    @ConfigEntry.Gui.Tooltip
    public int stoneDurability = 32;

    @ConfigEntry.Gui.Tooltip
    public int copperDurability = 128;

    @ConfigEntry.Gui.Tooltip
    public int ironDurability = 256;

    @ConfigEntry.Gui.Tooltip
    public int goldDurability = 224;

    @ConfigEntry.Gui.Tooltip
    public int diamondDurability = 1536;

    @ConfigEntry.Gui.Tooltip
    public int netheriteDurability = 2048;

    @ConfigEntry.Gui.Tooltip
    public float woodenSpeed = 1.3F;

    @ConfigEntry.Gui.Tooltip
    public float stoneSpeed = 2.7F;
}

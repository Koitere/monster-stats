package com.monsterstats;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

@ConfigGroup("monsterstats")
public interface MonsterStatsConfig extends Config
{
    @ConfigItem(
            keyName = "showStatsMenuOption",
            name = "Show Stats Menu Option",
            description = "Enable right-click 'Stats' option for NPCs, side panel must also be enabled."
    )
    default boolean showStatsMenuOption()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showHoverTooltip",
            name = "Show Hover Tooltip",
            description = "Show a tooltip with elemental weakness and weakness percent when hovering over monsters."
    )
    default boolean showHoverTooltip()
    {
        return true;
    }

    @ConfigItem(
            keyName = "shiftForTooltip",
            name = "Modifier key for Tooltip",
            description = "Hover tooltip only appears when the selected modifier key is held."
    )
    default boolean shiftForTooltip()
    {
        return true;
    }

    @ConfigItem(
            keyName = "tooltipModifierKey",
            name = "Tooltip Modifier Key",
            description = "Click, then press a key to set which key shows the hover tooltip while held."
    )
    default Keybind tooltipModifierKey() { return new Keybind(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK); }

    @ConfigItem(
            keyName = "enableSidePanel",
            name = "Enable Side Panel",
            description = "Enables the searchable side panel to display more monster stats."
    )
    default boolean enableSidePanel() { return true; }

    @ConfigItem(
            keyName = "showExamineChat",
            name = "Show Stats on Examine",
            description = "Display key monster stats in the chatbox when you examine an NPC."
    )
    default boolean showExamineChat() { return true; }
}

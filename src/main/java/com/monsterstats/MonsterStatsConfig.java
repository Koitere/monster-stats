package com.monsterstats;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("monsterstats")
public interface MonsterStatsConfig extends Config
{
    @ConfigItem(
            keyName = "showStatsMenuOption",
            name = "Show Stats Menu Option",
            description = "Enable right-click 'Stats' option for NPCs"
    )
    default boolean showStatsMenuOption()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showHoverTooltip",
            name = "Show Hover Tooltip",
            description = "Show a tooltip with elemental weakness and weakness percent when hovering over monsters"
    )
    default boolean showHoverTooltip()
    {
        return true;
    }

    @ConfigItem(
            keyName = "shiftForTooltip",
            name = "Shift for Tooltip",
            description = "Hover tooltip only appears when the Shift key is held."
    )
    default boolean shiftForTooltip()
    {
        return true;
    }
}

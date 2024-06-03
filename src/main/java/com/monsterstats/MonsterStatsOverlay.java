package com.monsterstats;

import com.google.inject.Inject;

import java.awt.*;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;

public class MonsterStatsOverlay extends Overlay
{
    private final MonsterStatsPlugin plugin;
    private final Client client;
    private final TooltipManager tooltipManager;

    @Inject
    MonsterStatsOverlay(MonsterStatsPlugin plugin, Client client, TooltipManager tooltipManager)
    {
        this.plugin = plugin;
        this.client = client;
        this.tooltipManager = tooltipManager;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        for (NPC npc : client.getNpcs())
        {
            renderNpc(graphics, npc);
        }
        return null;
    }

    private void renderNpc(Graphics2D graphics, NPC npc)
    {
        if (npc == null || npc.getCombatLevel() <= 0)
        {
            return;
        }

        String weakness = getWeakness(npc.getId());
        if (weakness == null)
        {
            return;
        }

        LocalPoint lp = npc.getLocalLocation();
        Point textLocation = Perspective.getCanvasTextLocation(client, graphics, lp, weakness, npc.getLogicalHeight() + 40);

        if (textLocation != null)
        {
            OverlayUtil.renderTextLocation(graphics, textLocation, weakness, Color.decode("#FFFFFFFF"));
        }
    }

    private String getWeakness(int npcId)
    {
        // TODO: Fetch or retrieve the weakness data for the NPC
        return "Example Weakness";
    }
}

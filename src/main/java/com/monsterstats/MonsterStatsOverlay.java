package com.monsterstats;

import com.google.inject.Inject;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ImageUtil;

public class MonsterStatsOverlay extends Overlay
{
    private final MonsterStatsPlugin plugin;
    private final TooltipManager tooltipManager;
    private final Client client;

    final BufferedImage stabIcon;
    final BufferedImage crushIcon;
    final BufferedImage slashIcon;
    final BufferedImage standardIcon;
    final BufferedImage heavyIcon;
    final BufferedImage lightIcon;
    final BufferedImage elementalIcon;
    final BufferedImage magicIcon;
    final BufferedImage fireIcon;
    final BufferedImage waterIcon;
    final BufferedImage airIcon;
    final BufferedImage earthIcon;
    final BufferedImage maxHitIcon;
    final BufferedImage attackStyleIcon;

    @Inject
    MonsterStatsOverlay(MonsterStatsPlugin plugin, Client client, TooltipManager tooltipManager)
    {
        this.plugin = plugin;
        this.client = client;
        this.tooltipManager = tooltipManager;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        stabIcon = ImageUtil.loadImageResource(getClass(),"/White_dagger.png");
        crushIcon = ImageUtil.loadImageResource(getClass(),"/White_warhammer.png");
        slashIcon = ImageUtil.loadImageResource(getClass(),"/White_scimitar.png");
        standardIcon = ImageUtil.loadImageResource(getClass(),"/Steel_arrow_5.png");
        heavyIcon = ImageUtil.loadImageResource(getClass(),"/Steel_bolts_5.png");
        lightIcon = ImageUtil.loadImageResource(getClass(),"/Steel_dart.png");
        elementalIcon = ImageUtil.loadImageResource(getClass(),"/Pure_essence.png");
        magicIcon = ImageUtil.loadImageResource(getClass(),"/Magic_icon.png");
        fireIcon = ImageUtil.loadImageResource(getClass(), "/Fire_rune.png");
        waterIcon = ImageUtil.loadImageResource(getClass(), "/Water_rune.png");
        airIcon = ImageUtil.loadImageResource(getClass(), "/Air_rune.png");
        earthIcon = ImageUtil.loadImageResource(getClass(), "/Earth_rune.png");
        maxHitIcon = ImageUtil.loadImageResource(getClass(), "/Damage_hitsplat_(max_hit).png");
        attackStyleIcon = ImageUtil.loadImageResource(getClass(), "/Combat_icon.png");
    }

    public BufferedImage getElementalWeaknessIcon(String elementalWeakness)
    {
        switch (elementalWeakness)
        {
            case "Air":
                return airIcon;
            case "Water":
                return waterIcon;
            case "Fire":
                return fireIcon;
            case "Earth":
                return earthIcon;
            default:
                return elementalIcon;
        }
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (plugin.hoveredNPC != null)
        {
            renderNpc(graphics, plugin.hoveredNPC);
        }
        return null;
    }

    private boolean isHoveringGameScene()
    {
        MenuEntry[] menuEntries = client.getMenuEntries();
        for (int i = menuEntries.length - 1; i >= 0; i--)
        {
            if (MenuAction.WALK.equals(menuEntries[i].getType()))
            {
                return true;
            }
        }
        return false;
    }

    private void renderNpc(Graphics2D graphics, NPC npc)
    {
        if (npc == null || npc.getCombatLevel() <= 0)
        {
            return;
        }

        Integer npcID = npc.getId();
        NPCStats idStats = NPCDataLoader.getIDStats(npcID);

        if (isHoveringGameScene())
        {
            renderTooltip(graphics, idStats);
        }
    }

private void renderTooltip(Graphics2D graphics, NPCStats stats)
{
    Point mousePosition = client.getMouseCanvasPosition();
    // Create a horizontal panel for icons and stats
    PanelComponent rowPanel = new PanelComponent();
    rowPanel.setPreferredLocation(new java.awt.Point(mousePosition.getX(), mousePosition.getY()));
    rowPanel.setOrientation(ComponentOrientation.HORIZONTAL);
    rowPanel.setBorder(new Rectangle(2,2,305,55)); //Set border of resulting tooltip
    rowPanel.setGap(new java.awt.Point(2, 0));  // Add horizontal gap between components

    tooltipManager.addFront(new Tooltip(rowPanel));
    rowPanel.getChildren().add(createIconWithText( (stabIcon), stats.getStabDefence()));
    rowPanel.getChildren().add(createIconWithText( (crushIcon), stats.getCrushDefence()));
    rowPanel.getChildren().add(createIconWithText( (slashIcon), stats.getSlashDefence()));
    rowPanel.getChildren().add(createIconWithText( (getElementalWeaknessIcon(stats.getElementalWeakness())), stats.getElementalPercent() + "%"));
    rowPanel.getChildren().add(createIconWithText( (magicIcon), stats.getMagicDefence()));
    rowPanel.getChildren().add(createIconWithText( (standardIcon), stats.getStandardDefence()));
    rowPanel.getChildren().add(createIconWithText( (heavyIcon), stats.getHeavyDefence()));
    rowPanel.getChildren().add(createIconWithText( (lightIcon), stats.getLightDefence()));

}

    private PanelComponent createIconWithText(BufferedImage icon, String text)
    {
        PanelComponent iconWithTextPanel = new PanelComponent();
        iconWithTextPanel.setOrientation(ComponentOrientation.VERTICAL);
        iconWithTextPanel.setBorder(new Rectangle(2,2,34,50)); //set border of individual icon/value pair
        iconWithTextPanel.setGap(new java.awt.Point(0, 3));  // Add vertical gap between icon and text
        iconWithTextPanel.getChildren().add(new ImageComponent(icon)); //add icon
        LineComponent valueLine = LineComponent.builder().left(" ").right("").left(text).build();
        iconWithTextPanel.getChildren().add(valueLine);

        return iconWithTextPanel;
    }

}

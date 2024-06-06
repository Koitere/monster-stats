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
import net.runelite.client.util.ImageUtil;

public class MonsterStatsOverlay extends Overlay
{
    private final MonsterStatsPlugin plugin;
    private final Client client;

    private static final Dimension IMAGE_SIZE = new Dimension(24, 24);

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

    @Inject
    MonsterStatsOverlay(MonsterStatsPlugin plugin, Client client)
    {
        this.plugin = plugin;
        this.client = client;
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

        String npcName = npc.getName();
        NPCStats npcStats = NPCDataLoader.getNPCStats(npcName);

        if (isHoveringGameScene())
        {
            renderTooltip(graphics, npcStats);
        }
    }

private void renderTooltip(Graphics2D graphics, NPCStats stats)
{
    // Get the current mouse position
    Point mousePosition = client.getMouseCanvasPosition();

    // Create a horizontal panel for icons and stats
    PanelComponent rowPanel = new PanelComponent();
    rowPanel.setPreferredLocation(new java.awt.Point(mousePosition.getX(), mousePosition.getY()));
    rowPanel.setOrientation(ComponentOrientation.HORIZONTAL);
    rowPanel.setBackgroundColor(Color.DARK_GRAY);
    rowPanel.setGap(new java.awt.Point(20, 0));  // Add horizontal gap between components

    // Add icons and corresponding stats
    rowPanel.getChildren().add(createIconWithText(resizeImage(crushIcon), stats.getCrushDefence()));
    rowPanel.getChildren().add(createIconWithText(resizeImage(stabIcon), stats.getStabDefence()));
    rowPanel.getChildren().add(createIconWithText(resizeImage(slashIcon), stats.getSlashDefence()));
    rowPanel.getChildren().add(createIconWithText(resizeImage(getElementalWeaknessIcon(stats.getElementalWeakness())), stats.getElementalPercent() + "%"));
    rowPanel.getChildren().add(createIconWithText(resizeImage(magicIcon), stats.getMagicDefence()));
    rowPanel.getChildren().add(createIconWithText(resizeImage(standardIcon), stats.getStandardDefence()));
    rowPanel.getChildren().add(createIconWithText(resizeImage(heavyIcon), stats.getHeavyDefence()));
    rowPanel.getChildren().add(createIconWithText(resizeImage(lightIcon), stats.getLightDefence()));

    rowPanel.render(graphics);
}

    private PanelComponent createIconWithText(BufferedImage icon, String text)
    {
        PanelComponent iconWithTextPanel = new PanelComponent();
        iconWithTextPanel.setPreferredSize(new Dimension(50, 60));
        iconWithTextPanel.setOrientation(ComponentOrientation.VERTICAL);
        iconWithTextPanel.setGap(new java.awt.Point(0, 2));  // Add vertical gap between icon and text
        iconWithTextPanel.setBackgroundColor(null);

        // Add icon
        iconWithTextPanel.getChildren().add(new ImageComponent(icon));

        // Add text
        iconWithTextPanel.getChildren().add(LineComponent.builder().left(text).build());

        return iconWithTextPanel;
    }

    private BufferedImage resizeImage(BufferedImage image)
    {
        BufferedImage resizedImage = new BufferedImage(IMAGE_SIZE.width, IMAGE_SIZE.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, IMAGE_SIZE.width, IMAGE_SIZE.height, null);
        g.dispose();
        return resizedImage;
    }

}

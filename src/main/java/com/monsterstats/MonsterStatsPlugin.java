package com.monsterstats;

import javax.inject.Inject;
import javax.swing.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
		name = "Monster Stats",
		description = "Shows monster stats with search functionality",
		tags = {"npc", "stats"}
)
public class MonsterStatsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MonsterStatsOverlay monsterStatsOverlay;

	@Inject
	private MonsterStatsConfig config;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ClientToolbar clientToolbar;

	NPC hoveredNPC;

	private NavigationButton navButton;
	private MonsterStatsPanel monsterStatsPanel;
	private static final String STATS_OPTION = "Stats";

	@Provides
	 MonsterStatsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MonsterStatsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(monsterStatsOverlay);
		monsterStatsPanel = new MonsterStatsPanel(monsterStatsOverlay, ImageUtil.loadImageResource(getClass(),"/icon.png"));

		navButton = NavigationButton.builder()
				.tooltip("Monster Stats")
				.icon(ImageUtil.loadImageResource(getClass(),"/icon.png"))
				.panel(monsterStatsPanel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(monsterStatsOverlay);
		clientToolbar.removeNavigation(navButton);
		monsterStatsPanel = null;
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (config.showStatsMenuOption() && event.getType() == MenuAction.NPC_SECOND_OPTION.getId() && event.getTarget() != null)
		{
			boolean alreadyHasStatsOption = false;
			for (MenuEntry entry : client.getMenuEntries())
			{
				if (entry.getOption().equals(STATS_OPTION))
				{
					alreadyHasStatsOption = true;
					break;
				}
			}

			if (!alreadyHasStatsOption)
			{
				client.createMenuEntry(client.getMenuEntries().length)
						.setOption(STATS_OPTION)
						.setTarget(event.getTarget())
						.setIdentifier(event.getIdentifier())
						.setType(MenuAction.RUNELITE)
						.setParam0(event.getActionParam0())
						.setParam1(event.getActionParam1());
			}
		}
		if (config.shiftForTooltip() && !client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			hoveredNPC = null;
			return;
		}
		if (config.showHoverTooltip())
		{
			MenuEntry entry = event.getMenuEntry();
            hoveredNPC = entry.getNpc();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuOption().equals(STATS_OPTION))
		{
			clientThread.invoke(() -> {
				String npcName = extractNpcName(event.getMenuTarget());
				if (npcName != null)
				{
					monsterStatsPanel.search(npcName, true);
					SwingUtilities.invokeLater(() -> clientToolbar.openPanel(navButton)); // Ensure the panel is opened on EDT
				}
			});
		}
	}

	private String extractNpcName(String target)
	{
		// Regular expression to find the text from menu target string
		Pattern pattern = Pattern.compile("<col=[0-9a-fA-F]+>([^<]+)<col=[0-9a-fA-F]+>");
		Matcher matcher = pattern.matcher(target);
		if (matcher.find())
		{
			return matcher.group(1);
		}
		return null;
	}

}

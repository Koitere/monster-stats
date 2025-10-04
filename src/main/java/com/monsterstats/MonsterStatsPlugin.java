package com.monsterstats;

import javax.inject.Inject;
import javax.swing.*;

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
import net.runelite.client.events.ConfigChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
		name = "Monster Stats",
		description = "Shows monster stats and other info with search functionality",
		tags = {"npc", "stats", "tooltip", "search", "defensive", "defence", "weakness", "elemental", "weaknesses", "bestiary", "monsters", "wiki"}
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
	private ConfigManager configManager;

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

		if (config.enableSidePanel()) {
			addNavBar();
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(monsterStatsOverlay);
		clientToolbar.removeNavigation(navButton);
		monsterStatsPanel = null;
	}

	public void addNavBar() {
		monsterStatsPanel = new MonsterStatsPanel(monsterStatsOverlay, ImageUtil.loadImageResource(getClass(),"/icon.png"));
		navButton = NavigationButton.builder()
				.tooltip("Monster Stats")
				.icon(ImageUtil.loadImageResource(getClass(),"/icon.png"))
				.panel(monsterStatsPanel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	public void removeNavBar()
	{
		if (navButton != null && monsterStatsPanel != null) {
			clientToolbar.removeNavigation(navButton);
			navButton = null;
			monsterStatsPanel = null;
		}
	}

	@Subscribe()
	public void onConfigChanged(ConfigChanged event) { //remove the nav button if the side panel gets disabled
		String configName = event.getKey();
		if (configName.equals("enableSidePanel")) {
			boolean enableSidePanel = Boolean.parseBoolean(event.getNewValue());
			if (enableSidePanel && navButton == null && monsterStatsPanel == null) {
				addNavBar();
			} else {
				removeNavBar();
				configManager.setConfiguration("monsterstats", "showStatsMenuOption", false); //also disable the right click menu, as this relies on side panel.
			}
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (config.enableSidePanel() && config.showStatsMenuOption() && event.getType() == MenuAction.NPC_SECOND_OPTION.getId() && event.getTarget() != null) //Add Stats option to right clicked NPCs
		{
			client.createMenuEntry(client.getMenuEntries().length)
					.setOption(STATS_OPTION)
					.setTarget(event.getTarget())
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE)
					.setParam0(event.getActionParam0())
					.setParam1(event.getActionParam1());

		}
		if (config.shiftForTooltip() && !client.isKeyPressed(KeyCode.KC_SHIFT)) //don't add tooltip if shift for tooltip is on
		{
			hoveredNPC = null;
			return;
		}
		if (config.showHoverTooltip()) //if hovering, tooltips are on, and shift for tooltip isn't on then show tooltip.
		{
			MenuEntry entry = event.getMenuEntry();
            hoveredNPC = entry.getNpc();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event) {
		if (event.getMenuOption().equals(STATS_OPTION)) {
			clientThread.invoke(() -> {
				NPC clickedNPC = client.getTopLevelWorldView().npcs().byIndex(event.getId()); //get the NPC from the MenuOptionClicked event id
				if (clickedNPC != null) {
					NPCStats npcStats = NPCDataLoader.getIDStats(clickedNPC.getId());
					if (npcStats.getName().contains("#")) { //if the name contains a '#' it has alternate forms and we will select this alt form.
						monsterStatsPanel.search(npcStats.getSearchName(), true, npcStats.getName().split("#", 2)[1]);
					} else { //otherwise we just select the default of that monster.
						monsterStatsPanel.search(npcStats.getSearchName(), true, "");
					}
					SwingUtilities.invokeLater(() -> clientToolbar.openPanel(navButton)); // Ensure the panel is opened on EDT
				}
			});

		}
	}
}

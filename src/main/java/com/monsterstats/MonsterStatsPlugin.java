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
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.KeyListener;
import java.awt.event.KeyEvent;

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

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private KeyManager keyManager;

	NPC hoveredNPC = null;

	private NavigationButton navButton;
	private MonsterStatsPanel monsterStatsPanel;
	private static final String STATS_OPTION = "Stats";
	private int lastExaminedNpcId = -1;
	private boolean modifierHeld = false;

	@Provides
	 MonsterStatsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MonsterStatsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(monsterStatsOverlay);
		keyManager.registerKeyListener(modifierKeyListener);

		if (config.enableSidePanel()) {
			addNavBar();
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(monsterStatsOverlay);
		keyManager.unregisterKeyListener(modifierKeyListener);
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
		String configGroup = event.getGroup();
		if (configName.equals("enableSidePanel") && configGroup.equals("monsterstats")) {
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
			NPCStats checkExist = NPCDataLoader.getIDStats(event.getMenuEntry().getNpc().getId());
			if (checkExist == null) { //if there is no valid entry in the database for this NPC, we will not add the Stats option.
				hoveredNPC = null;
				return;
			}
			client.createMenuEntry(client.getMenuEntries().length)
					.setOption(STATS_OPTION)
					.setTarget(event.getTarget())
					.setIdentifier(event.getIdentifier())
					.setType(MenuAction.RUNELITE)
					.setParam0(event.getActionParam0())
					.setParam1(event.getActionParam1());
		}
		if (config.shiftForTooltip() && !modifierHeld)
		{
			hoveredNPC = null;
			return;
		}
		if (config.showHoverTooltip()) //if hovering, tooltips are on, and shift for tooltip isn't on then show tooltip.
		{
			NPC currentNPC = event.getMenuEntry().getNpc(); //check if there is a valid NPC in this MenuEntry
			if (currentNPC == null)
			{
				return;
			}
			NPCStats checkExist = NPCDataLoader.getIDStats(currentNPC.getId());
			if (checkExist == null) { //if there is no valid entry in the database for this NPC, we will not add the hover tooltip.
				hoveredNPC = null;
				return;
			}
			MenuEntry entry = event.getMenuEntry();
            hoveredNPC = entry.getNpc();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event) {
		if (event.getMenuOption().equals(STATS_OPTION)) {
			clientThread.invoke(() -> {
				NPC clickedNPC = client.getTopLevelWorldView().npcs().byIndex(event.getId());
				if (clickedNPC != null) {
					NPCStats npcStats = NPCDataLoader.getIDStats(clickedNPC.getId());
					if (npcStats.getName().contains("#")) {
						monsterStatsPanel.search(npcStats.getSearchName(), true, npcStats.getName().split("#", 2)[1]);
					} else {
						monsterStatsPanel.search(npcStats.getSearchName(), true, "");
					}
					SwingUtilities.invokeLater(() -> clientToolbar.openPanel(navButton));
				}
			});
		}

		if (event.getMenuAction() == MenuAction.EXAMINE_NPC) {
			NPC examinedNPC = client.getTopLevelWorldView().npcs().byIndex(event.getId());
			lastExaminedNpcId = examinedNPC != null ? examinedNPC.getId() : -1;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		if (event.getType() != ChatMessageType.NPC_EXAMINE) {
			return;
		}
		if (!config.showExamineChat() || lastExaminedNpcId == -1) {
			return;
		}

		NPCStats stats = NPCDataLoader.getIDStats(lastExaminedNpcId);
		lastExaminedNpcId = -1;

		if (stats == null) {
			return;
		}

		queueExamineLine(stats.getSearchName() + " — HP: " + stats.getHitpoints() + " | Defence: " + stats.getDefenceLevel());
		queueExamineLine("Melee Defence — Stab: " + stats.getStabDefence() + " | Slash: " + stats.getSlashDefence() + " | Crush: " + stats.getCrushDefence());
		queueExamineLine("Magic Defence — Defence: " + stats.getMagicDefence() + " | Weakness: " + stats.getElementalWeakness() + " " + stats.getElementalPercent() + "%");
		queueExamineLine("Ranged Defence — Standard: " + stats.getStandardDefence() + " | Heavy: " + stats.getHeavyDefence() + " | Light: " + stats.getLightDefence());
	}

	private void queueExamineLine(String text) {
		String chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(text)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(chatMessage)
						.build());
	}

	private final KeyListener modifierKeyListener = new KeyListener()
	{
		@Override
		public void keyTyped(KeyEvent e) { }

		@Override
		public void keyPressed(KeyEvent e)
		{
			if (config.tooltipModifierKey().matches(e))
			{
				modifierHeld = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			if (config.tooltipModifierKey().matches(e))
			{
				modifierHeld = false;
			}
		}
	};
}

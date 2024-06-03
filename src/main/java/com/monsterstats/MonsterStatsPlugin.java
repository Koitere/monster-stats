package com.monsterstats;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "Monster Stats"
)
public class MonsterStatsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MonsterStatsOverlay monsterStatsOverlay;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(monsterStatsOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(monsterStatsOverlay);
	}
}

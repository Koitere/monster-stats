package com.monsterstats;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.runelite.client.ui.PluginPanel;

public class MonsterStatsPanel extends PluginPanel
{
    @Inject
    public MonsterStatsPanel()
    {
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String searchText = searchField.getText();
                try {
                    String weakness = OSRSWikiAPI.getMonsterWeakness(searchText);
                    // TODO: Display the result in the panel
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(searchField);
        add(searchButton);
    }
}

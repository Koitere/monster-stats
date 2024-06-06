package com.monsterstats;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MonsterStatsPanel extends PluginPanel
{
    private final JTextField searchField;
    private final JPanel resultsPanel;
    private final JPanel dataPanel;
    private final MonsterStatsOverlay monsterStatsOverlay;

    public MonsterStatsPanel(MonsterStatsOverlay monsterStatsOverlay, BufferedImage titleIcon)
    {
        this.monsterStatsOverlay = monsterStatsOverlay;

        setLayout(new BorderLayout());

        // Create a panel to hold the title and search field
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        // Add title with icon
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Monster Stats");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(new JLabel(new ImageIcon(titleIcon)));
        titlePanel.add(Box.createRigidArea(new Dimension(5, 0))); // Add some space between icon and title
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(5, 0))); // Add some space between icon and title
        titlePanel.add(new JLabel(new ImageIcon(titleIcon)));
        // Add the titlePanel to the northPanel
        northPanel.add(titlePanel);
        northPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between title and search field

        // Add search field
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                search(searchField.getText(), false);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                search(searchField.getText(), false);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                search(searchField.getText(), false);
            }
        });

        // Add the searchField to the northPanel
        northPanel.add(searchField);
        northPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between search field and results

        // Add the northPanel to the main panel
        add(northPanel, BorderLayout.NORTH);

        // Create a central panel to hold results and data panels with spacing
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setPreferredSize(new Dimension(300, 200));  // Set max height for the results panel

        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        centerPanel.add(resultsScrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between results and data panel
        centerPanel.add(new JScrollPane(dataPanel));

        add(centerPanel, BorderLayout.CENTER);
    }

    public void search(String searchString, boolean selectFirstMatch)
    {
        List<NPCStats> results = NPCDataLoader.getAllNPCStats().entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains(searchString.toLowerCase()))
                .sorted((a, b) -> {
                    if (a.getKey().equalsIgnoreCase(searchString)) return -1;
                    if (b.getKey().equalsIgnoreCase(searchString)) return 1;
                    return a.getKey().compareToIgnoreCase(b.getKey());
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        resultsPanel.removeAll();

        for (NPCStats npcStats : results)
        {
            JButton resultButton = new JButton(npcStats.getName());
            resultButton.addActionListener(e -> displayStats(npcStats));
            resultsPanel.add(resultButton);
        }

        if (selectFirstMatch && !results.isEmpty())
        {
            displayStats(results.get(0));
        }

        revalidate();
        repaint();
    }

    private void displayStats(NPCStats npcStats)
    {
        dataPanel.removeAll();

        // Create the stats panel section by section
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        addSectionTitle(statsPanel, "Melee Defence");
        addVerticalSpacing(statsPanel);
        addIconsAndValues(statsPanel, new BufferedImage[]{monsterStatsOverlay.stabIcon, monsterStatsOverlay.crushIcon, monsterStatsOverlay.slashIcon},
                new String[]{npcStats.getStabDefence(), npcStats.getCrushDefence(), npcStats.getSlashDefence()});

        addVerticalSpacing(statsPanel);

        addSectionTitle(statsPanel, "Magic Defence");
        addVerticalSpacing(statsPanel);

        BufferedImage elementalWeakness;
        elementalWeakness = monsterStatsOverlay.getElementalWeaknessIcon(npcStats.getElementalWeakness());

        addIconsAndValues(statsPanel, new BufferedImage[]{monsterStatsOverlay.magicIcon, elementalWeakness},
                new String[]{npcStats.getMagicDefence(), npcStats.getElementalPercent() + "%"});

        addVerticalSpacing(statsPanel);

        addSectionTitle(statsPanel, "Ranged Defence");
        addVerticalSpacing(statsPanel);
        addIconsAndValues(statsPanel, new BufferedImage[]{monsterStatsOverlay.standardIcon, monsterStatsOverlay.heavyIcon, monsterStatsOverlay.lightIcon},
                new String[]{npcStats.getStandardDefence(), npcStats.getHeavyDefence(), npcStats.getLightDefence()});

        dataPanel.add(statsPanel);

        revalidate();
        repaint();
    }

    private void addVerticalSpacing(JPanel panel)
    {
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void addIconsAndValues(JPanel panel, BufferedImage[] icons, String[] values)
    {
        JPanel iconsAndValuesPanel = new JPanel();
        iconsAndValuesPanel.setLayout(new GridLayout(2, icons.length, 10, 5)); // 2 rows, n columns, with horizontal and vertical gaps

        for (BufferedImage icon : icons) {
            JLabel iconLabel = new JLabel(new ImageIcon(icon));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconsAndValuesPanel.add(iconLabel);
        }

        for (String value : values) {
            JLabel valueLabel = new JLabel(value);
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconsAndValuesPanel.add(valueLabel);
        }

        panel.add(iconsAndValuesPanel);
    }

    private void addSectionTitle(JPanel panel, String text)
    {
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
    }
}

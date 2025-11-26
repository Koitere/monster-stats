package com.monsterstats;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.runelite.client.ui.ColorScheme;

public class MonsterStatsPanel extends PluginPanel
{
    private final JTextField searchField;
    private final JPanel resultsPanel;
    private final JPanel dataPanel;
    private final JPanel buttonPanel;
    private final JPanel maxHitTitlePanel;
    private final JPanel maxHitListPanel;
    private final JPanel attackStyleTitlePanel;
    private final JPanel attackStyleListPanel;
    private final JTextPane monsterTextPane;
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
        titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        titlePanel.setBorder(new EmptyBorder(5,5,5,5));
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
                search(searchField.getText(), false, "");
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                search(searchField.getText(), false, "");
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                search(searchField.getText(), false, "");
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
        resultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        resultsScrollPane.setPreferredSize(new Dimension(300, 200));  // Set max height for the results panel

        JPanel monsterLabelPanel = new JPanel();
        monsterLabelPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        monsterLabelPanel.setBorder(new EmptyBorder(5,0,5,0));
        monsterLabelPanel.setLayout(new BoxLayout( monsterLabelPanel, BoxLayout.X_AXIS));
        monsterLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        monsterTextPane = new JTextPane(); //To create wrapping text, we create a text pane with special document styling.
        monsterTextPane.setText("Monster Stats");
        monsterTextPane.setFont(new Font("Arial", Font.BOLD, 14));
        monsterTextPane.setEditable(false);
        monsterTextPane.setOpaque(false);
        monsterTextPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        monsterTextPane.setAlignmentY(Component.CENTER_ALIGNMENT);
        StyledDocument doc = monsterTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        monsterLabelPanel.add(new JLabel(new ImageIcon(titleIcon))); //Create Title area with our icon on either side of our wrap-able title object.
        monsterLabelPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        monsterLabelPanel.add(monsterTextPane);
        monsterLabelPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        monsterLabelPanel.add(new JLabel(new ImageIcon(titleIcon)));

        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        buttonPanel = new JPanel(new WrapLayout(FlowLayout.CENTER));
        JScrollPane buttonPane = new JScrollPane(buttonPanel);
        buttonPane.setPreferredSize(new Dimension(100, 100));

        //Building the max hit title panel
        maxHitTitlePanel = new JPanel();
        buildTitlePanel(maxHitTitlePanel, "Max Hit", new ImageIcon(monsterStatsOverlay.maxHitIcon));

        //Building the max hit list panel
        maxHitListPanel = new JPanel();
        JScrollPane maxHitScrollPane = buildListPanel(maxHitListPanel);


        //build attack style title panel
        attackStyleTitlePanel = new JPanel();
        buildTitlePanel(attackStyleTitlePanel, "Attack Style", new ImageIcon(monsterStatsOverlay.attackStyleIcon));

        //build attack style list panel
        attackStyleListPanel = new JPanel();
        JScrollPane attackStyleScrollPane = buildListPanel(attackStyleListPanel);

        //Adding everything to main center panel
        centerPanel.add(resultsScrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); //Spacer
        centerPanel.add(monsterLabelPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(buttonPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(new JScrollPane(dataPanel));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(maxHitTitlePanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(maxHitScrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(attackStyleTitlePanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(attackStyleScrollPane);

        add(centerPanel, BorderLayout.CENTER);

    }

    public void search(String searchString, boolean selectFirstMatch, String altForm)
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
            JButton resultButton = new JButton(npcStats.getSearchName());
            resultButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultButton.setHorizontalTextPosition(0);
            resultButton.setMaximumSize(new Dimension(220, resultButton.getMinimumSize().height)); //Set button to take up width of search results panel
            resultButton.addActionListener(e -> displayStats(npcStats, ""));
            resultsPanel.add(resultButton);
        }

        if (selectFirstMatch && !results.isEmpty())
        {
            displayStats(results.get(0), altForm);
        }

        revalidate();
        repaint();
    }

    private void displayStats(NPCStats npcStats, String altForm)
    {
        dataPanel.removeAll();
        buttonPanel.removeAll();

        JButton firstButton = null;
        NPCStats defaultStats = null;
        NPCStats selectedStats = null;

        if (npcStats.getAltForms() != null && !npcStats.getAltForms().isEmpty()) { //if there is multiple forms to display for a npc
            for (String k : npcStats.getAltForms().keySet()) {
                String parsedKey = k.replace("_", " ");
                JButton formButton = new JButton(parsedKey);
                if (firstButton == null) {
                    firstButton = formButton;
                    defaultStats = npcStats.getAltForms().get(k);
                }
                formButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        displayStats(npcStats, k);
                    }
                });
                buttonPanel.add(formButton);
                if (k.equals(altForm)) {
                    formButton.setEnabled(false); //show which button is selected
                }
            }
            if (altForm.isEmpty()) { //if altforms exist, but we don't have one selected, we enable the defaultButton
                firstButton.setEnabled(false);
            }
        } else {
            JButton noForms = new JButton("No Alternate Forms");
            noForms.setEnabled(false);
            buttonPanel.add(noForms);
        }
        if (!altForm.isEmpty()) { //if we are selecting an alt form, get that alt form to display
            selectedStats = npcStats.getAltForms().get(altForm);
        } else if (!npcStats.getAltForms().isEmpty()) { //if there is alt forms, but we aren't selecting one, show default one
            selectedStats = defaultStats;
        } else {
            selectedStats = npcStats; //if we have no alt forms
        }

        // Create the stats panel section by section
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(new EmptyBorder(5,5,5,5));
        statsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        addSectionSubTitle(statsPanel, "Melee Defence");
        addVerticalSpacing(statsPanel);
        assert selectedStats != null;
        addIconsAndValues(statsPanel, new BufferedImage[]{monsterStatsOverlay.stabIcon, monsterStatsOverlay.crushIcon, monsterStatsOverlay.slashIcon},
                new String[]{selectedStats.getStabDefence(), selectedStats.getCrushDefence(), selectedStats.getSlashDefence()});

        addVerticalSpacing(statsPanel);

        addSectionSubTitle(statsPanel, "Magic Defence");
        addVerticalSpacing(statsPanel);

        BufferedImage elementalWeakness;
        elementalWeakness = monsterStatsOverlay.getElementalWeaknessIcon(selectedStats.getElementalWeakness());

        addIconsAndValues(statsPanel, new BufferedImage[]{monsterStatsOverlay.magicIcon, elementalWeakness},
                new String[]{selectedStats.getMagicDefence(), selectedStats.getElementalPercent() + "%"});

        addVerticalSpacing(statsPanel);

        addSectionSubTitle(statsPanel, "Ranged Defence");
        addVerticalSpacing(statsPanel);
        addIconsAndValues(statsPanel, new BufferedImage[]{monsterStatsOverlay.standardIcon, monsterStatsOverlay.heavyIcon, monsterStatsOverlay.lightIcon},
                new String[]{selectedStats.getStandardDefence(), selectedStats.getHeavyDefence(), selectedStats.getLightDefence()});

        addVerticalSpacing(statsPanel);

        addSectionSubTitle(statsPanel, "Flat Armour");
        addVerticalSpacing(statsPanel);
        addIconsAndValues(statsPanel, new BufferedImage[]{monsterStatsOverlay.flatArmourIcon}, new String[]{selectedStats.getFlatArmour()});

        dataPanel.add(statsPanel);
        monsterTextPane.setText(selectedStats.getSearchName());

        maxHitListPanel.removeAll();
        addListItems(maxHitListPanel,selectedStats.getMaxHits());

        attackStyleListPanel.removeAll();
        addListItems(attackStyleListPanel,selectedStats.getAttackStyles());

        revalidate();
        repaint();
    }

    private void addListItems(JPanel listSection, List<String> listItems) //populate a JPanel with list items
    {
        for (String item : listItems)
        {
            JLabel itemLabel = new JLabel(item);
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            itemLabel.setBorder(new EmptyBorder(2, 5, 2, 5));
            itemLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align text inside label
            itemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listSection.add(itemLabel);
        }
    }

    private void buildTitlePanel(JPanel panel, String title, ImageIcon icon) //adds individual title sections for other stats
    {
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setPreferredSize(new Dimension(400,50));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(new JLabel(icon));
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(new JLabel(icon));
    }

    private JScrollPane buildListPanel(JPanel panel)
    {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JScrollPane listScrollPane = new JScrollPane(panel);
        listScrollPane.setPreferredSize(new Dimension(300, 100)); // Shows about 2 entries
        listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScrollPane.setBorder(null);
        return listScrollPane;
    }

    private void addVerticalSpacing(JPanel panel)
    {
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void addIconsAndValues(JPanel panel, BufferedImage[] icons, String[] values) //Adds stat values with corresponding stat icons
    {
        JPanel iconsAndValuesPanel = new JPanel();
        iconsAndValuesPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
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

    private void addSectionSubTitle(JPanel panel, String text) //adds sub titles to the stats section
    {
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
    }
}

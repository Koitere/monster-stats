package com.monsterstats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class NPCDataLoader {

    private static final String CSV_FILE = "/monster_data.csv";
    private static final Map<String, NPCStats> npcData = new HashMap<>();

    static {
        try (InputStream inputStream = NPCDataLoader.class.getResourceAsStream(CSV_FILE)) {
            if (inputStream == null) {
                System.err.println("Could not find file: " + CSV_FILE);
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] fields = parseCSVLine(line);
                        if (fields.length < 11) {
                            // Fill missing fields with '?'
                            fields = fillMissingFields(fields);
                        }
                        String name = NPCDataCleaner.cleanData(fields[0]);

                        // Exclude any names containing "(Deadman: Apocalypse)"
                        if (name.toLowerCase().contains("(deadman: apocalypse)")) {
                            continue;
                        }

                        String elementalWeakness = NPCDataCleaner.cleanElementalWeakness(fields[1]);
                        String elementalPercent = NPCDataCleaner.cleanElementalWeaknessPercent(fields[2].toLowerCase());
                        String crushDefence = NPCDataCleaner.cleanData(fields[3]);
                        String stabDefence = NPCDataCleaner.cleanData(fields[4]);
                        String slashDefence = NPCDataCleaner.cleanData(fields[5]);
                        String standardDefence = NPCDataCleaner.cleanData(fields[6]);
                        String heavyDefence = NPCDataCleaner.cleanData(fields[7]);
                        String lightDefence = NPCDataCleaner.cleanData(fields[8]);
                        String magicDefence = NPCDataCleaner.cleanData(fields[9]);
                        String combatLevel = NPCDataCleaner.cleanData(fields[10]);
                        NPCStats npcStats = new NPCStats(name, elementalWeakness, elementalPercent, crushDefence, stabDefence, slashDefence, standardDefence, heavyDefence, lightDefence, magicDefence, combatLevel);
                        npcData.put(name, npcStats);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] fillMissingFields(String[] fields) {
        String[] result = new String[11];
        for (int i = 0; i < 11; i++) {
            if (i < fields.length) {
                result[i] = fields[i];
            } else {
                result[i] = "?";
            }
        }
        return result;
    }

    private static String[] parseCSVLine(String line) {
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        char[] chars = line.toCharArray();
        for (char ch : chars) {
            if (inQuotes) {
                startCollectChar = true;
                if (ch == '\"') {
                    inQuotes = false;
                } else {
                    sb.append(ch);
                }
            } else {
                if (ch == '\"') {
                    inQuotes = true;
                    if (startCollectChar) {
                        sb.append('\"');
                    }
                } else if (ch == ',') {
                    sb.append('|'); // Use a different delimiter to split later
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString().split("\\|");
    }

    public static Map<String, NPCStats> getAllNPCStats()
    {
        return npcData;
    }

    public static NPCStats getNPCStats(String npcName)
    {
        return npcData.get(npcName);
    }
}

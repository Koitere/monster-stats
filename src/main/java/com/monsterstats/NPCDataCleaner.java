package com.monsterstats;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NPCDataCleaner {

    public static String cleanData(String start) {
        if (start == null || start.trim().isEmpty()) {
            return "?";
        }
        return getFirstValue(start.replace("\"", "").trim());
    }

    public static List<Integer> parseNPCIDs(String input) {
        List<Integer> result = new ArrayList<>();
        if (input.charAt(0) =='\"') {
            input = input.split("\"", 2)[1];
        }
        if (input == null || input.isEmpty() || input.equals("?")) {
            result.add(-1);
            return result;
        }
        // Split the input string by commas
        String[] parts = input.split(",");

        // Parse each part into an integer and add to the result list
        for (String part : parts) {
            try {
                result.add(Integer.parseInt(part.trim()));
            } catch (NumberFormatException e) {
                // Handle the case where part is not a valid integer
                log.debug("Invalid number format in NPC IDs: " + part, e);
            }
        }
        return result;
    }

    public static String cleanElementalWeakness(String start) {
        if (start == null || start.trim().isEmpty()) {
            return "None";
        }
        return getFirstValue(start.replace("\"", "").trim());
    }

    public static String cleanElementalWeaknessPercent(String start) {
        if (start == null || start.trim().isEmpty()) {
            return "0";
        }

        String cleanedValue = getFirstValue(start.replace("\"", "").trim());

        try {
            // Try to parse the cleaned value to an integer
            Integer.parseInt(cleanedValue);
        } catch (NumberFormatException e) {
            // If parsing fails, set the value to 0
            cleanedValue = "0";
        }

        return cleanedValue;
    }

    private static String getFirstValue(String input) {
        if (input.contains(",")) {
            return input.split(",")[0].trim();
        }
        return input;
    }
}


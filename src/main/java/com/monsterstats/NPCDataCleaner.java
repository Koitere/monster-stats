package com.monsterstats;

public class NPCDataCleaner {

    public static String cleanData(String start) {
        if (start == null || start.trim().isEmpty()) {
            return "?";
        }
        return getFirstValue(start.replace("\"", "").trim());
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


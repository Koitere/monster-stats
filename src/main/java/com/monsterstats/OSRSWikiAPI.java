package com.monsterstats;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OSRSWikiAPI {
    private static final String API_URL = "https://oldschool.runescape.wiki/api.php?action=query&format=json&prop=revisions&titles=%s&rvprop=content";

    public static String getMonsterWeakness(String monsterName) throws Exception {
        String urlStr = String.format(API_URL, monsterName);
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
            //JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            // Extract the necessary information from jsonObject
            return "Extracted Weakness Data";
        }
    }
}

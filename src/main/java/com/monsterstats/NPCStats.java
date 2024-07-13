package com.monsterstats;

import lombok.Getter;

import java.util.Map;
import java.util.HashMap;

@Getter
public class NPCStats {
    private final String name;
    private final String searchName;
    private final String elementalWeakness;
    private final String elementalPercent;
    private final String crushDefence;
    private final String stabDefence;
    private final String slashDefence;
    private final String standardDefence;
    private final String magicDefence;
    private final String heavyDefence;
    private final String lightDefence;
    private final Integer npcID;
    private final Map<String, NPCStats> altForms = new HashMap<>();

    public NPCStats(String name, String searchName, String elementalWeakness, String elementalPercent, String crushDefence, String stabDefence, String slashDefence, String standardDefence, String heavyDefence, String lightDefence, String magicDefence, Integer npcID, boolean alt) {
        this.name = name;
        this.searchName = searchName;
        this.elementalWeakness = elementalWeakness;
        this.elementalPercent = elementalPercent;
        this.crushDefence = crushDefence;
        this.stabDefence = stabDefence;
        this.slashDefence = slashDefence;
        this.standardDefence = standardDefence;
        this.magicDefence = magicDefence;
        this.heavyDefence = heavyDefence;
        this.lightDefence = lightDefence;
        this.npcID = npcID;
        if (alt) {
            String altName = name.split("#", 2)[1];
            altForms.put(altName,this);
        }
    }

    public void addForm(NPCStats altForm) {
        String altName = altForm.getName().split("#",2)[1];
        altForms.put(altName, altForm);
    }

}

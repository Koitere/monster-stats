package com.monsterstats;

import lombok.Getter;

import java.util.List;
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
    private final List<String> maxHits;
    private final List<String> attackStyles;
    private final Integer npcID;
    private final Map<String, NPCStats> altForms = new HashMap<>();
    private final String flatArmour;
    private final String hitpoints;
    private final String attackLevel;
    private final String strengthLevel;
    private final String defenceLevel;
    private final String magicLevel;
    private final String rangedLevel;
    private final String attackSpeed;

    public NPCStats(String name, String searchName, String elementalWeakness, String elementalPercent, String crushDefence, String stabDefence, String slashDefence, String standardDefence, String heavyDefence, String lightDefence, String magicDefence, Integer npcID, boolean alt, List<String> maxHits, List<String> attackStyles, String flatArmour, String hitpoints, String attackLevel, String strengthLevel, String defenceLevel, String magicLevel, String rangedLevel, String attackSpeed) {
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
        this.maxHits = maxHits;
        this.attackStyles = attackStyles;
        this.flatArmour = flatArmour;
        this.hitpoints = hitpoints;
        this.attackLevel = attackLevel;
        this.strengthLevel = strengthLevel;
        this.defenceLevel = defenceLevel;
        this.magicLevel = magicLevel;
        this.rangedLevel = rangedLevel;
        this.attackSpeed = attackSpeed;
    }

    public void addForm(NPCStats altForm) {
        String altName = altForm.getName().split("#",2)[1];
        altForms.put(altName, altForm);
    }

}

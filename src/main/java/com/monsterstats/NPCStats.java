package com.monsterstats;

import lombok.Getter;

@Getter
public class NPCStats {
    private final String name;
    private final String elementalWeakness;
    private final String elementalPercent;
    private final String crushDefence;
    private final String stabDefence;
    private final String slashDefence;
    private final String standardDefence;
    private final String magicDefence;
    private final String heavyDefence;
    private final String lightDefence;
    private final String combatLevel;

    public NPCStats(String name, String elementalWeakness, String elementalPercent, String crushDefence, String stabDefence, String slashDefence, String standardDefence, String heavyDefence, String lightDefence, String magicDefence, String combatLevel) {
        this.name = name;
        this.elementalWeakness = elementalWeakness;
        this.elementalPercent = elementalPercent;
        this.crushDefence = crushDefence;
        this.stabDefence = stabDefence;
        this.slashDefence = slashDefence;
        this.standardDefence = standardDefence;
        this.magicDefence = magicDefence;
        this.heavyDefence = heavyDefence;
        this.lightDefence = lightDefence;
        this.combatLevel = combatLevel;
    }

}

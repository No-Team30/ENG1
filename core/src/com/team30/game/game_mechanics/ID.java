package com.team30.game.game_mechanics;

enum IDType {
    Auber,
    Infiltrator,
    System,
    Npc,
    Unknown,
}

/**
 * A ID class used to uniquely identify every entity
 */
// TODO Needs work
// TODO Needs a pretty print
public class ID {
    static int idCount = 0;
    public final int ID;
    public final IDType type;

    public ID() {
        idCount += 1;
        this.ID = idCount;
        type = IDType.Unknown;
    }

    public ID(IDType type) {
        idCount += 1;
        this.ID = idCount;
        this.type = type;
    }
}
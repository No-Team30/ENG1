package com.team30.game.game_mechanics;


/**
 * A ID class used to uniquely identify every entity
 */
// TODO Needs work
// TODO Needs a pretty print
public class ID {
    public final int ID;
    private static int idCount = 1;
    public final EntityType type;

    public ID() {
        idCount += 1;
        this.ID = idCount;
        type = null;
    }

    public ID(EntityType type) {
        idCount += 1;
        this.ID = idCount;
        this.type = type;
    }
}
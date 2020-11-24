package com.team30.game.Recording;

import com.team30.game.game_mechanics.EntityType;
import com.team30.game.game_mechanics.ID;
import com.team30.game.game_mechanics.Infiltrators.InfiltratorType;

/**
 * Represents one action for a singular entity
 */
public class Action {
    private final EntityType entityType;
    private final ActionType actionType;
    private final ID id;
    private final ID target;
    // Can't use vectors because GSON breaks
    private final float xPosition;
    private final float yPosition;
    private final float xVelocity;
    private final float yVelocity;
    private final InfiltratorType infiltratorType;

    /**
     * Stores a game "action", i.e. something important that happened
     *
     * @param id         The ID of the entity
     * @param actionType The type of action that took place
     * @param xPosition  The current x position of the entity
     * @param yPosition  The current y position of the entity
     * @param xVelocity  The current x velocity (direction of travel) for the entity
     * @param yVelocity  The current y velocity (direction of travel) for the entity
     */
    public Action(ID id, ActionType actionType, float xPosition, float yPosition, float xVelocity, float yVelocity) {
        this.id = id;
        this.entityType = id.type;
        this.actionType = actionType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.target = null;
        this.infiltratorType = null;
    }

    /**
     * Stores a game "action", i.e. something important that happened
     * The target parameter can be used to identify a second entity, that is involved in this "action"
     *
     * @param id         The ID of the entity
     * @param actionType The type of action that took place
     * @param xPosition  The current x position of the entity
     * @param yPosition  The current y position of the entity
     * @param xVelocity  The current x velocity (direction of travel) for the entity
     * @param yVelocity  The current y velocity (direction of travel) for the entity
     * @param target     The id of an entity did something to another entity (
     */
    public Action(ID id, ActionType actionType, float xPosition, float yPosition, float xVelocity, float yVelocity, ID target) {
        this.id = id;
        this.entityType = id.type;
        this.actionType = actionType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.target = target;
        this.infiltratorType = null;
    }

    /**
     * Stores a game "action", i.e. something important that happened
     * This constructor also caters, for sub infiltrator types
     *
     * @param id              The ID of the entity
     * @param actionType      The type of action that took place
     * @param xPosition       The current x position of the entity
     * @param yPosition       The current y position of the entity
     * @param xVelocity       The current x velocity (direction of travel) for the entity
     * @param yVelocity       The current y velocity (direction of travel) for the entity
     * @param infiltratorType The sub infiltrator type for special abilities
     */
    public Action(ID id, ActionType actionType, float xPosition, float yPosition, float xVelocity, float yVelocity, InfiltratorType infiltratorType) {
        this.id = id;
        this.entityType = id.type;
        this.actionType = actionType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.target = null;
        this.infiltratorType = infiltratorType;
    }

    public ID getId() {
        return id;
    }

    public ID getTarget() {
        return target;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public float getXVelocity() {
        return xVelocity;
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public float getXPosition() {
        return xPosition;
    }

    public float getYPosition() {
        return yPosition;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public InfiltratorType getInfiltratorType() {
        return infiltratorType;
    }

    @Override
    public String toString() {
        return ("ID: " + getId() + "\nActionType: " + getActionType() + "\nEntityType: " + getEntityType() + "\nInfiltratorType: " + getInfiltratorType() + "\nX Position : " + getXPosition() + "\n Y Position: " + getYPosition());
    }
}

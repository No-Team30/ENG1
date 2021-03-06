package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.g2d.Batch;

public class StationSystem extends Entity {
    /**
     * The default health for any system
     */
    public static final int DEFAULT_HEALTH = 100;
    public final String type;
    /**
     * Used for scaling damage to the Auber, depending on how much health is remaining
     * TODO Also to set a limit for eventual repair of the systems
     */
    private final int maxHealth;
    private int health;
    /**
     * Whether the system has been destroyed
     */
    public boolean active;
    private float coolDown;
    public Integer pair;


    public StationSystem(String name, int xPos, int yPos, int width, int height, int health) {
        super(new ID(EntityType.StationSystem), null, xPos, yPos, width, height);
        // Currently using type to find system types (healing, teleportation)
        // TODO Switch to an enum eventually
        this.type = name;
        this.MAX_VELOCITY = 0f;
        this.VELOCITY_CHANGE = 0f;
        this.health = health;
        this.maxHealth = health;
        this.active = true;
        this.coolDown = 0.0f;
    }

    /**
     * If the system is not on cooldown, applies the given amount of damage<br>     *
     * And returns the remaining health
     * TODO Inform the auber which system is being damaged
     *
     * @param inflicted The amount of damage to be inflicted to the system
     * @return health The remaining health of the system
     */
    public int applyDamage(int inflicted) {
        if (this.coolDown > 0.0) {
            return this.health;
        }
        this.health = this.health - inflicted;
        System.out.println("Inflicting: " + inflicted + " to system: " + this.type + "now at: " + this.health);
        //TODO create sprite to warn Auber of system damage (play sound?)
        if (this.health <= 0) {
            this.active = false;
            this.health = 0;
        }
        this.coolDown = 10.0f;
        return health;
    }

    public float getCoolDown() {
        return this.coolDown;
    }

    /**
     * Removes the given amount of time from the cooldown
     *
     * @param delta The amount of time that has elapsed
     * @return If the cooldown has expired
     */
    public boolean updateCoolDown(float delta) {
        if (this.coolDown == 0) {
            return false;
        }
        this.coolDown -= delta;
        return this.coolDown == 0;
    }

    /**
     * Overridden and does nothing because the system images are currently in the base map layer (As of 14/11/2020)
     *
     * @param batch The sprite batch to render to
     */
    @Override
    public void draw(Batch batch) {

    }

    /**
     * @return The actual health of the system
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return Gets the maximum health of the system
     */
    public int getMaxHealth() {
        return maxHealth;
    }
}

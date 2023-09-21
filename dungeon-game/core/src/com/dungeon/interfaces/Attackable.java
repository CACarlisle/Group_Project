package com.dungeon.interfaces;

import com.badlogic.gdx.math.Vector2;

public interface Attackable{
    /**
     * Deal damage to an attackable entity.
     * @param damage the amount of damage being hit will inflict
     * @return true if damage was actually dealt
     */
    public boolean hit(int damage);

    /**
     * Method to knockback the attackable entity.
     */
    public void knockback();

    /**
     * Sets up the knockback of the attackable entity.
     * @param knockBackStrength
     * @param attackingVector
     */
    public void setupKnockBack(int knockBackStrength, Vector2 attackingVector);
}
package com.dungeon.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Weapon class, represents weapons.
 */
public class Weapon
{
    protected String name;
    protected int damage;
    protected int range;
    protected float span;
    protected float attackRate;
    private Texture weaponIcon;

    public Weapon(String name, int damage, int range, float span, float attackRate, String iconPath)
    {
        weaponIcon = new Texture(Gdx.files.internal(iconPath));
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.span = span;
        this.attackRate = attackRate;
    }
    /**
     * Accessor method for attackRate
     * @return the attackRate
     */
    public float getAttackRate(){
        return attackRate;
    }
    /**
     * Accessor method for weapon name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Accessor method for weapon attack damage
     * @return attack damage
     */
    public int getDamage() {
        return damage;
    }
    /**
     * Accessor method for weapon range
     * @return range
     */
    public int getRange() {
        return range;
    }
    /**
     * Accessor method for weapon span
     * @return span
     */
    public float getSpan() {
        return span;
    }

    /**
     * Method for determining whether an entity is within range of the weapon
     * @param attackFrom The vector that the attack is coming from
     * @param testPoint A vector to test from
     * @param attackDirection A vector representing the direction that the attack is going to.
     * @return Boolean representing whether the attack is in range or not
     */
    public boolean withinRange(Vector2 attackFrom, Vector2 testPoint, Vector2 attackDirection) {

        float distance = Vector2.dst(attackFrom.x, attackFrom.y, testPoint.x, testPoint.y);

        if (distance > range)
        {
            return false;
        }

        Vector2 dirToPoint = testPoint.sub(attackFrom).nor();
        float angle = attackDirection.angleDeg(testPoint);
        
        if (angle > 180) angle = 360 - angle;

        if (angle > span / 2)
        {
            return false;
        }

        return true;
    }
    /**
     * A method to set the weapon stats into a string, and returns it
     */
    @Override
    public String toString() {
        return name + " DMG: " + damage + " RANGE: " + range + " SPAN: " + span + " RATE: " + attackRate;
    }

    public void buffDamage(int amount) {
        damage += amount;
    }

    public void buffRange(int amount) {
        range += amount;
    }

    public void buffAttackRate(float amount) {
        if (attackRate - amount <= 0) return;
        attackRate -= amount;
    }

    public void buffSpan(float amount) {
        span += amount;
    }
    /**
     * Returns the icon of the weapon
     * @return the texture icon of the weapon
     */
    public Texture getIcon(){
        return weaponIcon;
    }
}

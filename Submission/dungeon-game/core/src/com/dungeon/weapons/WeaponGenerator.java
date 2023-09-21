package com.dungeon.weapons;

import java.util.Random;

/**
 * A class used to generate weapons randomly
 */
public final class WeaponGenerator
{
    private static Random r;
    private static float maxVariance = 0.1f; //Stats vary by 10% max

    public static Weapon getRandomWeapon()
    {
        if (r == null)
        {
            r = new Random();
        }

        Weapon newWeapon;

        int roll = r.nextInt(9);

        switch (roll)
        {
            case 0:
                newWeapon = getWeapon("Sword", 5, 100, 60, 0.5f,"weapons/sword.png");
                break;
            case 1:
                newWeapon = getWeapon("Short Sword", 4, 80, 55, 0.35f,"weapons/shortsword.png");
                break;
            case 2:
                newWeapon = getWeapon("Dagger", 2, 75, 65, 0.2f,"weapons/dagger.png");
                break;
            case 3:
                newWeapon = getWeapon("Great Sword", 10, 120, 80, 1.5f,"weapons/greatsword.png");
                break;
            case 4:
                newWeapon = getWeapon("Lance", 3, 250, 25, 0.5f,"weapons/lance.png");
                break;
            case 5:
                newWeapon = getWeapon("Axe", 8, 90, 100, 0.8f,"weapons/axe.png");
                break;
            case 6:
                newWeapon = getWeapon("Mace", 6, 100, 70, 0.6f,"weapons/mace.png");
                break;
            case 7:
                newWeapon = getWeapon("Battle Axe", 9, 100, 100, 1.2f,"weapons/battleaxe.png");
                break;
            default:
                newWeapon = getWeapon("Spear", 3, 200, 20, 0.45f,"weapons/spear.png");
                break;
        }

        //Debugger.log(newWeapon.toString());
        return newWeapon;
    }
    /**
     * Returns a weapon, with semi-randomised stats
     * @param name name of the weapon
     * @param targetDamage attack damage of the weapon
     * @param targetRange range of the weapon
     * @param targetSpan span of the weapon
     * @param attackRate attack rate of the weapon
     * @return A reference to the weapon
     */
    private static Weapon getWeapon(String name, int targetDamage, int targetRange, int targetSpan, float attackRate, String filePath)
    {
        int damage = (int) (targetDamage * (1 + randFloat(-maxVariance, maxVariance)));
        int range = (int) (targetRange * (1 + randFloat(-maxVariance, maxVariance)));
        int span = (int) (targetSpan * (1 + randFloat(-maxVariance, maxVariance)));
        return new Weapon(name, damage, range, span, attackRate,filePath);
    }
    /**
     * A method to generate a random value within a range
     * @param min minumum value of the range
     * @param max maximum value of the range
     * @return the float generated
     */
    private static float randFloat(float min, float max)
    {
        return r.nextFloat() * (max - min) + min;
    }
}
package com.dungeon.particles;

public final class ParticleUtility {
    
    /**
     * Shows a number particle at a given position.
     * @param value Value to be shown as a particle
     * @param x x position of the particle
     * @param y y position of the particle
     */
    public static void spawnDamageParticle(int value, float x, float y)
    {
        DamageParticle dp;
        int digit = 0;
        int i = 0;
        while(value > 0) {
            digit = value % 10;
            
            dp = new DamageParticle(digit, x - 12*i, y, 0.5f);

            value /= 10;
            i++;
        }
    }
    /**
     * Shows a number particle at a given position.
     * @param value Value to be shown as a particle
     * @param x x position of the particle
     * @param y y position of the particle
     */
    public static void spawnHealParticle(int value, float x, float y)
    {
        HealParticle hp;
        int digit = 0;
        int i = 1;
        while(value > 0) {
            digit = value % 10;
            
            hp = new HealParticle(digit, x - 12*i, y, 0.5f);

            value /= 10;
            i++;
        }

        hp = new HealParticle(43, x - 12*i + 4, y, 0.5f);
    }
}

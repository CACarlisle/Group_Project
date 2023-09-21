package com.dungeon.particles;

/**
 * A class used to display heal particles.
 */
public class HealParticle extends DigitParticle
{
    public HealParticle(int digit, float x, float y, float lifetime)
    {
        super(digit, x, y, lifetime);

        setParticleColour(0x5fb052ff);
    }
}

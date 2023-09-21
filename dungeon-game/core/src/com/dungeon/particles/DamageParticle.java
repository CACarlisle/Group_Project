package com.dungeon.particles;
/**
 * A class used to display damage particles
 */
public class DamageParticle extends DigitParticle
{
    public DamageParticle(int digit, float x, float y, float lifetime)
    {
        super(digit, x, y, lifetime);

        setParticleColour(0xda3c47ff);
    }
}

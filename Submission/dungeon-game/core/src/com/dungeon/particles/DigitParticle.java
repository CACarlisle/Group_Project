package com.dungeon.particles;

import com.dungeon.debug.*;

/**
 * A class used to display digit particles
 */
public class DigitParticle extends Particle{
    
    public DigitParticle(int digit, float x, float y, float lifetime)
    {
        super(x, y, lifetime);

        //Ensure digit is valid
        if ((digit > 9 || digit < 0) && digit != 43) { //43 is code for +
            Debugger.logWarning("Damage digit must be in the range 0-9. Value given was " + digit + "! Defaulted to 0");
            digit = 0;
        }

        setParticleTexture("damage-font/" + digit + ".png");
    }
}

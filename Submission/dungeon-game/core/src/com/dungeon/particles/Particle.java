package com.dungeon.particles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;

import com.dungeon.debug.Debugger;
/**
 * A class used to abstract creation of a particle
 */
public class Particle {
    private Sprite sprite;
    private Texture particleTexture;
    
    private float initialX;
    private float initialY;

    private float x;
    private float y;

    private float targetX;
    private float targetY;

    private float lifetime;
    private float elapsedTime = 0f;

    private boolean alive;
    /**
     * Constructor for particles
     * @param x the X location of the particle
     * @param y the Y location of the particle
     * @param lifetime the lifetime of the particle
     */
    public Particle(float x, float y, float lifetime)
    {
        //Set lifetime
        this.lifetime = lifetime;

        //Set sprite based given texture
        particleTexture = new Texture("misc/no_texture.png");
        sprite = new Sprite(particleTexture);
        
        //Set the initial location
        initialX = x;
        initialY = y;

        //Set the target location
        targetX = initialX;
        targetY = initialY + 10f;

        alive = true;

        ParticleManager.getInstance().addParticle(this);
    }
    /**
     * The update method for the particle
     */
    public void update()
    {
        if (alive) moveParticle();
        updateElapsedTime();
    }
    /**
     * A method used to move the particle
     */
    private void moveParticle()
    {
        float t = (elapsedTime / lifetime);

        if (t > 1f) t = 1f;

        x = initialX + (targetX - initialX) * t;
        y = initialY + (targetY - initialY) * t;
        sprite.setCenter(x, y);
    }
    /**
     * A method to update the elapsed time, and check whether the particle should still be alive
     */
    private void updateElapsedTime()
    {
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (elapsedTime >= lifetime) {
            alive = false;
            ParticleManager.getInstance().removeParticle(this);
        }
    }
    /**
     * An Accessor method to return the sprite
     * @return returns the sprite of the particle
     */
    public Sprite getSprite()
    {
        return sprite; 
    }
    /**
     * A method to set the colour of a particle
     * @param colour The colour
     */
    public void setParticleColour(Color colour)
    {
        sprite.setColor(colour);
    }
    /**
     * A method to set the colour of a particle
     * @param rgba The RGBA for the colour
     */
    public void setParticleColour(int rgba)
    {
        setParticleColour(new Color(rgba));
    }
    /**
     * A Mutator method setting the texture of a particle
     * @param path The path to the texture of the particle
     */
    public void setParticleTexture(String path)
    {
        try
        {
            particleTexture = new Texture(path);
            sprite.setTexture(particleTexture);
            setParticleSize();
        } catch(Exception e)
        {
            Debugger.logError("Texture " + path + " does not exist or cannot be loaded!");
        }
    }
    /**
     * A method to set the size of a particle
     */
    private void setParticleSize()
    {
        sprite.setSize(particleTexture.getWidth() * 2, particleTexture.getHeight() * 2);
    }
}

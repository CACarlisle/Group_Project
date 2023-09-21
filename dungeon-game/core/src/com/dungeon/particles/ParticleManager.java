package com.dungeon.particles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A class used to manage particles
 */
public class ParticleManager {

    private static ParticleManager particleManager;

    private ArrayList<Particle> particles;
    private ArrayList<Particle> toAdd;
    private ArrayList<Particle> toRemove;
    private SpriteBatch batch;
    /**
     * Returns the particle manager, or creates one if there is not currently one. (SINGLETON)
     * @return A reference to the particle manager 
     */
    public static ParticleManager getInstance()
    {
        if (particleManager == null) {
            particleManager = new ParticleManager();
        }

        return particleManager;
    }
    /**
     * Constructor for particle manager
     */
    private ParticleManager()
    {
        particles = new ArrayList<Particle>();
        toAdd = new ArrayList<Particle>();
        toRemove = new ArrayList<Particle>();

        
        batch = new SpriteBatch();
    }
    /**
     * A method to add a particle to the particle manager
     * @param dp The particle to add
     */
    public void addParticle(Particle dp)
    {
        toAdd.add(dp);
        //particles.add(dp);
    }
    /**
     * A method to remove a particle from the particle manager
     * @param dp the particle to remove
     */
    public void removeParticle(Particle dp)
    {
        toRemove.add(dp);
        //particles.remove(dp);
    }
    /**
     * A method to update all particles, and remove particles no longer needed
     */
    public void updateAll()
    {
        for (Particle dp : particles)
        {
            dp.update();
        }

        drawParticles();

        particles.addAll(toAdd);
        toAdd.clear();

        particles.removeAll(toRemove);
        toRemove.clear();
    }
    /**
     * A method to draw particles
     */
    public void drawParticles()
    {
        batch.begin();
        for (Particle dp : particles)
        {
            dp.getSprite().draw(batch);
        }
        batch.end();
    }
    /**
     * A method to return the list of particles
     * @return An ArrayList of particles
     */
    public ArrayList<Particle> getParticles()
    {
        return particles;
    }
}


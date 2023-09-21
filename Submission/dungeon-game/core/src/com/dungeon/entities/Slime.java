package com.dungeon.entities;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

/**
 * Class defining a Slime
 */
public class Slime extends MeleeEnemy {


    /**
     * Constructor for a slime
     * @param target A reference to the player
     */
    public Slime(Entity target, int health) {
        super(target);

        setMaxHealth(health);
        setHealth(maxHealth);

        Random r = new Random();
        setSpeed(100 + (r.nextInt(50)*2 - 80f));    // trying to randomise the movement
        attackStrength = 5;
        position = new Vector2(1500, 100);
        hitBox = new Rectangle();
        hitBox.setWidth(45f);
        hitBox.setHeight(33f);
        hitBox.setCenter(position);
        setAgroRange(300);
        attackSound = Gdx.audio.newSound((Gdx.files.internal("audio/slimeAttack1.wav")));
    }
}
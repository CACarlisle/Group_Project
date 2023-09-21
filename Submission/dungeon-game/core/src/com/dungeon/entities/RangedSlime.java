package com.dungeon.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 * Creates a ranged slime
 */
public class RangedSlime extends RangedEnemy {
    // DEFAULT VALUES
    protected float speed = 1;
    private Sound rangedSlimeAttackNoise;


    public RangedSlime(Entity target, int health) {
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
        setAgroRange(350);

        rangedSlimeAttackNoise = Gdx.audio.newSound((Gdx.files.internal("audio/rangedSlimeAttack1.wav")));
    }


    @Override
    public void attack() {
        super.attack();
        if(attackCooldown == 2) rangedSlimeAttackNoise.setVolume(rangedSlimeAttackNoise.play(), 0.18f);
    }
}

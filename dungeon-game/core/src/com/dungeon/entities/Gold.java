package com.dungeon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.dungeon.animation.Animator;

import java.util.Random;

/**
 * A class creating and animating gold
 */
public class Gold extends Entity {
    protected int value;

    private Animator goldIdleAnimatior;
    private Sound coinPickupSound;

    private boolean collectable;

    private float elapsed;
    private float time;
    private Vector2 a;
    private Vector2 b;

    public Gold(int value, Vector2 start)
    {
        super();

        Random r = new Random();

        position = start;
        a = start;
        b = new Vector2(a).add(new Vector2(2 * r.nextFloat() -1, 2 * r.nextFloat() - 1).nor().scl(25f));

        time = 0.2f;
        elapsed = time;

        isInvincible = true;
        hitBox = new Rectangle(0, 0, 16, 16);
        this.value = value;

        setSpriteTexture("misc/gold.png");
        sprite.setSize(16, 16);

        goldIdleAnimatior = new Animator("animations/gold_anim.png", 4, 8, 8, 0.5f, true, false);
        coinPickupSound = Gdx.audio.newSound((Gdx.files.internal("audio/coinPickup1.wav")));
    }

    @Override
    public void update() {
        super.update();
        goldIdleAnimatior.animate(this);

        if (elapsed > 0) arcMove();
        else collectable = true;
    }

    public void arcMove() {
        float t = 1 - (elapsed / time);

        setX(a.x + (b.x - a.x) * t);
        setY(a.y + (b.y - a.y) * t);

        elapsed -= Gdx.graphics.getDeltaTime();
    }

    /**
     * A method to collect gold, and add it to the players total gold.
     */
    public void collect()
    {
        if (!collectable) return;

		coinPickupSound.setVolume(coinPickupSound.play(), 0.5f);
        EntityManager.player.addGold(value);
        kill();
    }
}

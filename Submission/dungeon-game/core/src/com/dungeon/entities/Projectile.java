package com.dungeon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.dungeon.interfaces.Attacker;
import com.dungeon.interfaces.Moveable;
import com.dungeon.misc.Utilities;

import com.dungeon.animation.*;

/**
 * A class defining a Projectile
 */
public class Projectile extends Entity implements Attacker, Moveable {

    private Entity target;
    private Vector2 normVector;
    private Vector2 attackingVector;
    private int attackStrength;

    private Animator projectileAnimator;

    /**
     * Constructor for a projectile
     * @param projectileTarget A reference to the target (player)
     * @param startPosition The starting position of the projectile
     * @param normalizedVector A normalized Vector determining the direction of the projectile
     * @param attackPower The attack power of the projectile
     */
    public Projectile(Entity projectileTarget, Vector2 startPosition, Vector2 normalizedVector,int attackPower){
        super();
        attackStrength = attackPower;
        target = projectileTarget;
        normVector = normalizedVector;
        position = startPosition;
        hitBox = new Rectangle();
        hitBox.setWidth(16);
        hitBox.setHeight(16);
        hitBox.setCenter(position);
        setSpeed(200);
        setSpriteTexture("misc/no_texture.png");
        sprite.setSize(16,16);
        isInvincible = true;

        projectileAnimator = new Animator("animations/slimeball.png", 4, 8, 8, 0.4f, true, false);
    }



    /**
     * Attack method for Projectile's
     */
    @Override
    public void attack() {
        if(hitBox.overlaps(target.hitBox)) {
            attackingVector = new Vector2(target.getPosition().sub(position));
            attackingVector.nor();
            target.hit(attackStrength);
            this.kill();
        }
    }


    /**
     * Update method for Projectiles, allowing it to move and attack
     */
    @Override
    public void update(){
        super.update();
        move();
        attack();

        projectileAnimator.animate(this);
    }


    /**
     * Movement method for projectile, moving it and killing it once it reaches the side of the screen
     */
    @Override
    public void move() {
        setX((getX() + (normVector.x*getSpeed() * Gdx.graphics.getDeltaTime())));
        setY((getY() + (normVector.y*getSpeed() * Gdx.graphics.getDeltaTime())));

        if(getX() < 100) this.kill();
        if(getX() > Utilities.SCREEN_WIDTH - 100) this.kill();
        if(getY() < 40) this.kill();
        if(getY() > Utilities.SCREEN_HEIGHT - 250) this.kill();
    }
}

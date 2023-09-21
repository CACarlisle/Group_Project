package com.dungeon.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

import com.dungeon.animation.*;

/**
 * Class defining methods of a melee enemy
 */
public abstract class MeleeEnemy extends Enemy {

    private Vector2 attackingVector;
    private float attackCooldown;
    private float elapsedCooldown = 0;
    /**
     * Constructor for MeleeEnemy's 
     * @param target A reference to the player
     */
    public MeleeEnemy(Entity target) {
        super();
        this.target = target;
        
        attackCooldown = 0.5f;

        enemySAnim.setStateAnimator("Jump", new Animator("animations/slime_blue_jump.png", 8, 32,32, 0.8f, true, true));
        enemySAnim.setStateAnimator("Attack",new Animator("animations/slime_blue_attack.png", 2, 32,32, 0.2f, false, true));
        enemySAnim.setStateAnimator("Die",  new Animator("animations/slime_blue_die.png", 4, 32,32, deathTime, false, true));
    }

    /**
     * Method to move the melee enemy to the player
     */
    protected void move() {
        if (isDead) return;
        if (elapsedCooldown > 0) return;
        moveTowards(target.getPosition(), speed);
    }

    public void update()
    {
        super.update();
        if (attackCooldown > 0)
            attackCooldown = attackCooldown - Gdx.graphics.getDeltaTime();
        elapsedCooldown -= Gdx.graphics.getDeltaTime();
    }

    /**
     * Method to allow the Melee Enemy to attack the player
     */
    @Override
    public void attack() {
        if(hitBox.overlaps(target.hitBox) && attackCooldown <= 0) {
            enemySAnim.switchState("Attack");
            attackingVector = new Vector2(target.getPosition().sub(position));
            attackingVector.nor();
            boolean hit = target.hit(getAttackStrength());
            if(hit) attackSound.setVolume(attackSound.play(), 0.18f);
            target.setupKnockBack(50, attackingVector);

            elapsedCooldown = attackCooldown;
        }
    }

    /**
     * Method to kill an entity
     */
    @Override
    public void kill(float time) {
        super.kill(time);
        attackCooldown = 100;
    }
}

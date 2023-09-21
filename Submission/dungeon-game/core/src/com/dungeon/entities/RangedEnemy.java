package com.dungeon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.dungeon.animation.*;
import com.dungeon.misc.Utilities;

/**
 * A class defining a ranged enemy
 */
public abstract class RangedEnemy extends Enemy{

    protected float attackCooldown;
    private Boolean attacking = false;
    private float moveCooldown;
    

    /**
     * Constructor for ranged enemies
     * @param target A reference to the player
     */
    public RangedEnemy(Entity target)
    {
        super();
        this.target = target;

        enemySAnim.setStateAnimator("Jump", new Animator("animations/slime_green_jump.png", 8, 32,32, 0.8f, true, true));
        enemySAnim.setStateAnimator("Attack", new Animator("animations/slime_green_attack.png", 3, 32,32, 0.3f, false, true));
        enemySAnim.setStateAnimator("Die", new Animator("animations/slime_green_die.png", 4, 32,32, deathTime, false, true));

    }
    
    /**
     * Method to update the ranged enemy
     */
    @Override
    public void update(){
        super.update();
        if (attackCooldown > 0)
            attackCooldown = attackCooldown - Gdx.graphics.getDeltaTime();

        if (moveCooldown > 0)
            moveCooldown -= Gdx.graphics.getDeltaTime();
    }

    /**
     * Method to allow ranged enemies to move
     */
    protected void move() {
        if (isDead) return;
        if (moveCooldown > 0) return;

        //Using unnormalized vector to check for how close enemy is to player
        Vector2 unnormalizedVector = new Vector2(target.getX()-getX(), target.getY()-getY());

        if(unnormalizedVector.len() > 200 || unnormalizedVector.len() < -200){
            //MOVE CLOSER SO 200 AWAY
            moveTowards(target.getPosition(), speed);   // Move towards target
            attacking = false;
        }
        else if(unnormalizedVector.len() < 150 || unnormalizedVector.len() < -150) {
            //MOVE AWAY IF TOO CLOSE
            moveTowards(target.getPosition(), -speed);  // Move away from target
            attacking = false;
        }
        else {
            attacking = true;
        }
        if ((getX() < 120) || (getX() > Utilities.SCREEN_WIDTH - 120) || (getY() < 60) || (getY() > Utilities.SCREEN_HEIGHT - 270)){
            attacking = true;
        }
    }

    /**
     * Method allowing ranged enemies to attack
     */
    @Override
    public void attack() {
        if(attackCooldown <= 0 && attacking == true)
        {   
            enemySAnim.switchState("Attack");
            Vector2 unnormalizedVector = new Vector2(target.getX()-getX(), target.getY()-getY());
            Vector2 norm = (new Vector2(target.getX()-getX(), target.getY()-getY())).nor();
            if(unnormalizedVector.len() <= 200 || unnormalizedVector.len() <= 200){
                //CREATE PROJECTILE ENTITY, BLOCK CREATION OF PROJECTILE FOR A SHORT PERIOD
                new Projectile(target, new Vector2(position),new Vector2(norm),getAttackStrength()); 

                attackCooldown = 2;
                moveCooldown = 1;
            }
        }
        //IF VECTOR LENGTH IS CLOSE ENOUGH, ALLOW CREATION OF A PROJECTILE
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


package com.dungeon.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import com.dungeon.animation.*;
import com.dungeon.interfaces.Attacker;
import com.dungeon.misc.Utilities;


/**
 * An abstract class defining an Enemy
 */
public abstract class Enemy extends Entity implements Attacker {

	protected EnemyAnimation enemyAnimation;
    protected StateAnimator enemySAnim;
    protected Entity target;
    protected int agroRange;
    private float randomDirectionCooldown = 0;
    private Random r;
    private int stationary;
    private Vector2 randomDirection = new Vector2();
    protected int attackStrength = 0;
    protected static int totalEnemiesKilled = 0;
    protected Sound attackSound;
    
    public Enemy()
    {
        super();
        r = new Random();

        deathTime = 0.3f;

        enemySAnim = new StateAnimator(this);
        enemySAnim.addState("Jump", null, false);
		enemySAnim.addState("Attack", null, true);
		enemySAnim.addState("Die", null, false);

        
    }

    @Override
    public void update()
    {
        super.update();
 
        if(canMove) move();
        else {
            randomlyMove();
            checkIfAgro();
        }
        attack();
        //enemyAnimation.animate(this);

        if(getX() < 100) setX(100);
        if(getX() > Utilities.SCREEN_WIDTH - 100) setX(Utilities.SCREEN_WIDTH - 100f);
        if(getY() < 40) setY(40);
        if(getY() > Utilities.SCREEN_HEIGHT - 250) setY(Utilities.SCREEN_HEIGHT - 250f);

        enemySAnim.animate();
    }

    protected abstract void move();

    /**
     * Method to kill an entity
     */
    @Override
    public void kill(float time) {
        if (!isDead)
        {
            totalEnemiesKilled += 1;

            for (int i=0; i< 3 + r.nextInt(4); i++) {
                new Gold(1, getPosition());
            }
        }

        enemySAnim.switchState("Die");

        super.kill(time);
    }


    /**
     * @param agroRange the agroRange to set
     */
    public void setAgroRange(int agroRange) {
        this.agroRange = agroRange;
    }


    /**
     * @return the agroRange
     */
    public int getAgroRange() {
        return agroRange;
    }


    private void checkIfAgro() {
        float distance = target.getPosition().sub(getPosition()).len();
        if(distance < getAgroRange()) canMove = true;
    }

    /**
     * Randomly moves enemies so that they don't just stand still when the player is not in aggro range.
     */
    private void randomlyMove() {
        
        if (randomDirectionCooldown > 0) randomDirectionCooldown = randomDirectionCooldown - Gdx.graphics.getDeltaTime();
        else{
            randomDirection.setToRandomDirection().nor();
            stationary = r.nextInt(2);  //Generates random number between 1 and 0
            randomDirectionCooldown = 0.5f;
        }
        //Code to randomly move enemies
        if (stationary == 0){
            setX(getX() + randomDirection.x * speed * Gdx.graphics.getDeltaTime());
            setY(getY() + randomDirection.y * speed * Gdx.graphics.getDeltaTime());
            setFacing(randomDirection);
        }
    }
    /**
     * Mutator method to set attack strength
     * @param strength the integer attack strength
     */
    public void setAttackStrength(int strength){
        attackStrength = strength;
    }
    /**
     * Accessor method for attack strength
     * @return returns the attack strength as an integer
     */
    public int getAttackStrength(){
        return attackStrength;
    }


    /**
     * @return the totalEnemiesKilled
     */
    public static int getTotalEnemiesKilled() {
        return totalEnemiesKilled;
    }

    /**
     * @param totalEnemiesKilled the totalEnemiesKilled to set
     */
    public static void setTotalEnemiesKilled(int totalEnemiesKilled) {
        Enemy.totalEnemiesKilled = totalEnemiesKilled;
    }

    
    /**
     * Disposes of the entity
     */
    @Override
    public void dispose() {
    }
}
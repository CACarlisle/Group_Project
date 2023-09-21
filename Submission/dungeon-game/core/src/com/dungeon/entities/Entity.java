package com.dungeon.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.dungeon.interfaces.Attackable;
import com.dungeon.misc.Utilities;
import com.dungeon.particles.*;

/**
 * An abstract class defining an Entity
 */
public abstract class Entity implements Attackable {

    protected Sprite sprite;
    protected Sprite hpbar;
    protected Sprite hpbarFill;

    private Texture hpbarFillTexture;
    private TextureRegion hpbarFillRegion;

    protected int id;
    protected Vector2 position;
    protected float speed;
	protected Rectangle hitBox;
    protected float health;
    protected float maxHealth;
    protected int facing;
    protected Boolean canMove;
    protected Boolean isKnockedBack;
    protected int knockBackStrength;
    protected int knockBackCount;
    protected Vector2 knockBackDirection;
    protected boolean isInvincible;

    protected boolean isDead;
    private float timeToLive;
    protected float deathTime = 0f;

    /**
     * Constructor
     */
    public Entity() {
        EntityManager.getInstance().addEntity(this);
        id = EntityManager.getInstance().getNewID();
        facing = Utilities.RIGHT;

        maxHealth = 10;
        health = maxHealth;
        canMove = false;
        isKnockedBack = false;
        isInvincible = false;
        isDead = false;

        sprite = new Sprite(new Texture("misc/no_texture.png"));
        sprite.setSize(64,64);

        hpbar = new Sprite(new Texture("ui/enemy_hpbar_bg.png"));
        hpbar.setSize(48, 8);

        hpbarFillTexture = new Texture ("ui/enemy_hpbar_fill.png");
        hpbarFillRegion = new TextureRegion(hpbarFillTexture, 0, 0, hpbarFillTexture.getWidth(), hpbarFillTexture.getHeight()); 

        hpbarFill = new Sprite(hpbarFillRegion);
        hpbarFill.setSize(48, 8);
    }


    /**
     * Set sprite texture.
     * @param path path to the texture
     */
    public void setSpriteTexture(String path)
    {
        setSpriteTexture(new Texture(path));
    }

    /**
     * Set sprite texture.
     * @param texture texture to set
     */
    public void setSpriteTexture(Texture texture)
    {
        sprite.setTexture(texture);
    }

    /**
     * Set sprite texture.
     * @param texture textureRegion to set
     */
    public void setSpriteTexture(TextureRegion texture)
    {
        sprite.setRegion(texture);
    }


    /**
     * Method that should be called every update (every frame).
     * This method should handle anything that the entity does on a frame-by-frame basis e.g. moving.
     */
    public void update()
    {
        sprite.setCenter(position.x, position.y);
        hpbar.setCenter(position.x, position.y - sprite.getHeight()/2);
        hpbarFill.setCenter(position.x - (hpbar.getWidth() - hpbarFill.getWidth())/2, position.y - sprite.getHeight()/2);
        
        if (isDead) {
            timeToLive -= Gdx.graphics.getDeltaTime();
            if (timeToLive <= 0) kill();
        }
    }

    /**
     * Draws the sprite and its health bar.
     * @param batch
     */
    public void draw(SpriteBatch batch)
    {
        sprite.draw(batch);
        if (this != EntityManager.player && isDamaged() && !isDead) {
            hpbar.draw(batch);
            hpbarFill.draw(batch);
        }
    }


    /**
     * Method that is called to kill the entity.
     * Should be called when the entity health reaches 0
     */
    public void kill()
    {
        //Debugger.logInfo(this.getClass().getSimpleName() + " id:" + getIDString() + " has been killed");
        EntityManager.getInstance().removeEntity(this);
    }

    public void kill(float time){
        if (isDead) return;
        isDead = true;
        timeToLive = time;
    }

    /**
     * Deal damage to this entity, update health bar, and show damage particle.
     * If health reaches 0, kill this entity.
     * @param damage the amount of damage being hit will inflict
     */
    @Override
    public boolean hit(int damage) {
        health -= damage;
        ParticleUtility.spawnDamageParticle(damage, position.x, position.y);
        updateHealthbar();
        if (health <= 0) kill(deathTime);
        return true;
    }

    /**
     * Increases health by amount given, spawns heal particle, and updates health bar.
     * @param toHeal
     */
    public void heal(int toHeal) {
        health += toHeal;
        if (health > maxHealth) health = maxHealth;

        ParticleUtility.spawnHealParticle(toHeal, position.x, position.y);
        updateHealthbar();
    }

    @Override
    public void knockback() {
        if(knockBackCount == 0){
            canMove = true;
            isKnockedBack = false;
        }
        setX(getX() + knockBackDirection.x * (knockBackStrength/5));
        setY(getY() + knockBackDirection.y * (knockBackStrength/5));
        knockBackCount--;
    }


    /**
     * Moves towards the targets location.
     * @param targetLocation target's location
     * @param moveSpeed the speed of entity
     */
    public void moveTowards(Vector2 targetLocation, float moveSpeed)
    {
        Vector2 direction = targetLocation.sub(getPosition()).nor();
        setX(getX() + direction.x * moveSpeed * Gdx.graphics.getDeltaTime());
        setY(getY() + direction.y * moveSpeed * Gdx.graphics.getDeltaTime());

        setFacing(direction);
    }


    /**
     * @return the hitBox
     */
    public Rectangle getHitBox() {
        return hitBox;
    }

    /**
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Accessor method to get x
     * @return returns the value of x
     */
    public float getX(){
        return position.x;
    }

    /**
     * Mutator method for X
     * @param newX value to set X to
     */
    public void setX(float newX){
        position.x = (float) newX;
        hitBox.setCenter((float)newX, (float)getY());
    }

    /**
     * Accessor method to get y
     * @return returns the value of y
     */
    public float getY(){
        return position.y;
    }

    /**
     * Mutator method for Y
     * @param newY value to set Y to
     */
    public void setY(float newY){
        position.y = (float) newY;
        hitBox.setCenter((float)getX(), (float)newY);
    }

    /**
     * Accessor method to get the vector position
     * @return the position vector
     */
    public Vector2 getPosition() {
        return new Vector2(position);
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vector2 position) {
        this.position = position;
        hitBox.setCenter(this.position);
    }

    /**
     * Accessor method for health.
     * @return returns entities health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Mutator method for health.
     * @param healthValue float value to set health to
     */
    public void setHealth(float healthValue) {
        this.health = healthValue;
        clampHealth();
    }

    /**
     * @param maxHealth max health to set
     */
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Modify health.
     * @param modifyBy amount to change health by
     */
    public void modifyHealth(float modifyBy) {
        this.health += modifyBy;
        clampHealth();
    }

    /**
     * Stops health going above max health.
     */
    private void clampHealth() {
        if (health > maxHealth && maxHealth != 0) health = maxHealth;
    }

    /**
     * Gets direction Entity is facing.
     * @return direction facing
     */
    public int getFacing() {
        return facing;
    }

    public void setFacing(Vector2 direction) {
        if (direction.x > 0) facing = Utilities.RIGHT;
        else if(direction.x < 0) facing = Utilities.LEFT;
    }

    /**
     * Get the entity's ID.
     * @return ID as a number
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the ID as a string.
     * @return string ID
     */
    public String getIDString()
    {
        return String.format("%04d", id);
    }

    /**
     * Get whether the entity can move.
     * @return
     */
    public Boolean isMoveable()
    {
        return canMove;
    }

    /**
     * A method to set up knockback for an entity
     */
    @Override
    public void setupKnockBack(int knockBackStrength, Vector2 attackingVector) {
        this.isKnockedBack = true;
        this.knockBackCount = 6;
        canMove = false;
        isKnockedBack = true;
        this.knockBackDirection = attackingVector;
        this.knockBackStrength = knockBackStrength;
    }

    /**
     * @param isKnockedBack the isKnockedBack to set
     */
    public void setIsKnockedBack(Boolean isKnockedBack) {
        this.isKnockedBack = isKnockedBack;
    }

    /**
     * @return if the entity is in the process of being knocked back
     */
    public Boolean getIsKnockedBack() {
        return isKnockedBack;
    }

    /**
     * @return the knockback strength
     */
    public int getKnockBackStrength() {
        return knockBackStrength;
    }

    public boolean isDamaged() {
        return health < maxHealth; 
    }

    private void updateHealthbar()
    {
        int newWidth = (int) (hpbarFillTexture.getWidth() * (health/maxHealth));
        hpbarFillRegion = new TextureRegion(hpbarFillTexture, 0, 0, newWidth, hpbarFillTexture.getHeight());
        hpbarFill.setRegion(hpbarFillRegion);
        hpbarFill.setSize(newWidth * 2, 8);
    }

    /**
     * Accessor method for if the entity is invincible.
     * @return gets whether the entity is invincible
     */
    public boolean getIsInvincible(){
        return isInvincible;
    }

    /**
     * Method that should be called when the program is closed.
     */
    public void dispose() {}
}

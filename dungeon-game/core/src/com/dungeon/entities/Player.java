package com.dungeon.entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import com.dungeon.animation.*;
import com.dungeon.interfaces.Attackable;
import com.dungeon.interfaces.Attacker;
import com.dungeon.misc.InputManager;
import com.dungeon.misc.Utilities;
import com.dungeon.shop.*;
import com.dungeon.ui.UI;
import com.dungeon.weapons.Weapon;
import com.dungeon.weapons.WeaponGenerator;

/**
 * A class defining the Player
 */
public class Player extends Entity implements Attacker {

    private int gold;
    private PlayerAnimation playerAnimation;
	private boolean playAttackingAnimation;

    private StateAnimator playerStateAnimator;
    private StateAnimator playerStateAnimatorDown;
    private StateAnimator playerStateAnimatorUp;

    private Sound swordAttackSound;
    private Sound spearAttackSound;

    private Sound hurtSounds[];
    private final int numOfHurtSounds = 3;
    private Random r;

    private Sprite cooldownbar;
    private Sprite cooldownbarFill;
    private Texture cooldownbarFillTexture;
    private TextureRegion cooldownbarFillRegion;

    protected static int totalDamageDealt = 0;
    private float invincibilityFrames = 0;
    private float attackCooldown = 0;
    private float cooldownFrom;

    private Weapon weaponL;
    private Weapon weaponR;
    private int mouseClick;

    private SpriteBatch batch;
    private Sprite eInteract;

    private float animPlaying;


    //INVENTORY WHEN MADE

    public Player(SpriteBatch batch){
        super();
        setMaxHealth(100);
        setHealth(maxHealth);
        setGold(0);
        setSpeed(192);
        this.batch = batch;
        canMove = true;
        playAttackingAnimation = false;
        mouseClick = Utilities.DOWN;
        playerAnimation = new PlayerAnimation();
        eInteract = new Sprite(new Texture(Gdx.files.internal("misc/E.png")));
        eInteract.setSize(32, 32);

        swordAttackSound = Gdx.audio.newSound(Gdx.files.internal("audio/swing.ogg"));
        spearAttackSound = Gdx.audio.newSound(Gdx.files.internal("audio/swing2.ogg"));

        r = new Random();
        hurtSounds = new Sound[numOfHurtSounds];
        for (int i = 0; i < hurtSounds.length; i++) {
            hurtSounds[i] = Gdx.audio.newSound(Gdx.files.internal("audio/hurtSound"+ (i+1) +".wav"));
        }

        /* Sets the starting position of the player and puts a hit box around them */
        position = new Vector2(50, Utilities.SCREEN_HEIGHT/2-35);
        hitBox = new Rectangle();
        hitBox.setWidth(32);
        hitBox.setHeight(64);
        hitBox.setCenter(position);

        weaponL = WeaponGenerator.getRandomWeapon();
        weaponR = WeaponGenerator.getRandomWeapon();
        while (true){
            if(weaponL.getName() == weaponR.getName()) weaponR = WeaponGenerator.getRandomWeapon();
            else break;
        }

        cooldownbar = new Sprite(new Texture("ui/enemy_hpbar_bg.png"));
        cooldownbar.setSize(48,8);
        
        cooldownbarFillTexture = new Texture("ui/player_cooldown_bar_fill.png");
        cooldownbarFillRegion = new TextureRegion(cooldownbarFillTexture, 0, 0, cooldownbarFillTexture.getWidth(), cooldownbarFillTexture.getHeight());

        cooldownbarFill = new Sprite(cooldownbarFillRegion);
        cooldownbarFill.setSize(48,8);

        
        sprite.setSize(80,80);
        playerStateAnimator = new StateAnimator(this);
        playerStateAnimator.addState("Walk", new Animator("animations/player_walk.png", 4, 32, 32, 0.6f, true, true), false);
        playerStateAnimator.addState("Idle", new Animator("animations/player_idle.png", 2, 32, 32, 0.3f, true, true), false);
        playerStateAnimator.addState("Attack", new Animator("animations/player_attack.png", 3, 32, 32, 0.45f, false, true), true);

        playerStateAnimatorDown = new StateAnimator(this);
        playerStateAnimatorDown.addState("Walk", new Animator("animations/player_walk_down.png", 4, 32, 32, 0.6f, true, true), false);
        playerStateAnimatorDown.addState("Idle", new Animator("animations/player_idle_down.png", 2, 32, 32, 0.3f, true, true), false);
        playerStateAnimatorDown.addState("Attack", new Animator("animations/player_attack_down.png", 3, 32, 32, 0.45f, false, true), true);

        playerStateAnimatorUp = new StateAnimator(this);
        playerStateAnimatorUp.addState("Walk", new Animator("animations/player_walk_up.png", 4, 32, 32, 0.6f, true, true), false);
        playerStateAnimatorUp.addState("Idle", new Animator("animations/player_idle_up.png", 2, 32, 32, 0.3f, true, true), false);
        playerStateAnimatorUp.addState("Attack", new Animator("animations/player_attack_up.png", 3, 32, 32, 0.45f, false, true), true);
    }
    @Override
    public void heal(int toHeal){
        super.heal(toHeal);
        UI.getInstance().renderHealth(health);
    }
    @Override
    public void update() {
        super.update();

        if( animPlaying > 0) animPlaying -= Gdx.graphics.getDeltaTime();

        cooldownbar.setCenter(position.x, position.y - sprite.getHeight()/2);
        cooldownbarFill.setCenter(position.x - (cooldownbar.getWidth() - cooldownbarFill.getWidth())/2, position.y - sprite.getHeight()/2);

        if(getIsKnockedBack()) knockback();
        if (canMove) move();
        attack();
        checkHitbox();
        //playerAnimation.animate(this);
        
        if (facing == Utilities.UP) {
            playerStateAnimatorUp.animate();
        }
        else if(facing == Utilities.DOWN) {
            playerStateAnimatorDown.animate();
        }
        else {
            playerStateAnimator.animate();
        }

        if (invincibilityFrames > 0){
            invincibilityFrames= invincibilityFrames-Gdx.graphics.getDeltaTime();
        }
        if (attackCooldown > 0){
            updateCooldownBar();
            attackCooldown= attackCooldown-Gdx.graphics.getDeltaTime();
        }

        if(getX() < 100) setX(100);
        if(getX() > Utilities.SCREEN_WIDTH - 100) setX(Utilities.SCREEN_WIDTH - 100f);
        if(getY() < 40) setY(40);
        if(getY() > Utilities.SCREEN_HEIGHT - 250) setY(Utilities.SCREEN_HEIGHT - 250f);

    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);

        if (!isDead && attackCooldown > 0){
            cooldownbar.draw(batch);
            cooldownbarFill.draw(batch);
        }
    }


    /**
     * Deal damage to this player.
     * @param damage the amount of damage being hit will inflict
     */
    @Override
    public boolean hit(int damage) {
        if (invincibilityFrames<= 0){
            if ((health-damage) < 0) health = 0;
            else health -= damage;
            UI.getInstance().renderHealth(health);
            playHurtSound();
            return true;
        }
        return false;
    }

    /**
     * Plays a hurt sound.
     */
    public void playHurtSound() {
        int hurtSound = r.nextInt(numOfHurtSounds);
        hurtSounds[hurtSound].setVolume(hurtSounds[hurtSound].play(), 0.18f);
    }


    /**
     * Draws an 'E' next to the player.
     * @param color the colour you want to set the 'E'
     */
    public void updateAndDrawEInteract(Color color) {
        eInteract.setCenter(getX(), getY()+64);
        eInteract.setColor(color);
        eInteract.draw(batch);
    }


    /**
     * @return the gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @param gold the gold to set
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Adds gold to the player's gold.
     * @param toAdd gold to add
     */
    public void addGold(int toAdd) {
        this.gold += toAdd;
    }


    @Override
    public void setupKnockBack(int knockBackStrength, Vector2 attackingVector) {
        if (invincibilityFrames<= 0){
            this.isKnockedBack = true;
            this.knockBackCount = 6;
            canMove = false;
            isKnockedBack = true;
            this.knockBackDirection = attackingVector;
            this.knockBackStrength = knockBackStrength;
            invincibilityFrames = 0.3f;
        }
    }


    /**
     * Method for allowing the player to attack
     */
    @Override
    public void attack()
    {
        Weapon attackWith = null;
        if (attackCooldown <= 0) {
            
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                swordAttackSound.setVolume(swordAttackSound.play(), 0.6f);
                mouseClick = Utilities.LEFT;
                attackWith = weaponL;
            }
            else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                spearAttackSound.setVolume(spearAttackSound.play(), 0.6f);
                mouseClick = Utilities.RIGHT;
                attackWith = weaponR;
            }
            else {
                return;
            }

            playerStateAnimator.switchState("Attack");
            playerStateAnimatorDown.switchState("Attack");
            playerStateAnimatorUp.switchState("Attack");
            animPlaying = 0.45f;
        }

        Vector2 attackDirection = InputManager.getMouseVector(position);
        setAnimationAttackDirection(attackDirection);

        ArrayList<Entity> entities = EntityManager.getInstance().getEntities();
        ArrayList<Entity> toHit = new ArrayList<Entity>();  //Separate collection to avoid ConcurrentModException
        if (attackCooldown <= 0)
        {
            playAttackingAnimation = true;
            playerAnimation.restartAttackAnimation();
            for (Entity e : entities)
            {
                if(e == this) continue;

                if (attackWith.withinRange(getPosition(), e.getPosition(), attackDirection) && e instanceof Attackable && !e.isInvincible)
                {
                    //Debugger.log("\t--- " + e.getClass().getSimpleName() + " id:" + e.getIDString() +  " at " + e.getPosition() + " gets hit with "+ attackWith.getName() +" for "+ attackWith.getDamage() +" damage!");
                    toHit.add(e);
                    totalDamageDealt += attackWith.getDamage();
                }
            }

            for (Entity e : toHit)
            {
                e.hit(attackWith.getDamage());
            }
            
            cooldownFrom = attackWith.getAttackRate();
            attackCooldown = cooldownFrom;
        }
    }

    /**
     * Moves the player if any of the WASD keys a being pressed.
     */
    public void move() {
        Vector2 moveVector = InputManager.getInputVector();

        //Check animations
        if (moveVector.x == 0 && moveVector.y == 0) {   //IDLE
            switchAnim("Idle");
        }
        else {
            switchAnim("Walk"); 
        }

        setFacing(moveVector);

        setX(getX() + moveVector.x * speed * Gdx.graphics.getDeltaTime());
        setY(getY() + moveVector.y * speed * Gdx.graphics.getDeltaTime());
    }


    /**
     * Gets weapon equiped on left mouse.
     * @return left weapon
     */
    public Weapon getEquippedL() {
        return weaponL;
    }

    /**
     * Gets weapon equiped on right mouse.
     * @return right weapon
     */
    public Weapon getEquippedR() {
        return weaponR;
    }

    /**
     * Checks the hitbox of the player, checking if there are entities within the hitbox of the player
     */
    public void checkHitbox()
    {
        ArrayList<Entity> entities = EntityManager.getInstance().getEntities();
        ArrayList<Gold> toCollect = new ArrayList<Gold>();  //Separate collection to avoid ConcurrentModException

        for (Entity e : entities)
        {
            if(e == this) continue;

            if (e instanceof Gold && hitBox.overlaps(e.hitBox))
            {
                toCollect.add((Gold)e);
            }
        }

        for (Gold e : toCollect)
        {
            e.collect();
        }

        ShopManager sm = ShopManager.getInstance();

        if (!sm.isOpen()) return;

        sm.updatePrices(gold);

        ShopItem[] shopItems = sm.getItems();
        ShopItem currentItem;

        for (int i=0; i<shopItems.length; i++)
        {
            currentItem = shopItems[i];
            if (hitBox.overlaps(currentItem.getHitbox())) {
                if (currentItem.getBuyable()) updateAndDrawEInteract(Color.WHITE); // Shows white E next to player.
                else updateAndDrawEInteract(new Color(0xda3c47ff)); // Shows red E next to player.
            }

            if (hitBox.overlaps(currentItem.getHitbox()) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {

                if (currentItem.buy()) {
                    gold -= currentItem.getPrice();
                }


                break;
            }
        }
    }


    /**
     * Caculates the compass direction for the attacking animation.
     * @param vectorDirection vector from the player to the mouse.
     */
    public void setAnimationAttackDirection(Vector2 vectorDirection) {
        float angle = vectorDirection.angleDeg();
        if(angle > 60 && angle < 120) playerAnimation.setDirectionFacing(Utilities.UP);
        else if(angle > 120 && angle < 240) playerAnimation.setDirectionFacing(Utilities.LEFT);
        else if(angle > 240 && angle < 300) playerAnimation.setDirectionFacing(Utilities.DOWN);
        else playerAnimation.setDirectionFacing(Utilities.RIGHT);
    }


    /**
	 * @param directionFacing the directionFacing to set
	 */
    public void setDirectionFacing(int directionFacing) {
        playerAnimation.setDirectionFacing(directionFacing);
    }

    
    /**
     * Returns if player is dead.
     * @return true if dead
     */
    public boolean isDead() {
        return health <= 0;
    }


    /**
     * Gets whether the attacking animation needs to be played.
     * @return true if attacking
     */
    public boolean getPlayAttackingAnimation() {
        return playAttackingAnimation;
    }

    /**
     * @param playAttackingAnimation the attackingAnimation to set
     */
    public void setPlayAttackingAnimation(boolean playAttackingAnimation) {
        this.playAttackingAnimation = playAttackingAnimation;
    }


    /**
     * @return the totalDamageDealt
     */
    public static int getTotalDamageDealt() {
        return totalDamageDealt;
    }


    /**
     * Resets all the players attributes,
     * randomises their weapons,
     * and resets their position.
     */
    public void resetPlayer() {
        canMove = true;
        playAttackingAnimation = false;
        isKnockedBack = false;
        isInvincible = false;
        health = maxHealth;
        gold = 0;
        speed = 192;
        position = new Vector2(50, Utilities.SCREEN_HEIGHT/2-35);
        totalDamageDealt = 0;
        invincibilityFrames = 0;
        attackCooldown = 0;
        facing = Utilities.RIGHT;
        knockBackCount = 0;
        mouseClick = Utilities.DOWN;
        rerollWeapons();
    }

    public void rerollWeapons()
    {
        weaponL = WeaponGenerator.getRandomWeapon();
        weaponR = WeaponGenerator.getRandomWeapon();
        while (true){
            if(weaponL.getName() == weaponR.getName()) weaponR = WeaponGenerator.getRandomWeapon();
            else break;
        }
    }

    private void updateCooldownBar()
    {
        int newWidth = (int) (cooldownbarFillTexture.getWidth() * (attackCooldown/cooldownFrom));
        cooldownbarFillRegion = new TextureRegion(cooldownbarFillTexture, 0, 0, newWidth, cooldownbarFillTexture.getHeight());
        cooldownbarFill.setRegion(cooldownbarFillRegion);
        cooldownbarFill.setSize(newWidth * 2, 8);
    }

    @Override
    public void setFacing(Vector2 direction){
        if (direction.x > 0) facing = Utilities.RIGHT;
        else if(direction.x < 0) facing = Utilities.LEFT;
        else if(direction.x == 0){
            if (direction.y > 0) facing = Utilities.UP;
            else if(direction.y < 0) facing = Utilities.DOWN;
        }
    }


    @Override
    public void dispose() {
        playerAnimation.dispose();
        swordAttackSound.dispose();
        spearAttackSound.dispose();
    }

    private void switchAnim(String anim)
    {
        if (animPlaying <= 0) {
            playerStateAnimator.switchState(anim);
            playerStateAnimatorDown.switchState(anim);
            playerStateAnimatorUp.switchState(anim);
        }
    }
}

package com.dungeon.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.dungeon.entities.EntityManager;
import com.dungeon.entities.Player;
import com.dungeon.misc.Utilities;

/**
 * Class that handles drawing a room.
 */
public class Room {

    private Texture background;

    private boolean leftDoorExists;
    private boolean rightDoorExists;

    private Sprite leftDoorSprite;
    private Sprite rightDoorSprite;
    private Rectangle leftDoorHitbox;
    private Rectangle rightDoorHitbox;

    private Sprite rightLockedLockSprite;
    private Sprite leftLockedLockSprite;
    private Sprite rightUnlockedLockSprite;
    private Sprite leftUnlockedLockSprite;

    private boolean roomUnlocked = false;


    /**
     * Constructor to set up a room, and its doors and locks.
     * @param background the background texture to use
     * @param leftDoorExists true if you want left door to be drawn
     * @param rightDoorExists true if you want right door to be drawn
     */
    public Room(Texture background, boolean leftDoorExists, boolean rightDoorExists) {

        this.background = background;
        this.leftDoorExists = leftDoorExists;
        this.rightDoorExists = rightDoorExists;

        if(rightDoorExists) {
            rightDoorSprite = new Sprite(new Texture(Gdx.files.internal("misc/door2.png")));
            rightDoorSprite.setFlip(true, false);
            rightDoorSprite.setBounds(Utilities.SCREEN_WIDTH - 200, (Utilities.SCREEN_HEIGHT / 2) - 128, 128, 128);
            rightDoorHitbox = new Rectangle(rightDoorSprite.getX()+64, rightDoorSprite.getY(), rightDoorSprite.getWidth()*0.5f, rightDoorSprite.getHeight()*0.75f);

            rightLockedLockSprite  = new Sprite(new Texture(Gdx.files.internal("misc/lockedLock.png")));
            rightLockedLockSprite.setPosition(rightDoorHitbox.x+rightDoorHitbox.width*0.5f+16, rightDoorHitbox.y+rightDoorHitbox.height+10);
            rightLockedLockSprite.setScale(2f);
            rightUnlockedLockSprite  = new Sprite(new Texture(Gdx.files.internal("misc/unlockedLock.png")));
            rightUnlockedLockSprite.setPosition(rightDoorHitbox.x+rightDoorHitbox.width*0.5f+16, rightDoorHitbox.y+rightDoorHitbox.height+10);
            rightUnlockedLockSprite.setScale(2f);
        }
        if(leftDoorExists) {
            leftDoorSprite = new Sprite(new Texture(Gdx.files.internal("misc/door2.png")));
            leftDoorSprite.setBounds(72, (Utilities.SCREEN_HEIGHT / 2) - 128, 128, 128);
            leftDoorHitbox = new Rectangle(leftDoorSprite.getX(), leftDoorSprite.getY(), leftDoorSprite.getWidth()*0.5f, leftDoorSprite.getHeight()*0.75f);

            leftLockedLockSprite = new Sprite(rightLockedLockSprite);
            leftLockedLockSprite.setPosition(leftDoorHitbox.x, leftDoorHitbox.y+leftDoorHitbox.height+10);
            leftUnlockedLockSprite = new Sprite(rightUnlockedLockSprite);
            leftUnlockedLockSprite.setPosition(leftDoorHitbox.x, leftDoorHitbox.y+leftDoorHitbox.height+10);
        }
    }


    /**
     * Draws the background, the doors, and the locks.
     * @param batch
     */
    public void drawRoom(SpriteBatch batch) {
        batch.draw(background, 0, 0, Utilities.SCREEN_WIDTH, Utilities.SCREEN_HEIGHT);
        if(rightDoorExists) {
            rightDoorSprite.draw(batch);
            if(roomUnlocked) rightUnlockedLockSprite.draw(batch);
            else rightLockedLockSprite.draw(batch);
        }
        if(leftDoorExists){
            leftDoorSprite.draw(batch);
            if(roomUnlocked) leftUnlockedLockSprite.draw(batch);
            else leftLockedLockSprite.draw(batch);
        }
    }


    /**
     * Checks if the user is touching a door. If they are,
     * increment/decrement the current level and return it.
     * @param currentLevel the current level
     * @return the new level
     */
    public int checkForDoorCollisionAndChangeLevel(int currentLevel) {
        Player player = EntityManager.getInstance().getPlayer();

        if(rightDoorExists && player.getHitBox().overlaps(rightDoorHitbox)) {
            return currentLevel+1;
        }
        else if(leftDoorExists && player.getHitBox().overlaps(leftDoorHitbox)) {
            return currentLevel-1;
        }
        else {
            return currentLevel;
        }
    }

    /**
     * Checks if touching a door's hitbox.
     * @return true if touching a door
     */
    public boolean checkForDoorCollision() {
        Player player = EntityManager.getInstance().getPlayer();
        if(rightDoorExists && player.getHitBox().overlaps(rightDoorHitbox))
            return true;
        else if(leftDoorExists && player.getHitBox().overlaps(leftDoorHitbox))
            return true;
        else
            return false;
    }

    /**
     * Returns if room is unlocked.
     */
    public boolean getRoomUnlocked() {
        return roomUnlocked;
    }

    /**
     * @param roomUnlocked the roomUnlocked to set
     */
    public void setRoomUnlocked(boolean roomUnlocked) {
        this.roomUnlocked = roomUnlocked;
    }

    /**
     * Method that should be called when the program is closed.
     */
    public void dispose() {
        background.dispose();
    }
}

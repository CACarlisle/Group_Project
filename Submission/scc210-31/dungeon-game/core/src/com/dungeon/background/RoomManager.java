package com.dungeon.background;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.dungeon.entities.Enemy;
import com.dungeon.entities.Entity;
import com.dungeon.entities.EntityManager;
import com.dungeon.entities.Player;
import com.dungeon.entities.RangedSlime;
import com.dungeon.entities.Slime;
import com.dungeon.misc.Utilities;

/**
 * Class to manage the rooms and the enemies in that room.
 */
public class RoomManager {

    private static SpriteBatch batch;
    private static Player player;
    private EntityManager entityManager;

    private Room[] rooms;
    private int currentLevel;
    private int newLevel;
    private int roomToDraw;
    private static int maxLevelReached;

    private Sound lockSound;
    private Sound unlockSound;

    private Random r;
    private Vector2 enemyVectors[];

    private boolean transitioning;
    private Texture screenWipeTexture;
    private Image screenWipeImageActor;
    private float transitionTime;
    private float timeElapsed;
    private float start;
    private float end;


    /**
     * Creates the necessary rooms and spawns randomly generated enemies.
     * @param batch
     * @param player
     */
    public RoomManager(SpriteBatch batch, Player player) {
        RoomManager.batch = batch;
        RoomManager.player = player;
        entityManager = EntityManager.getInstance();

        rooms = new Room[5];
        for (int i = 0; i < rooms.length-1; i++) {
            rooms[i] = new Room(new Texture(Gdx.files.internal("backgrounds/background" + (i+1) + ".png")), true, true);
        }
        rooms[4] = new Room(new Texture(Gdx.files.internal("backgrounds/background" + 1 + ".png")), false, true);

        screenWipeTexture = new Texture(Gdx.files.internal("ui/screenwipe.png"));
        screenWipeImageActor = new Image(screenWipeTexture);

        currentLevel = 0;
        newLevel = 0;
        roomToDraw = 0;
        maxLevelReached = currentLevel;
        transitioning = false;

        lockSound = Gdx.audio.newSound(Gdx.files.internal("audio/lockSound1.wav"));
        unlockSound = Gdx.audio.newSound(Gdx.files.internal("audio/unlockSound1.wav"));

        r = new Random();
        randomiseEnemies();
        spawnEnemies();
    }


    /**
     * Draws the correct room.
     */
    public void drawRoom() {
        if(currentLevel == 0) roomToDraw = 4;
        else roomToDraw = currentLevel%4;

        if(!transitioning) {
            if(isRoomEmptyOfEnemies() && !rooms[roomToDraw].getRoomUnlocked()) unlockRoom();
            rooms[roomToDraw].drawRoom(RoomManager.batch);
            if(checkForRoomChange()) setupTransition();
        }
        else {
            rooms[roomToDraw].drawRoom(RoomManager.batch);
        }
    }


    /**
     * Returns whether the room needs to change.
     */
    public boolean checkForRoomChange() {
        if (!Gdx.input.isKeyJustPressed(Input.Keys.E) || !isRoomEmptyOfEnemies()) return false;
        newLevel = rooms[roomToDraw].checkForDoorCollisionAndChangeLevel(currentLevel);
        if(currentLevel == newLevel) return false;
        else return true;
    }


    /**
     * Sets the room to unlocked and plays the unlocking sound.
     */
    private void unlockRoom() {
        rooms[roomToDraw].setRoomUnlocked(true);
        unlockSound.setVolume(unlockSound.play(), 0.4f);
    }


    /**
     * Shows the transition.
     */
    public void showTransition() {
        if(timeElapsed>transitionTime) {
            transitioning = false;
            return;
        }
        if(currentLevel != newLevel && timeElapsed>transitionTime/2) {
            movePlayerAndSpawnEnemies();
            rooms[roomToDraw].setRoomUnlocked(false);
            currentLevel = newLevel;
        }
        screenWipeImageActor.act(Gdx.graphics.getDeltaTime());
        batch.draw(screenWipeTexture, screenWipeImageActor.getX(), screenWipeImageActor.getY(), Utilities.SCREEN_WIDTH*2f, Utilities.SCREEN_HEIGHT);
        timeElapsed += Gdx.graphics.getDeltaTime();
    }
    
    /**
     * Sets up the tranisition before it starts.
     */
    public void setupTransition() {
        transitioning = true;
        if(currentLevel<newLevel) {
            start = Utilities.SCREEN_WIDTH;
            end = -Utilities.SCREEN_WIDTH*2f;
        }
        else {
            start = -Utilities.SCREEN_WIDTH*2f;
            end = Utilities.SCREEN_WIDTH;
        }
        transitionTime = 1.6f;
        timeElapsed = 0f;
        screenWipeImageActor.setPosition(start, 0);
        screenWipeImageActor.addAction(Actions.moveTo(end, 0, transitionTime));
    }


    /**
     * Move the player to the correct side of the room and spawn new enemies.
     */
    private void movePlayerAndSpawnEnemies() {
        if(currentLevel<newLevel) {
            RoomManager.player.setPosition(new Vector2(108, (Utilities.SCREEN_HEIGHT / 2) - 96));
            RoomManager.player.setDirectionFacing(Utilities.RIGHT);
            EntityManager.killAllNotPlayer(); // destroys all gold for now

            if (newLevel > maxLevelReached) {
                spawnEnemies();
                maxLevelReached = newLevel;
                lockSound.setVolume(lockSound.play(), 0.4f);
            }
        }
        else if(currentLevel>newLevel) {
            RoomManager.player.setPosition(new Vector2(Utilities.SCREEN_WIDTH - 100, (Utilities.SCREEN_HEIGHT / 2) - 96));
            RoomManager.player.setDirectionFacing(Utilities.LEFT);
            EntityManager.killAllNotPlayer(); // destroys all gold for now
        }
    }


    /**
     * Spawn the randomly generated enemies.
     */
    private void spawnEnemies() {
        for (int i = 0; i < enemyVectors.length; i++) {
            if(r.nextInt(2) == 0) new Slime(RoomManager.player, 10+currentLevel*5).setPosition(enemyVectors[i]);
            else new RangedSlime(RoomManager.player, 5+currentLevel*5).setPosition(enemyVectors[i]);
        }
        entityManager.updateEntitiesList();
        randomiseEnemies();
    }


    /**
     * Randomly generates a number of melee and ranged enemies
     * and sets the position to be random.
     */
    private void randomiseEnemies() {
        int numbOfEnemies = r.nextInt(3+(currentLevel*2)) + 4+currentLevel;

        enemyVectors = new Vector2[numbOfEnemies];

        for (int i = 0; i < numbOfEnemies; i++) {
            enemyVectors[i] = new Vector2(r.nextInt(Utilities.SCREEN_WIDTH / 2) + Utilities.SCREEN_WIDTH / 2, r.nextInt(Utilities.SCREEN_HEIGHT));
        }
    }


    /**
     * Checks if the room is empty of enemies.
     * 
     * @return true if room is empty
     */
    public boolean isRoomEmptyOfEnemies() {
        for (Entity e : EntityManager.getInstance().getEntities()) {
            if (e instanceof Enemy)
                return false;
        }
        return true;
    }

    /**
     * Method to count the number of enemies in the room and return the count
     * @return Returns an integer count of the number of enemies in the room
     */
    public static int numOfEnemies() {
        int numOfEnemies = 0;
        for (Entity e : EntityManager.getInstance().getEntities()) {
            if (e instanceof Enemy)
                numOfEnemies++;
        }
        return numOfEnemies;
    }

    /**
     * Returns if the player is touching a door.
     * @return true if touching a door's hitbox
     */
    public boolean isPlayerTouchingDoor() {
        return rooms[roomToDraw].checkForDoorCollision();
    }

    /**
     * @return the maxLevelReached
     */
    public static int getMaxLevelReached() {
        return maxLevelReached;
    }

    /**
     * Returns if the room is currently transitioning.
     * @return true if transition is being shown.
     */
    public boolean getTransitioning() {
        return transitioning;
    }

    /**
     * Called when the game is reset.
     * Goes back to room one,
     * and kills and respawns enemies.
     */
    public void resetRoom() {
        currentLevel = 0;
        newLevel = 0;
        maxLevelReached = 0;
        roomToDraw = 0;
        transitioning = false;
        EntityManager.killAllNotPlayer();
        randomiseEnemies();
        spawnEnemies();

        for (Room room : rooms) {
            room.setRoomUnlocked(false);
        }
    }

    /**
     * Method that should be called when the program is closed.
     */
    public void dispose() {
        screenWipeTexture.dispose();
        for (int i = 0; i < rooms.length; i++) {
            rooms[i].dispose();
        }
    }
}

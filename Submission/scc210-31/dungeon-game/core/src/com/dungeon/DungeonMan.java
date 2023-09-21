package com.dungeon;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.dungeon.background.RoomManager;
import com.dungeon.debug.*;
import com.dungeon.entities.Enemy;
import com.dungeon.entities.Entity;
import com.dungeon.entities.EntityManager;
import com.dungeon.entities.Player;
import com.dungeon.misc.Utilities;
import com.dungeon.particles.*;
import com.dungeon.shop.*;
import com.dungeon.ui.UI;
import com.dungeon.weapons.Weapon;

/**
 * The main class of the game
 */
public class DungeonMan extends ApplicationAdapter {

	private OrthographicCamera camera;
	private BitmapFont font;
	private SpriteBatch batch;

	private Music backgroundMusic;

	private Player player;

	private RoomManager roomManager;
	private boolean clearedRoomChanged;
	private boolean clearedRoom;
	private boolean tempClearedRoom;

	private EntityManager entityManager;
	private ParticleManager particleManager;
	private ShopManager shopManager;

	private ArrayList<Entity> entities;

	private Stage uiStage;
  
	private UI userInterface;

	// Debugging
	private boolean debugOn = false;

	public enum State {
		PAUSE,
		RUN,
		RESUME,
		STOPPED,
		GAMEOVER
	}
	private State state = State.PAUSE;


	@Override
	public void create() {
		batch = new SpriteBatch();

		font = new BitmapFont();

		entityManager = EntityManager.getInstance();
		particleManager = ParticleManager.getInstance();
		shopManager = ShopManager.getInstance();
		shopManager.stockShop();


		player = new Player(batch);
		entityManager.setPlayer(player);

		uiStage = new Stage(new ScreenViewport());
		roomManager = new RoomManager(batch, player);
		userInterface = new UI(uiStage, this);
		userInterface.updateWeaponIcons(player);
		/*
		 * backgroundTrack1.ogg - Matt's song
		 * backgroundTrack2.ogg - Kez's Song
		 * backgroundTrack3.ogg - Kez's Song but different (kinda wacky)
		 * (ps, i made them in garageband so thats why theyre not great xD)
		 */
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/backgroundTrack2.ogg"));
		backgroundMusic.setVolume(0.2f);
		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		entities = entityManager.getEntities();

		// set up the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Utilities.SCREEN_WIDTH, Utilities.SCREEN_HEIGHT);

		font = new BitmapFont();

		userInterface.showMainMenu();

		clearedRoom = false;
		clearedRoomChanged = true;
	}

	@Override
	public void render() {

		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		uiStage.act();
		switch (state) {
			case RUN:
				ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1f); // Clears the screen to grey
				camera.update();

				roomManager.drawRoom();

				// ui rendering
				
				uiStage.draw();
				

				shopManager.updateAll();

				tempClearedRoom = roomManager.isRoomEmptyOfEnemies();
				clearedRoomChanged = tempClearedRoom != clearedRoom;
				if (clearedRoomChanged) shopManager.showShop(roomManager.isRoomEmptyOfEnemies());
				clearedRoom = tempClearedRoom;

				drawSprites();
				if(roomManager.isPlayerTouchingDoor() && roomManager.isRoomEmptyOfEnemies()) player.updateAndDrawEInteract(Color.WHITE);
				handleDebug();
				drawUI();

				if(roomManager.getTransitioning()) roomManager.showTransition();

				entityManager.updateAll(); // attack and move all entities on screen
				particleManager.updateAll();

				if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
					userInterface.showPauseMenu();

				if(player.isDead()) {
					userInterface.showGameOverMenu();
					resetGame();
					gameOver();
				}

				break;
			case PAUSE:
				if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
					userInterface.hideMenu();
				break;
			case RESUME:
				this.state = State.RUN;
				break;
			case GAMEOVER:
				// Do nothing
				break;
			case STOPPED:
				// Do nothing
				break;
			default:
				break;
		}

		uiStage.draw();
		batch.end();
	}

	/**
	 * Shows the hitboxes on screen.
	 */
	private void handleDebug() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
			debugOn = !debugOn;
			if (debugOn) {
				Debugger.logInfo("Debugging turned " + Debugger.ANSI_GREEN_BOLD + "ON");
			} else {
				Debugger.logInfo("Debugging turned " + Debugger.ANSI_RED_BOLD + "OFF");
			}
		}
		if (debugOn) {
			Debugger.drawRect(Utilities.SCREEN_WIDTH - 136, (Utilities.SCREEN_HEIGHT / 2) - 128, 64, 96, Color.VIOLET);
			Debugger.drawRect(72, (Utilities.SCREEN_HEIGHT / 2) - 128, 64, 96, Color.GREEN);
			//Debugger.drawRect(20, (Utilities.SCREEN_HEIGHT / 2) - 80, 40, 80, Color.PURPLE);
			for (Entity e : entities) {
				if (e == player)
					Debugger.drawRect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height,
							Color.GREEN); // Debugger Player
				else
					Debugger.drawRect(e.getHitBox().x, e.getHitBox().y, e.getHitBox().width, e.getHitBox().height, Color.RED);
			}
		}
	}

	/**
	 * Draws temporary ui text to display basic stats
	 */ 
	private void drawUI() {

		// ui rendering
		uiStage.draw();

		// health and gold placed around their respective locations
		BitmapFont uiFont = new BitmapFont();
		uiFont.draw(batch, "Gold: " + player.getGold(), 60, Utilities.SCREEN_HEIGHT - 90);

		Weapon l = player.getEquippedL();
		Weapon r = player.getEquippedR();
		uiFont.draw(batch, "Equipped L: " + l.toString(), Utilities.SCREEN_WIDTH/2-540, 15);
		uiFont.draw(batch, "Equipped R: " + r.toString(), Utilities.SCREEN_WIDTH/2+120, 15);

		uiFont.draw(batch, "Rooms Cleared: " + RoomManager.getMaxLevelReached(), 60, Utilities.SCREEN_HEIGHT-190);
		uiFont.draw(batch, "Enemies Remaining: " + RoomManager.numOfEnemies(), 60, Utilities.SCREEN_HEIGHT - 130);

		if (debugOn) {
			font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 30, 30);

			for (Entity e : entities) {
				if (e.getIsInvincible())
					continue;

				font.draw(batch, "HP: " + e.getHealth(), e.getX(), e.getY() - 30);
			}
		}
		//Prevents font from being distorted
		BitmapFont fontFix = new BitmapFont();
		fontFix.draw(batch, "test", 0, 0);

		fontFix.dispose();
		uiFont.dispose();
	}


	/**
	 * Draws every entity.
	 */
	private void drawSprites()
	{
		for (Entity e : entities)
		{
			e.draw(batch);
		}
	}

	@Override
	public void pause() {
		this.state = State.PAUSE;
		backgroundMusic.setVolume(0.2f);
	}

	@Override
	public void resume() {
		this.state = State.RESUME;
		backgroundMusic.setVolume(0.5f);
	}

	public void endGame() {
		Gdx.app.exit();
	}

	public void resetGame() {
		player.resetPlayer();
		roomManager.resetRoom();
		Enemy.setTotalEnemiesKilled(0);
	}

	public void gameOver() {
		this.state = State.GAMEOVER;
		backgroundMusic.setVolume(0.2f);

	}

	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		backgroundMusic.dispose();
		roomManager.dispose();
		uiStage.dispose();
		for (Entity entity : entities) {
			entity.dispose();
		}
	}
}

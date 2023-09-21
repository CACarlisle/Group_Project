package com.dungeon.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;

import com.dungeon.DungeonMan;
import com.dungeon.background.RoomManager;
import com.dungeon.entities.Enemy;
import com.dungeon.entities.EntityManager;
import com.dungeon.entities.Player;
import com.dungeon.misc.HealthBar;
import com.dungeon.misc.Utilities;

/**
 * A class creating the UI for the player
 */
public class UI {

    //Declaration of main elements that will be changed elsewhere
    private Image playButton;
    private Image retryButton;
	private Image endButton;
	private Image exitBtn;
	private Image menuBackgroundImage;
    private DungeonMan gameInstance;
    private HealthBar health;

	private static UI UIInstance;

	private Table infoTable;
	private Table gameOverTable;
	private Label roomsClearedLbl;
	private Label totalDamageDealtLbl;
	private Label totalEnemiesKilledLbl;

	private static Image leftWeaponUI;
	private static Image rightWeaponUI;
	private Stage stage;
    private static ShapeRenderer renderer;

	/**
	 * Creates an instance of the UI
	 * @param uiStage The stage used for rendering the UI
	 * @param instance A reference to the main class of the game
	 */
    public UI(Stage uiStage, DungeonMan instance){
		UIInstance = this;

		stage = uiStage;
        gameInstance = instance;
		renderer = new ShapeRenderer();

        //GOLD COUNTER
		Texture goldTexture = new Texture(Gdx.files.internal("misc/gold.png"));
		Image gold = new Image(goldTexture);
		gold.setSize(24, 24);
		gold.setY(Utilities.SCREEN_HEIGHT-100);
		gold.setX(20);
		uiStage.addActor(gold);
    
        //HEALTH BAR
		health = new HealthBar(uiStage);

		//Defeated enemies
		Texture skullTexture= new Texture(Gdx.files.internal("ui/skull.png"));
		Image skull = new Image(skullTexture);
		skull.setX(10);
		skull.setY(Utilities.SCREEN_HEIGHT-160);
		skull.setSize(48, 48);
		uiStage.addActor(skull);
		//Rooms cleared
		Texture clearedTexture= new Texture(Gdx.files.internal("ui/cleared_room.png"));
		Image cleared = new Image(clearedTexture);
		cleared.setX(10);
		cleared.setY(Utilities.SCREEN_HEIGHT-220);
		cleared.setSize(48, 48);
		uiStage.addActor(cleared);



		//set up weapon information 
		infoTable = new Table();
		infoTable.setPosition(0,0);
		infoTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		infoTable.align(Align.bottom);
		uiStage.addActor(infoTable);

		Texture swordTexture = new Texture(Gdx.files.internal("misc/no_texture.png"));
		leftWeaponUI = new Image(swordTexture);
		Texture spearTexture = new Texture(Gdx.files.internal("misc/no_texture.png"));
		rightWeaponUI = new Image(spearTexture);

		Texture mouseLeftTexture = new Texture(Gdx.files.internal("ui/mouseLeft.png"));
		Image mouseUILeft = new Image(mouseLeftTexture);
		Texture mouseRightTexture = new Texture(Gdx.files.internal("ui/mouseRight.png"));
		Image mouseUIRight = new Image(mouseRightTexture);

		mouseUILeft.setSize(32, 32);
		mouseUIRight.setSize(32, 32);
		mouseUILeft.setX((Utilities.SCREEN_WIDTH/2) - 48);
		mouseUIRight.setX((Utilities.SCREEN_WIDTH/2) + 16);
		mouseUILeft.setY(80);
		mouseUIRight.setY(80);
		uiStage.addActor(mouseUILeft);
		uiStage.addActor(mouseUIRight);

		leftWeaponUI.setX((Utilities.SCREEN_WIDTH/2) - 72);
		rightWeaponUI.setX((Utilities.SCREEN_WIDTH/2));

		//ADD TO TABLE
		infoTable.add(leftWeaponUI);
		infoTable.add(rightWeaponUI);
		
		//set up room information (when assets are made)

        //Menu
		Texture menuBackground = new Texture("ui/Title Screen.png");
		menuBackgroundImage = new Image(menuBackground);
		uiStage.addActor(menuBackgroundImage);

		Table menuTable = new Table();
		menuTable.setPosition(0,0);
		menuTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		menuTable.align(Align.center);

		uiStage.addActor(menuTable);
        menuTable.setColor(Color.BLACK);
        Texture play = new Texture("ui/playButtonOff.png");
		playButton = new Image(play);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//WHEN PLAY BUTTON IS CLICKED, GAME IS RESUMED	
                hideMenu();
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				Texture playOn = new Texture("ui/playButtonOn.png");
				playButton.setDrawable(new TextureRegionDrawable(new TextureRegion(playOn)));
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				Texture playOff = new Texture("ui/playButtonOff.png");
				playButton.setDrawable(new TextureRegionDrawable(new TextureRegion(playOff)));
			}
		});

        playButton.setVisible(false);
		menuTable.add(playButton);
		Texture end = new Texture("ui/exitButtonOff.png");
		endButton = new Image(end);
		endButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//WHEN END GAME BUTTON IS CLICKED, GAME IS RESUMED
				Gdx.app.exit();
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				Texture exitOn = new Texture("ui/exitButtonOn.png");
				endButton.setDrawable(new TextureRegionDrawable(new TextureRegion(exitOn)));
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				Texture exitOff = new Texture("ui/exitButtonOff.png");
				endButton.setDrawable(new TextureRegionDrawable(new TextureRegion(exitOff)));
			}
		});
        endButton.setVisible(false);

		Texture padding = new Texture("ui/invisiblePadding.png");
		Image paddingImage = new Image(padding);
		menuTable.add(paddingImage);
		menuTable.add(endButton);


		//createWeaponStatsUI(uiStage);
		createGameOverScreen(uiStage);



		Gdx.input.setInputProcessor(uiStage);
    }

	private void createWeaponStatsUI(Stage uiStage)
	{
		Table statsTable = new Table();
		statsTable.setPosition(0,0);
		statsTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		statsTable.align(Align.bottom);
		//statsTable.setDebug(true);

		Texture damageTex = new Texture("weapons/damage_icon.png");

		Image damageUI = new Image(damageTex);

		statsTable.add(damageUI);

		uiStage.addActor(statsTable);
	}


/**
 * Method to create a gameover screen
 * @param uiStage The stage used for rendering the UI
 */
	private void createGameOverScreen(Stage uiStage) {
		// Outer table (whole menu; stats+buttons):
		gameOverTable = new Table();
		gameOverTable.setPosition(0,0);
		gameOverTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gameOverTable.align(Align.center);
        gameOverTable.setColor(Color.BLACK);
		uiStage.addActor(gameOverTable);

		// One inner table (just for stats):
		Table statsTable = new Table();

		Label gameOverLbl = new Label("Game Over", new LabelStyle(new BitmapFont(), Color.RED));
		gameOverLbl.setFontScale(8);
		statsTable.add(gameOverLbl);

		statsTable.row();
		roomsClearedLbl = new Label("", new LabelStyle(new BitmapFont(), Color.CORAL));
		roomsClearedLbl.setFontScale(3);
		statsTable.add(roomsClearedLbl);

		statsTable.row();
		totalDamageDealtLbl = new Label("", new LabelStyle(new BitmapFont(), Color.CORAL));
		totalDamageDealtLbl.setFontScale(3);
		statsTable.add(totalDamageDealtLbl);

		statsTable.row();
		totalEnemiesKilledLbl = new Label("", new LabelStyle(new BitmapFont(), Color.CORAL));
		totalEnemiesKilledLbl.setFontScale(3);
		statsTable.add(totalEnemiesKilledLbl);

		gameOverTable.add(statsTable);
		gameOverTable.row();

		// Other inner table (just for buttons):
		Table buttonTable = new Table();

        Texture play = new Texture("ui/playButtonOff.png");
		retryButton = new Image(play);
		retryButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//WHEN PLAY BUTTON IS CLICKED, GAME IS RESUMED
				renderHealth(EntityManager.getInstance().getPlayer().getHealth());
                hideGameOverMenu();
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				Texture playOn = new Texture("ui/playButtonOn.png");
				retryButton.setDrawable(new TextureRegionDrawable(new TextureRegion(playOn)));
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				Texture playOff = new Texture("ui/playButtonOff.png");
				retryButton.setDrawable(new TextureRegionDrawable(new TextureRegion(playOff)));
			}
		});
		buttonTable.add(retryButton);

		Texture exitTexture = new Texture("ui/exitButtonOff.png");
		exitBtn = new Image(exitTexture);
		exitBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//WHEN END GAME BUTTON IS CLICKED, GAME IS RESUMED
				Gdx.app.exit();
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				Texture exitOn = new Texture("ui/exitButtonOn.png");
				exitBtn.setDrawable(new TextureRegionDrawable(new TextureRegion(exitOn)));
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				Texture exitOff = new Texture("ui/exitButtonOff.png");
				exitBtn.setDrawable(new TextureRegionDrawable(new TextureRegion(exitOff)));
			}
		});

		Texture padding = new Texture("ui/invisiblePadding.png");
		Image paddingImage = new Image(padding);
		buttonTable.add(paddingImage);

		buttonTable.add(exitBtn);

		gameOverTable.add(buttonTable);
		gameOverTable.setVisible(false);
		Gdx.input.setInputProcessor(uiStage);
	}


	/**
	 * Method used to show the main menu
	 */
	public void showMainMenu(){
		menuBackgroundImage.setVisible(true);
		playButton.setVisible(true);
		endButton.setVisible(true);
        gameInstance.pause();
	}
	/**
	 * Method used to show the pause menu
	 */
    public void showPauseMenu(){
        playButton.setVisible(true);
		endButton.setVisible(true);
        gameInstance.pause();
    }

	/**
	 * Method used to show the game over menu
	 * @param roomsCleared The number of rooms cleared
	 * @param totalEnemiesKilled The number of enemies killed
	 * @param totalDamageDealt The amount of damage dealt
	 */
	public void showGameOverMenu() {
		this.roomsClearedLbl.setText("Rooms Cleared: " + RoomManager.getMaxLevelReached());
		this.totalEnemiesKilledLbl.setText("Total Enemies Killed: " + Enemy.getTotalEnemiesKilled());
		this.totalDamageDealtLbl.setText("Total Damage Dealt: " + Player.getTotalDamageDealt());
		gameOverTable.setVisible(true);
        gameInstance.pause();
	}
	/**
	 * Method to hide the gameOverMenu
	 */
	public void hideGameOverMenu() {
		gameOverTable.setVisible(false);
        gameInstance.resume();
	}
	/**
	 * Method to hide pause & main menu
	 */
    public void hideMenu(){
        playButton.setVisible(false);
		endButton.setVisible(false);
		menuBackgroundImage.setVisible(false);
        gameInstance.resume();
    }
	/**
	 * Method to render the healthbar
	 * @param healthValue the current health
	 */
    public void renderHealth(float healthValue){
        health.updateHealthBar(healthValue);
    }
	
	/**
	 * Updates the weapon icons for the UI to the weapons the player currently has equiped
	 * @param playerInstance
	 */
	public void updateWeaponIcons(Player playerInstance){
		infoTable.removeActor(leftWeaponUI);
		infoTable.removeActor(rightWeaponUI);
		leftWeaponUI = new Image(playerInstance.getEquippedL().getIcon());
		rightWeaponUI = new Image(playerInstance.getEquippedR().getIcon());
		leftWeaponUI.setX((Utilities.SCREEN_WIDTH/2)-70);
		rightWeaponUI.setX((Utilities.SCREEN_WIDTH/2)+6);

		leftWeaponUI.setSize(72f, 72f);
		rightWeaponUI.setSize(72f, 72f);

		infoTable.addActor(leftWeaponUI);
		infoTable.addActor(rightWeaponUI);
	}

	public static UI getInstance(){
		return UIInstance;
	}
}

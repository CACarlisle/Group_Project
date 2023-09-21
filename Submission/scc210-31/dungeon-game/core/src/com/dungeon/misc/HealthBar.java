package com.dungeon.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * A class for creating a healthbar and adding it to the UI
 */

public class HealthBar {
  
    private float maxHealth = 100;

    private Image healthBar;
    private Image healthBackground;

    private Texture health;
    private TextureRegion healthCropped;
    
    public HealthBar(Stage uiStage){
        //create images

        health = new Texture(Gdx.files.internal("ui/hp-bar-fill.png"));
        Texture healthBack = new Texture(Gdx.files.internal("ui/hp-bar-bg.png"));
        
        healthCropped = new TextureRegion(health, 0, 0, health.getWidth(), health.getHeight());
		
        healthBar = new Image(healthCropped);
        healthBackground = new Image(healthBack);

        //set scales and positions

        healthBackground.setSize(384, 48);
        healthBackground.setX(10);
        healthBackground.setY(Utilities.SCREEN_HEIGHT- healthBackground.getHeight() - 10);

        healthBar.setSize(384, 48);
        healthBar.setX(10);
        healthBar.setY(Utilities.SCREEN_HEIGHT - healthBackground.getHeight() - 10);

        //add actors to the ui stage
        uiStage.addActor(healthBackground);
		uiStage.addActor(healthBar);
    }   
    /**
     * A method to update the healthBar to the current health 
     * @param hp A float value representing the health
     */
    public void updateHealthBar(Float hp) {
        if(hp < 0) {
            //healthBar.setScaleX(0);
        }
        else {
            //healthBar.setScaleX(health/maxHealth);
            int newWidth = 12 + (int) ((health.getWidth() - 12) * (hp / maxHealth));
            healthCropped = new TextureRegion(health, 0, 0, newWidth, health.getHeight());
            healthBar.setSize(newWidth * 3, 48);
            healthBar.setDrawable(new TextureRegionDrawable(healthCropped));
        }
    }
}

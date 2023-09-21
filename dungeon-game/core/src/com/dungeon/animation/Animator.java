package com.dungeon.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dungeon.misc.Utilities;
import com.dungeon.entities.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animator {

    private Texture spriteSheet;
	private Animation<TextureRegion> animation;
	private float stateTime;

    private int numberOfCells;
    private int cellX;
    private int cellY;

    private float animationTime;
    private float frameTime;

    private boolean looping;
	private boolean flipOnFacing;

	/**
	 * Constructor
	 */
    public Animator(String spriteSheetPath, int numberOfCells, int cellX, int cellY, float animationTime, boolean looping, boolean flipOnFacing) {
        spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));

        this.numberOfCells = numberOfCells;
        this.cellX = cellX;
        this.cellY = cellY;

        this.animationTime = animationTime;
        frameTime = animationTime /  numberOfCells;
        this.looping = looping;
		this.flipOnFacing = flipOnFacing;

        
        animation = createAnimationFromSpriteSheet(spriteSheet);

		stateTime = 0f;
    }

    /**
	 * Sets the texture of a given entity based on the current animation frame
	 */
    public void animate(Entity entity) {
		
		stateTime += Gdx.graphics.getDeltaTime();

		TextureRegion currentFrame = animation.getKeyFrame(stateTime, looping);

		if (flipOnFacing == true && !currentFrame.isFlipX() && entity.getFacing() == Utilities.RIGHT) {
			currentFrame.flip(true, false);
		}
		else if (flipOnFacing == true && currentFrame.isFlipX() && entity.getFacing() == Utilities.LEFT) {
			currentFrame.flip(true, false);
		}

		entity.setSpriteTexture(currentFrame);
    }

    /**
	 * Takes a sprite sheet and splits it up to create and return an animation.
	 * @param spriteSheet the sprite sheet to create an animation from
	 * @return the created animation
	 */
	public Animation<TextureRegion> createAnimationFromSpriteSheet(Texture spriteSheet) {
		TextureRegion[][] cells = TextureRegion.split(spriteSheet, cellX, cellY);
		TextureRegion[] frames = new TextureRegion[numberOfCells];

		int index = 0;
		for (int i = 0; i < numberOfCells; i++) {
			frames[index++] =  cells[0][i];
		}

		return (new Animation<TextureRegion>(frameTime, frames));
	}

    public boolean isFininshed()
    {
        return !looping && animation.isAnimationFinished(stateTime);
    }

	public void reset() {
		stateTime = 0;
	}

	/**
	 * Disposes of all the used textures.
	 */
	public void dispose () {
		spriteSheet.dispose();
	}
}

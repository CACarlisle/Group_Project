package com.dungeon.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.dungeon.entities.Enemy;
import com.dungeon.misc.Utilities;

/**
 * A class used to abstract EnemyAnimation
 */
public class EnemyAnimation{
    private Texture walkSheet;
	private Animation<TextureRegion> walkAnimation;
	private float stateTime;

	private Animator jumpAnimation;
	private Animator attackAnimation;
	private Animator deathAnimation;

	/**
	 * Constructor
	 */
    public EnemyAnimation(String spriteSheetPath) {
        walkSheet = new Texture(Gdx.files.internal(spriteSheetPath));
        setupAnimations();
		stateTime = 0f;

		// jumpAnimation = new Animator("animations/slime_blue_jump.png", 8, 32,32, 0.75f, true);
		// attackAnimation = new Animator("animations/slime_blue_attack.png", 4, 32,32, 0.75f, true);
		// deathAnimation = new Animator("animations/slime_blue_die.png", 4, 32,32, 0.375f, true);
    }

    /**
	 * Depending on the current keyboard controls being pressed, 
	 * a frame from one of the animations is grabbed and returned to be rendered.
	 * @return the frame to be rendered
	 */
    public void animate(Enemy enemy) {
		// changeStateTime();
		// enemy.setSpriteTexture(walkAnimation.getKeyFrame(stateTime, true));
		//jumpAnimation.animate(enemy);
		//attackAnimation.animate(enemy);
		//deathAnimation.animate(enemy);
    }

    /**
	 * Checks if the frame passed in needs to be flipped to face the correct direction.
	 * If it does, it flips it and returns it.
	 * @param tempCurrentFrame the frame to check and maybe flip
	 * @param direction the direction the character is facing
	 * @return the frame facing the correct direction
	 */
	public TextureRegion flipFrame(TextureRegion tempCurrentFrame, int direction) {
		if((direction == Utilities.LEFT && tempCurrentFrame.isFlipX()) || (direction == Utilities.RIGHT && !tempCurrentFrame.isFlipX())) { // checks if facing left AND checks the frame is flipped OR checks if facing right AND checks if the frame is not flipped
			tempCurrentFrame.flip(true, false); //flips the frame so it is facing left
		}
		return tempCurrentFrame;
	}

    /**
	 * Incremenet the state time by the time between the current frame and previous frame.
	 */
	public void changeStateTime() {
		stateTime += Gdx.graphics.getDeltaTime();
	}

    /**
	 * Goes through each animation sheet and splits it up into squares of equal sizes and
	 * then adds each sqaure to a 1d array.
	 */
	private void setupAnimations() {
		walkAnimation = createWalkingAnimation(walkSheet);
	}

    /**
	 * Takes a walking texture sheet and splits it up to create and return an animation.
	 * @param walkSheet the walking sheet to create an animation from
	 * @return the created animation
	 */
	public Animation<TextureRegion> createWalkingAnimation(Texture walkSheet) {
		TextureRegion[][] tempTiles = TextureRegion.split(walkSheet, 32, 32);
		TextureRegion[] tempFrames = new TextureRegion[6];
		int index = 0;
		for (int i = 0; i < 6; i++) {
			tempFrames[index++] =  tempTiles[1][i];
		}

		// 0.12f is like the speed at which it animates
		return (new Animation<TextureRegion>(0.12f, tempFrames));
	}


	/**
	 * Disposes of all the used textures.
	 */
	public void dispose () {
		walkSheet.dispose();
	}
}

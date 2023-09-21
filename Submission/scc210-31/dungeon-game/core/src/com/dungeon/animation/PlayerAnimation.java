package com.dungeon.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.dungeon.entities.Player;
import com.dungeon.misc.Utilities;

/**
 * A class managing player animations
 */
public class PlayerAnimation{

	private Texture walkSideSheet;
	private Animation<TextureRegion> walkSideAnimation;
	private Texture walkUpSheet;
	private Animation<TextureRegion> walkUpAnimation;
	private Texture walkDownSheet;
	private Animation<TextureRegion> walkDownAnimation;

	private Texture idleSideSheet;
	private Animation<TextureRegion> idleSideAnimation;
	private Texture idleUpSheet;
	private Animation<TextureRegion> idleUpAnimation;
	private Texture idleDownSheet;
	private Animation<TextureRegion> idleDownAnimation;

	private Texture attackSideSheet;
	private Animation<TextureRegion> attackSideAnimation;
	private Texture attackUpSheet;
	private Animation<TextureRegion> attackUpAnimation;
	private Texture attackDownSheet;
	private Animation<TextureRegion> attackDownAnimation;

	private int directionFacing; //current direction the character is facing
	private float stateTime;
	private boolean startedAttackAnimation;


	/**
	 * Gets all the players animation sheets.
	 * Uses these sheets to create the animation frames.
	 */
    public PlayerAnimation() {
		//stores the sheets
		walkSideSheet = new Texture(Gdx.files.internal("animations/walk_side.png"));
		walkUpSheet = new Texture(Gdx.files.internal("animations/walk_up.png"));
		walkDownSheet = new Texture(Gdx.files.internal("animations/walk_down.png"));

		idleSideSheet = new Texture(Gdx.files.internal("animations/idle_side.png"));
		idleUpSheet = new Texture(Gdx.files.internal("animations/idle_up.png"));
		idleDownSheet = new Texture(Gdx.files.internal("animations/idle_down.png"));

		attackSideSheet = new Texture(Gdx.files.internal("animations/attack_side.png"));
		attackUpSheet = new Texture(Gdx.files.internal("animations/attack_up.png"));
		attackDownSheet = new Texture(Gdx.files.internal("animations/attack_down.png"));

		directionFacing = Utilities.RIGHT; // starting direction
		startedAttackAnimation = false;

		setupAnimations();

		stateTime = 0f;
    }


	/**
	 * Figures out the correct frame to show
	 * and animates the character.
	 * @param player
	 */
	public void animate(Player player) {
		this.changeStateTime();
		TextureRegion frame;

		// Checks if attacking animation needs to be played.
		if(player.getPlayAttackingAnimation()) {
			if(!startedAttackAnimation) {	// Checks if the animation has been started yet.
				this.zeroStateTime();		// Sets the time to start the animation
				startedAttackAnimation = true;
			}
			frame = this.getAttackFrame();	// Gets attacking frame
			if(!this.attacking()) {
				player.setPlayAttackingAnimation(false);
				startedAttackAnimation = false;
			}
		}
		else frame = this.getWalkOrIdleFrame();	// Gets walking or idling frame

		player.setSpriteTexture(frame);
	}


	/**
	 * Depending on the current keyboard controls being pressed, 
	 * a frame from one of the walking or idling animations is grabbed and returned to be rendered.
	 * @return the frame to be rendered
	 */
    public TextureRegion getWalkOrIdleFrame() {

		if(Gdx.input.isKeyPressed(Input.Keys.A)) { // LEFT
			directionFacing = Utilities.LEFT;
			return flipFrame(walkSideAnimation.getKeyFrame(stateTime, true), directionFacing);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D)) {	//RIGHT
			directionFacing = Utilities.RIGHT;
			return flipFrame(walkSideAnimation.getKeyFrame(stateTime, true), directionFacing);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.W)) {	// UP
			directionFacing = Utilities.UP;
			return walkUpAnimation.getKeyFrame(stateTime, true);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)) {	// DOWN
			directionFacing = Utilities.DOWN;
			return walkDownAnimation.getKeyFrame(stateTime, true);
		}
		else {
			return getIdleFrame();
		}
    }


	/**
	 * Checks if the frame passed in needs to be flipped to face the correct direction.
	 * If it does, it flips it and returns it.
	 * @param tempCurrentFrame the frame to check and maybe flip
	 * @param direction the direction the character is facing
	 * @return the frame facing the correct direction
	 */
	public TextureRegion flipFrame(TextureRegion tempCurrentFrame, int direction)
	{
		if((direction == Utilities.LEFT && tempCurrentFrame.isFlipX()) || (direction == Utilities.RIGHT && !tempCurrentFrame.isFlipX())) { // checks if facing left AND checks the frame is flipped OR checks if facing right AND checks if the frame is not flipped
			tempCurrentFrame.flip(true, false); //flips the frame so it is facing left
		}
		return tempCurrentFrame;
	}


	/*==========================================================================
	  ========================|| ATTACKING METHODS: ||==========================*/
	/**
	 * Gets the necessary attack animation depending on the direction the player is facing.
	 * @param directionFacing the direction the player is facing
	 * @return the animation
	 */
	public Animation<TextureRegion> getAttackAnimation(int directionFacing) {
		if(directionFacing == Utilities.LEFT || directionFacing == Utilities.RIGHT) return attackSideAnimation;
		else if(directionFacing == Utilities.UP) return attackUpAnimation;
		else return attackDownAnimation;
	}
	/**
	 * Checks if the user is currently attacking or not.
	 * @return true if attacking - false if not attacking
	 */
	public boolean attacking() {
    	return (!((getAttackAnimation(directionFacing)).isAnimationFinished(stateTime)) && Gdx.graphics.getFrameId() > 5);
    }
	/**
	 * Gets the current attack frame to be displayed.
	 * @return the attack animation frame
	 */
	public TextureRegion getAttackFrame() {
		return flipFrame((getAttackAnimation(directionFacing)).getKeyFrame(stateTime), directionFacing);
	}
	/**
	 * Restart the attack animation.
	 */
	public void restartAttackAnimation() {
		this.startedAttackAnimation = false;
	}




	/**
	 * Gets the necessary idle frame depending on the direction the player is facing.
	 * @return the frame to render
	 */
	public TextureRegion getIdleFrame() {
		if(directionFacing == Utilities.LEFT || directionFacing == Utilities.RIGHT) return flipFrame(idleSideAnimation.getKeyFrame(stateTime, true), directionFacing);
		else if(directionFacing == Utilities.UP) return idleUpAnimation.getKeyFrame(stateTime, true);
		else return idleDownAnimation.getKeyFrame(stateTime, true);
	}



	/**
	 * Incremenet the state time by the time between the current frame and previous frame.
	 */
	public void changeStateTime() {
		stateTime += Gdx.graphics.getDeltaTime();
	}

	/**
	 * Set the state time to 0.
	 */
	public void zeroStateTime() {
		stateTime = 0;
	}

	/**
	 * @param directionFacing the directionFacing to set
	 */
	public void setDirectionFacing(int directionFacing) {
		this.directionFacing = directionFacing;
	}


	


	/*=====================================================================================
	  ========================|| SETTING UP ANIMATION METHODS: ||==========================*/
    /**
	 * Goes through each animation sheet and splits it up into squares of equal sizes and
	 * then adds each sqaure to a 1d array.
	 */
	private void setupAnimations()
	{
		walkSideAnimation = createWalkingAnimation(walkSideSheet);
		walkUpAnimation = createWalkingAnimation(walkUpSheet);
		walkDownAnimation = createWalkingAnimation(walkDownSheet);

		idleSideAnimation = createIdleAnimation(idleSideSheet);
		idleUpAnimation = createIdleAnimation(idleUpSheet);
		idleDownAnimation = createIdleAnimation(idleDownSheet);

		attackSideAnimation = createAttackingAnimation(attackSideSheet);
		attackUpAnimation = createAttackingAnimation(attackUpSheet);
		attackDownAnimation = createAttackingAnimation(attackDownSheet);
	}


	/**
	 * Takes a walking texture sheet and splits it up to create and return an animation.
	 * @param walkSheet the walking sheet to create an animation from
	 * @return the created animation
	 */
	public Animation<TextureRegion> createWalkingAnimation(Texture walkSheet)
	{
		TextureRegion[][] tempTiles = TextureRegion.split(walkSheet, 64, 64);
		TextureRegion[] tempFrames = new TextureRegion[6];
		int index = 0;
		for (int i = 0; i < 4; i++) {
			tempFrames[index++] =  tempTiles[0][i];
		}
		tempFrames[index++] =  tempTiles[1][0];
		tempFrames[index] =  tempTiles[1][1];

		// 0.12f is like the speed at which it animates
		return (new Animation<TextureRegion>(0.12f, tempFrames));
	}


	/**
	 * Takes an idling texture sheet and splits it up to create and return an animation.
	 * @param idleSheet the idle sheet to create an animation from
	 * @return the created animation
	 */
	public Animation<TextureRegion> createIdleAnimation(Texture idleSheet)
	{
		TextureRegion[][] tempTiles = TextureRegion.split(idleSheet, 64, 64);
		TextureRegion[] tempFrames = new TextureRegion[5];
		int index = 0;
		for (int i = 0; i < 4; i++) {
			tempFrames[index++] =  tempTiles[0][i];
		}
		tempFrames[index] =  tempTiles[1][0];

		return (new Animation<TextureRegion>(0.45f, tempFrames));
	}


	/**
	 * Takes an attacking texture sheet and splits it up to create and return an animation.
	 * @param attackSheet the attack sheet to create an animation from
	 * @return the created animation
	 */
	public Animation<TextureRegion> createAttackingAnimation(Texture attackSheet)
	{
		TextureRegion[][] tempTiles = TextureRegion.split(attackSheet, 64, 64);
		TextureRegion[] tempFrames = new TextureRegion[3];
		int index = 0;
		for (int i = 0; i < 2; i++) {
			tempFrames[index++] =  tempTiles[0][i];
		}
		tempFrames[index] =  tempTiles[1][0];

		return (new Animation<TextureRegion>(0.12f, tempFrames));
	}

	/**
	 * Disposes of all the used textures.
	 */
	public void dispose () {
		walkSideSheet.dispose();
		walkUpSheet.dispose();
		walkDownSheet.dispose();
		idleSideSheet.dispose();
		idleUpSheet.dispose();
		idleDownSheet.dispose();
		attackSideSheet.dispose();
		attackUpSheet.dispose();
		attackDownSheet.dispose();
	}
}
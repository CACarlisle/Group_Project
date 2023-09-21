package com.dungeon.misc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;

/**
 * Class to abstract away taking input.
 */
public final class InputManager
{
    /**
     * Calculate the input vector based on what keys are being pressed.
     * @return normalised input vector
     */
    private static Vector2 calculateInputVector()
    {
        Vector2 inVec = new Vector2(0,0);

        if(Gdx.input.isKeyPressed(Input.Keys.A)) inVec.x -= 1;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) inVec.x += 1;
        if(Gdx.input.isKeyPressed(Input.Keys.W)) inVec.y += 1;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) inVec.y -= 1;

        return inVec.nor();
    }

    /**
     * Calculate the mouse position as a vector2 using the mouseX and mouseY positions
     * @return vector2 position of the mouse cursor
     */
    private static Vector2 calculateMousePosition()
    {
        return new Vector2(Gdx.input.getX(), Utilities.SCREEN_HEIGHT - Gdx.input.getY());
    }

    /**
     * Get the current input vector
     * @return the input vector for the current frame (or when it was last updated)
     */
    public static Vector2 getInputVector()
    {
        return calculateInputVector();
    }

    /**
     * Get the current mouse position
     * @return vector2 position of the mouse
     */
    public static Vector2 getMousePosition()
    {
        return calculateMousePosition();
    }

    /**
     * Gets the normalised vector from the mouse to the given vector.
     * @param from the vector
     * @return the vector from mouse to given vector
     */
    public static Vector2 getMouseVector(Vector2 from)
    {
        return calculateMousePosition().sub(from).nor();
    }
}
package com.dungeon.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Class to allow for easier debugging of various aspects of the game
 */
public final class Debugger 
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m"; 
    public static final String ANSI_YELLOW_BOLD = "\u001B[1;33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RED_BOLD = "\u001B[1;31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_GREEN_BOLD = "\u001B[1;32m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_WHITE_BOLD = "\u001B[1;37m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_BLUE_BOLD = "\u001B[1;34m";

    private static ShapeRenderer renderer;

    private static void newRendererIfNull()
    {
        if (renderer == null) {
            renderer = new ShapeRenderer();
        }
    }
    

    /**
     * Renders a debug rectangle
     * @param start Start coordinate of the rectangle
     * @param end End coordinate of the rectangle
     */
    public static void drawRect(Vector2 start, Vector2 end)
    {
        drawRect(start, end, Color.WHITE);
    }

    /**
     * Renders a debug rectangle
     * @param start Start coordinate of the rectangle
     * @param end End coordinate of the rectangle
     * @param colour colour of the rectangle
     */
    public static void drawRect(Vector2 start, Vector2 end, Color colour)
    {
        drawRect(start.x, start.y, end.x - start.x, end.y - start.y);
    }

    /**
     * Renders a debug rectangle
     * @param x x position of the rectangle
     * @param y y position of the rectangle
     * @param w width of the rectangle
     * @param h height of the rectangle
     */
    public static void drawRect(float x, float y, float w ,float h)
    {
        drawRect(x, y, w, h, Color.WHITE);
    }

    /**
     * Renders a debug rectangle in a given colour
     * @param x x position of the rectangle
     * @param y y position of the rectangle
     * @param w width of the rectangle
     * @param h height of the rectangle
     * @param colour colour of the rectangle
     */
    public static void drawRect(float x, float y, float w ,float h, Color colour)
    {
        newRendererIfNull();
        renderer.begin(ShapeType.Line);
        renderer.setColor(colour);
        renderer.rect(x, y, w, h);
        renderer.end();
    }

    public static void drawCricle(float x, float y, float radius, Color colour)
    {
        newRendererIfNull();
        renderer.begin(ShapeType.Line);
        renderer.setColor(colour);
        renderer.circle(x, y, radius);
        renderer.end();
    }

    public static void drawCricle(float x, float y, float radius)
    {
        drawCricle(x, y, radius, Color.WHITE);
    }

    public static void drawCricle(Vector2 center, float radius, Color color)
    {
        drawCricle(center.x, center.y, radius, color);
    }

    public static void drawCricle(Vector2 center, float radius)
    {
        drawCricle(center.x, center.y, radius);
    }

    public static void drawPolygon(Vector2[] points, Color colour)
    {
        float[] points_xy = new float[points.length * 2];

        for (int i=0; i<points.length; i++)
        {
            points_xy[i*2] = points[i].x;
            points_xy[i*2 + 1] = points[i].y;
        }

        drawPolygon(points_xy, colour);
    }

    public static void drawPolygon(float[] points, Color colour)
    {
        newRendererIfNull();
        renderer.begin(ShapeType.Line);
        renderer.setColor(colour);
        renderer.polygon(points);
        renderer.end();
    }

    public static void log(String message)
    {
        System.out.println(ANSI_WHITE_BOLD + "DEBUG: " + ANSI_RESET + ANSI_WHITE + message + ANSI_RESET);
    }

    public static void logError(String message)
    {
        System.out.println(ANSI_RED_BOLD + "ERROR: " + ANSI_RESET + ANSI_RED + message + ANSI_RESET);
    }

    public static void logWarning(String message)
    {
        System.out.println(ANSI_YELLOW_BOLD + "WARNING: " + ANSI_RESET + ANSI_YELLOW + message + ANSI_RESET);
    }

    public static void logInfo(String message)
    {
        System.out.println(ANSI_BLUE_BOLD + "INFO: " + ANSI_RESET + ANSI_BLUE + message + ANSI_RESET);
    }
}

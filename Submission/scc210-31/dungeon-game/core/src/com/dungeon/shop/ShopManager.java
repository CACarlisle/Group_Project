package com.dungeon.shop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.dungeon.misc.Utilities;

import java.util.Random;

/**
 * A Class used for managing the shop
 */
public class ShopManager {
    private ShopItem[] items;
    private ShopStock shopStock;

    private SpriteBatch batch;

    private int itemSpacing = 128;

    private static ShopManager instance;

    private boolean open;
    protected Random rand;

    public static ShopManager getInstance()
    {
        if (instance == null) instance = new ShopManager();

        return instance;
    }
    /**
     * Constructor for shop manager
     */
    private ShopManager()
    {
        shopStock = new ShopStock();
        batch = new SpriteBatch();
        rand = new Random();
    }   
    /**
     * A method used to stock the shop
     */
    public void stockShop()
    {
        items = new ShopItem[3];

        ShopItem si = null;
        boolean unique = false;
        for(int i=0; i <3; i++){
            
            si = shopStock.getItem();
            

            si.setPosition((Utilities.SCREEN_WIDTH/2) + (i-1) * itemSpacing - 32, Utilities.SCREEN_HEIGHT/2 - 64);

            items[i] = si;
        }
    }
    /**
     * A method used to update all shop items
     */
    public void updateAll()
    {
        for(int i=0; i<3; i++){
            if (items[i] == null) continue;

            items[i].update();
        }

        drawSprites();
    }
    /**
     * A method used to update the prices of shop items
     * @param gold the price of the items
     */
    public void updatePrices(int gold)
    {
        for(int i=0; i<3; i++){
            if (items[i] == null) continue;

            items[i].testBuyable(gold);
        }
    }
    /**
     * A method used to draw the sprites of shop items
     */
    public void drawSprites()
    {
        if (!open) return;

        batch.begin();
        for(int i=0; i<3; i++){
            if (items[i] == null) continue;
            items[i].draw(batch);
        }
        batch.end();
    }
    /**
     * An accessor method used to return the items in the shop
     * @return an array of the shop items
     */
    public ShopItem[] getItems()
    {
        return items;
    }
    /**
     * A method to show the shop
     * @param show TRUE/FALSE of whether the shop should be shown
     */
    public void showShop(boolean show)
    {
        open = show;

        if (show) {
            stockShop();
        }
    }
    /**
     * Accessor method for whether the shop is open or not
     * @return a boolean representing whether the shop is open or not
     */
    public boolean isOpen()
    {
        return open;
    }
}

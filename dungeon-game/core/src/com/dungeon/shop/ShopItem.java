package com.dungeon.shop;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * An abstract class defining a shop item
 */
public abstract class ShopItem
{
    protected String name;

    protected int price;
    protected Sprite sprite;

    private Rectangle hitbox;

    private Sprite coinSprite;
    private Sprite[] priceSprites;

    /**Chance of this item spawning in the shop. The lower the value, the less likely it is to spawn */
    protected int rarity; 

    protected boolean buyable;

    protected float x;
    protected float y;

    private float ticks;
    private float sineSpeed = 2f;

    private int priceLen;

    private Sound buyingSound;

    /**
     * Constructor for the item
     * @param name the name of the item
     * @param price the price of the item
     * @param rarity the rarity of the item
     */
    public ShopItem(String name, int price, int rarity)
    {
        if (price == 0) price = 1;

        this.name = name;
        this.price = price;
        this.rarity = rarity;

        sprite = new Sprite(new Texture("misc/no_texture.png"));
        sprite.setSize(64,64);

        coinSprite = new Sprite(new Texture("misc/gold.png"));
        coinSprite.setSize(16,16);


        priceLen = (int) (Math.log10(price) + 1);
        priceSprites = new Sprite[priceLen];

        int digit = 0;
        int i = 0;
        while(price > 0) {
            digit = price % 10;
            
            priceSprites[i] = new Sprite(new Texture("damage-font/" + digit + ".png"));
            priceSprites[i].setSize(16,24);

            price /= 10;
            i++;
        }

        x = -100;
        y = -100;

        buyable = false;

        hitbox = new Rectangle(x, y, 64, 64);

        //Initialise at a random point
        ticks = new Random().nextFloat() * 3;

        buyingSound = Gdx.audio.newSound((Gdx.files.internal("audio/buyingSound1.wav")));
    }
    /**
     * Sets the sprite texture of an item
     * @param path the path to the texture
     */
    public void setSpriteTexture(String path)
    {
        sprite.setTexture(new Texture(path));
    }
    /**
     * Tests whether the user is able to buy an item
     * @param coins The amount of coins the player has
     */
    public void testBuyable(int coins)
    {
        if (coins < price)
        {
            buyable = false;

            for (int i=0; i<priceLen; i++) {
                priceSprites[i].setColor(new Color(0xda3c47ff));
            }
        }
        else
        {
            buyable = true;

            for (int i=0; i<priceLen; i++) {
                priceSprites[i].setColor(new Color(0xFFFFFFFF));
            }
        }
    }
    /**
     * A method to buy an item from the shop, and then use it
     * @return
     */
    public boolean buy()
    {
        if (!buyable) return false;

        doEffect();
        buyingSound.setVolume(buyingSound.play(), 1f);
        return true;
    }

    public boolean getBuyable() {
        return buyable;
    }

    /**
     * Do whatever buying this item will do
     */
    protected abstract void doEffect();

    public void update()
    {
        ticks += Gdx.graphics.getDeltaTime();
        sprite.setCenter(x, y + (float) (Math.sin(sineSpeed * ticks) * 5));

        coinSprite.setCenter(x - 12 * (priceLen + 1)/2, y - 45);
        
        for (int i=0; i<priceLen; i++) {
            priceSprites[i].setCenter(x + 12 * ((priceLen + 1)/2 - i), y-45);
        }

        hitbox.setCenter(x,y);
        //Debugger.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
    }
    /**
     * A method to draw a shop item
     * @param batch
     */
    public void draw(SpriteBatch batch)
    {
        sprite.draw(batch);
        coinSprite.draw(batch);
        for (int i=0; i<priceLen; i++) {
            priceSprites[i].draw(batch);
        }
    }
    /**
     * Accessor method to return the sprite
     * @return A reference to the sprite
     */
    public Sprite getSprite()
    {
        return sprite;
    }
    /**
     * Accessor method to return the hitbox
     * @return A reference to the hitbox
     */
    public Rectangle getHitbox()
    {
        return hitbox;
    }
    /**
     * Accessor method to return the price
     * @return The price of the item
     */
    public int getPrice()
    {
        return price;
    }
    /**
     * Mutator method to set the position of the item
     * @param x the X co-ordinate of the item
     * @param y the Y co-ordinate of the item
     */
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}

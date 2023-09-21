package com.dungeon.shop;

import com.dungeon.entities.EntityManager;
import com.dungeon.ui.UI;

/**
 * A class representing health restoration items in the shop
 */
public class ItemHealthRestore extends ShopItem
{
    protected int hpValue;
    /**
     * Constructor for the health item
     * @param item the item to copy
     */
    public ItemHealthRestore(ItemHealthRestore item)
    {
        super(item.name, item.price, item.rarity);
        this.hpValue = item.hpValue;

        initSprite();
    }
    /**
     * Constructor for health item, showing the health value associated with it
     * @param hpValue
     */
    public ItemHealthRestore(int hpValue)
    {
        super("Health +" + hpValue, hpValue * 2, 100 - hpValue);

        this.hpValue = hpValue;

        initSprite();
    }
    /**
     * A method to initialise the sprite for the health item
     */
    private void initSprite()
    {
        switch (hpValue)
        {
            case 10:
            case 20:
            case 25:
            case 50:
                setSpriteTexture("shop/items/health_" + hpValue + ".png");
                break;
            default:
                setSpriteTexture("shop/items/health_mystery.png");
                price = 20;
                rarity = 75;
                break;
        }
    }
    /**
     * A method to actually heal the user of the item
     */
    protected void doEffect()
    {
        EntityManager.getInstance().getPlayer().heal(hpValue);
    }
}

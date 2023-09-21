package com.dungeon.shop;

import com.dungeon.entities.EntityManager;
import java.util.Random;

/**
 * A class representing range buff items in the shop
 */
public class ItemBuffRange extends ShopItem
{
    protected int buffValue;

    /**
     * Constructor for the copied item
     * @param item the item to copy
     */
    public ItemBuffRange(ItemBuffRange item)
    {
        super(item.name, item.price, item.rarity);
        this.buffValue = item.buffValue;
        setSpriteTexture("shop/items/buff_range.png");
    }

    /**
     * Constructor for range buff item
     * @param buffValue the value to buff damage by
     */
    public ItemBuffRange(int buffValue)
    {
        super("Buff Range +"+buffValue, 2 * buffValue, 100 - (2 * buffValue));
        this.buffValue = buffValue;
        setSpriteTexture("shop/items/buff_range.png");
    }

    /**
     * A method to actually reroll the weapon
     */
    protected void doEffect()
    {
        int randomNum = ShopManager.getInstance().rand.nextInt(2);

        if (randomNum == 0) EntityManager.getInstance().getPlayer().getEquippedL().buffRange(buffValue);
        else EntityManager.getInstance().getPlayer().getEquippedR().buffRange(buffValue);
    }
}

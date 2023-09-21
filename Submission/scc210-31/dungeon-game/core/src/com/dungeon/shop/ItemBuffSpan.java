package com.dungeon.shop;

import com.dungeon.entities.EntityManager;
import java.util.Random;

/**
 * A class representing span buff items in the shop
 */
public class ItemBuffSpan extends ShopItem
{
    protected int buffValue;

    /**
     * Constructor for the copied item
     * @param item the item to copy
     */
    public ItemBuffSpan(ItemBuffSpan item)
    {
        super(item.name, item.price, item.rarity);
        this.buffValue = item.buffValue;
        setSpriteTexture("shop/items/buff_span.png");
    }

    /**
     * Constructor for span buff item
     * @param buffValue the value to buff damage by
     */
    public ItemBuffSpan(int buffValue)
    {
        super("Buff Span +"+buffValue, 4 * buffValue, 100 - (4 * buffValue));
        this.buffValue = buffValue;
        setSpriteTexture("shop/items/buff_span.png");
    }

    /**
     * A method to actually reroll the weapon
     */
    protected void doEffect()
    {
        int randomNum = ShopManager.getInstance().rand.nextInt(2);

        if (randomNum == 0) EntityManager.getInstance().getPlayer().getEquippedL().buffSpan(buffValue);
        else EntityManager.getInstance().getPlayer().getEquippedR().buffSpan(buffValue);
    }
}

package com.dungeon.shop;

import com.dungeon.entities.EntityManager;
import java.util.Random;

/**
 * A class representing rate buff items in the shop
 */
public class ItemBuffRate extends ShopItem
{
    protected float buffValue;

    /**
     * Constructor for the copied item
     * @param item the item to copy
     */
    public ItemBuffRate(ItemBuffRate item)
    {
        super(item.name, item.price, item.rarity);
        this.buffValue = item.buffValue;
        setSpriteTexture("shop/items/buff_rate_01.png");
    }

    /**
     * Constructor for rate buff item
     * @param buffValue the value to buff damage by
     */
    public ItemBuffRate(float buffValue)
    {
        super("Buff Atttack Rate +"+buffValue, (int)(300 * buffValue), 100 - (int)(100f * buffValue));
        this.buffValue = buffValue;
        setSpriteTexture("shop/items/buff_rate_01.png");
    }

    /**
     * A method to actually reroll the weapon
     */
    protected void doEffect()
    {
        int randomNum = ShopManager.getInstance().rand.nextInt(2);

        if (randomNum == 0) EntityManager.getInstance().getPlayer().getEquippedL().buffAttackRate(buffValue);
        else EntityManager.getInstance().getPlayer().getEquippedR().buffAttackRate(buffValue);
    }
}

package com.dungeon.shop;

import com.dungeon.entities.EntityManager;
import java.util.Random;

/**
 * A class representing damage buff items in the shop
 */
public class ItemBuffDamage extends ShopItem
{
    protected int buffValue;

    /**
     * Constructor for the copied item
     * @param item the item to copy
     */
    public ItemBuffDamage(ItemBuffDamage item)
    {
        super(item.name, item.price, item.rarity);
        this.buffValue = item.buffValue;
        setSpriteTexture("shop/items/buff_damage.png");
    }

    /**
     * Constructor for damage buff item
     * @param buffValue the value to buff damage by
     */
    public ItemBuffDamage(int buffValue)
    {
        super("Buff Damage +"+buffValue, 20 * buffValue, 100 - (20 * buffValue));
        this.buffValue = buffValue;
        setSpriteTexture("shop/items/buff_damage.png");
    }

    /**
     * A method to actually reroll the weapon
     */
    protected void doEffect()
    {
        int randomNum = ShopManager.getInstance().rand.nextInt(2);

        if (randomNum == 0) EntityManager.getInstance().getPlayer().getEquippedL().buffDamage(buffValue);
        else EntityManager.getInstance().getPlayer().getEquippedR().buffDamage(buffValue);
    }
}

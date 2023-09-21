package com.dungeon.shop;

import com.dungeon.entities.EntityManager;
import com.dungeon.ui.UI;

/**
 * A class representing weapon reroll item in the shop
 */
public class ItemReroll extends ShopItem
{
    /**
     * Constructor for the copied item
     * @param item the item to copy
     */
    public ItemReroll(ItemReroll item)
    {
        super(item.name, item.price, item.rarity);
        setSpriteTexture("shop/items/new_weapon.png");
    }

    /**
     * Constructor for reroll weapon item
     */
    public ItemReroll()
    {
        super("Re-roll Weapons", 30, 50);
        setSpriteTexture("shop/items/new_weapon.png");
    }

    /**
     * A method to actually reroll the weapon
     */
    protected void doEffect()
    {
        EntityManager.getInstance().getPlayer().rerollWeapons();
        UI.getInstance().updateWeaponIcons(EntityManager.getInstance().getPlayer());
    }
}

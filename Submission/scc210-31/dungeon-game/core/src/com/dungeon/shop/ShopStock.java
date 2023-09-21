package com.dungeon.shop;

import java.util.ArrayList;
import java.util.Random;

public class ShopStock
{
    private ArrayList<ShopItem> stock;

    private Random r;

    public ShopStock()
    {
        stock = new ArrayList<ShopItem>();

        stock.add(new ItemHealthRestore(10));
        stock.add(new ItemHealthRestore(20));
        stock.add(new ItemHealthRestore(25));
        stock.add(new ItemHealthRestore(50));
        stock.add(new ItemReroll());
        stock.add(new ItemBuffDamage(1));
        stock.add(new ItemBuffRange(10));
        stock.add(new ItemBuffSpan(5));
        stock.add(new ItemBuffRate(0.1f));

        r = new Random();
    }

    /**
    * Get a stock item based on its rarity at random
    */
    public ShopItem getItem()
    {
        int total = 0;

        for (ShopItem si : stock)
        {
            total += si.rarity;
        }

        int current = 0;
        int roll = r.nextInt(total);

        ShopItem selected = null;
        for (ShopItem si : stock)
        {
            current += si.rarity;
            selected = si;
            if (current > roll) break;
        }

        if (selected instanceof ItemHealthRestore){
            return new ItemHealthRestore((ItemHealthRestore) selected);
        }
        else if (selected instanceof ItemReroll){
            return new ItemReroll((ItemReroll) selected);
        }
        else if (selected instanceof ItemBuffDamage){
            return new ItemBuffDamage((ItemBuffDamage) selected);
        }
        else if (selected instanceof ItemBuffRange){
            return new ItemBuffRange((ItemBuffRange) selected);
        }
        else if (selected instanceof ItemBuffRate){
            return new ItemBuffRate((ItemBuffRate) selected);
        }
        else if (selected instanceof ItemBuffSpan){
            return new ItemBuffSpan((ItemBuffSpan) selected);
        }

        return selected;
    }
}

package net.stln.launchersandarrows.item.util;

import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class BowPullTimeDictionary {
    private static final Map<Item, Integer> dict = new HashMap<>();

    public static Integer get(Item item) {
        return dict.get(item);
    }

    public static Map<Item, Integer> getDict() {
        return dict;
    }
}

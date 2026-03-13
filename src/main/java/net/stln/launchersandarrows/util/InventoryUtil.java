package net.stln.launchersandarrows.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InventoryUtil {
    public static ItemStack getItemInInventory(Player entity, Item item) {
        for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
            if (entity.getInventory().getItem(i).is(item)) {
                return entity.getInventory().getItem(i);
            }
        }
        return null;
    }
}

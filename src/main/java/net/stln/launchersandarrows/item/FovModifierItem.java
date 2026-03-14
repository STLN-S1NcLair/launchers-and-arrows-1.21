package net.stln.launchersandarrows.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface FovModifierItem {
    float getFovModifier(Player player, ItemStack stack);
}

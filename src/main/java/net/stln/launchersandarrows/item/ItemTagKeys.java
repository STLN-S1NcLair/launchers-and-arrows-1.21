package net.stln.launchersandarrows.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.stln.launchersandarrows.LaunchersAndArrows;

public class ItemTagKeys {
    public static final TagKey<Item> BOXED_BOLTS = TagKey.create(Registries.ITEM, LaunchersAndArrows.id("boxed_bolts"));
}

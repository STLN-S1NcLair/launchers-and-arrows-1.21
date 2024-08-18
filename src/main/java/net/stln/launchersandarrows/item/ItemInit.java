package net.stln.launchersandarrows.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.LaunchersAndArrowsDataGenerator;

public class ItemInit {

    public static final Item LONG_BOW = registerItem("long_bow", new BowItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(LaunchersAndArrows.MOD_ID, name), item);
    }

    public static void  registerModItems() {
        LaunchersAndArrows.LOGGER.info("Registering Mod Items for" + LaunchersAndArrows.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(LONG_BOW);
        });
    }
}

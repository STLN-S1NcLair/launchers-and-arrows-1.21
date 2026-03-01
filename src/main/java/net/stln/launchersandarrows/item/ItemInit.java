package net.stln.launchersandarrows.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;

public class ItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LaunchersAndArrows.MOD_ID);

    public static final DeferredItem<Item> VOLATILE_FUEL = ITEMS.registerItem("volatile_fuel",
            Item::new, new Item.Properties());
    public static final DeferredItem<Item> COOLANT = ITEMS.registerItem("coolant",
            Item::new, new Item.Properties());
    public static final DeferredItem<Item> REDSTONE_CAPACITOR = ITEMS.registerItem("redstone_capacitor",
            Item::new, new Item.Properties());
    public static final DeferredItem<Item> ACID = ITEMS.registerItem("acid",
            Item::new, new Item.Properties());
    public static final DeferredItem<Item> VISCOUS_WATER = ITEMS.registerItem("viscous_water",
            Item::new, new Item.Properties());
    public static final DeferredItem<Item> ECHO_COIL = ITEMS.registerItem("echo_coil",
            Item::new, new Item.Properties());

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}

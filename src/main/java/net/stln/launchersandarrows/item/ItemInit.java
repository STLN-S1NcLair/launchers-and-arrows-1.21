package net.stln.launchersandarrows.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.LongBowItem;
import net.stln.launchersandarrows.item.bow.ModularBowItem;
import net.stln.launchersandarrows.item.bow.MultiShotBowItem;
import net.stln.launchersandarrows.item.bow.RapidBowItem;

public class ItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LaunchersAndArrows.MOD_ID);

    // Weapons
    public static final DeferredItem<Item> LONG_BOW = ITEMS.registerItem("long_bow",
            LongBowItem::new, new Item.Properties().durability(1024));
    public static final DeferredItem<Item> RAPID_BOW = ITEMS.registerItem("rapid_bow",
            RapidBowItem::new, new Item.Properties().durability(2048));
    public static final DeferredItem<Item> MODULAR_BOW = ITEMS.registerItem("modular_bow",
            ModularBowItem::new, new Item.Properties().durability(16384));
    public static final DeferredItem<Item> MULTISHOT_BOW = ITEMS.registerItem("multishot_bow",
            MultiShotBowItem::new, new Item.Properties().durability(2048));
    // MECHANICAL_BOW
    // RAINSHOT_BOW
    // BOLT_THROWER
    // QUICK_BOLT_THROWER
    // CROSSLAUNCHER
    // HOOK_LAUNCHER
    // SLINGSHOT

    // Arrows
    // Bolts
    // Hook
    // Strings
    // Pulleys
    // Self Erosion Plate

    // Materials
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

    public static final DeferredItem<Item> METAL_ARROWHEAD = ITEMS.registerItem("metal_arrowhead",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> CUSTOMMADE_TICKET = ITEMS.registerItem("custommade_ticket",
            Item::new, new Item.Properties());

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}

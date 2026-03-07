package net.stln.launchersandarrows.item;

import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.LongBowItem;
import net.stln.launchersandarrows.item.bow.ModularBowItem;
import net.stln.launchersandarrows.item.bow.MultiShotBowItem;
import net.stln.launchersandarrows.item.bow.RapidBowItem;
import net.stln.launchersandarrows.item.launcher.CrossLauncherItem;
import net.stln.launchersandarrows.item.launcher.HookLauncherItem;
import net.stln.launchersandarrows.item.launcher.SlingShotItem;

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
    public static final DeferredItem<Item> CROSSLAUNCHER = ITEMS.registerItem("crosslauncher",
            CrossLauncherItem::new, new Item.Properties().durability(1024));
    public static final DeferredItem<Item> HOOK_LAUNCHER = ITEMS.registerItem("hook_launcher",
            HookLauncherItem::new, new Item.Properties().durability(1024));
    public static final DeferredItem<Item> SLINGSHOT = ITEMS.registerItem("slingshot",
            SlingShotItem::new, new Item.Properties().durability(1024));

    // Arrows
    // Bolts

    public static final DeferredItem<Item> GRAPPLING_HOOK = ITEMS.registerItem("grappling_hook",
            ArrowItem::new, new Item.Properties());

    // Strings
    public static final DeferredItem<Item> RANGE_STRING = ITEMS.registerItem("range_string",
            BowModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> STURDY_STRING = ITEMS.registerItem("sturdy_string",
            BowModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> LIGHTWEIGHT_STRING = ITEMS.registerItem("lightweight_string",
            BowModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> SLIMY_STRING = ITEMS.registerItem("slimy_string",
            BowModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> PRECISION_STRING = ITEMS.registerItem("precision_string",
            BowModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> OVERLOADED_STRING = ITEMS.registerItem("overloaded_string",
            BowModifierItem::new, new Item.Properties());

    // Pulleys

    public static final DeferredItem<Item> SELF_EROSION_PLATE = ITEMS.registerItem("self_erosion_plate",
            SelfErosionPlateItem::new, new Item.Properties());

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

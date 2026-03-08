package net.stln.launchersandarrows.item;

import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.*;
import net.stln.launchersandarrows.item.launcher.*;
import net.stln.launchersandarrows.item.util.AttributeEffectsDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.util.AttributeEnum;
import net.stln.launchersandarrows.util.ModifierEnum;

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

    public static final DeferredItem<Item> MECHANICAL_BOW = ITEMS.registerItem("mechanical_bow",
            MechanicalBowItem::new, new Item.Properties().durability(4096));

    public static final DeferredItem<Item> RAINSHOT_BOW = ITEMS.registerItem("rainshot_bow",
            RainShotBowItem::new, new Item.Properties().durability(16384));

    public static final DeferredItem<Item> BOLT_THROWER = ITEMS.registerItem("bolt_thrower",
            BoltThrowerItem::new, new Item.Properties().durability(3072));

    public static final DeferredItem<Item> QUICK_BOLT_THROWER = ITEMS.registerItem("quick_bolt_thrower",
            QuickBoltThrowerItem::new, new Item.Properties().durability(16384));

    public static final DeferredItem<Item> CROSSLAUNCHER = ITEMS.registerItem("crosslauncher",
            CrossLauncherItem::new, new Item.Properties().durability(1024));

    public static final DeferredItem<Item> HOOK_LAUNCHER = ITEMS.registerItem("hook_launcher",
            HookLauncherItem::new, new Item.Properties().durability(1024));

    public static final DeferredItem<Item> SLINGSHOT = ITEMS.registerItem("slingshot",
            SlingShotItem::new, new Item.Properties().durability(1024));

    // Arrows
    // Boxed Bolts
    public static final DeferredItem<Item> BOXED_BOLTS = ITEMS.registerItem("boxed_bolts",
            BoltItem::new, new Item.Properties());
    public static final DeferredItem<Item> BOXED_EXPLOSIVE_BOLTS = ITEMS.registerItem("boxed_explosive_bolts",
            BoltItem::new, new Item.Properties());

    // Grappling Hook
    public static final DeferredItem<Item> GRAPPLING_HOOK = ITEMS.registerItem("grappling_hook",
            ArrowItem::new, new Item.Properties());

    // Strings
    // ├ Attribute Modifiers

    // └ Modifiers
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
    // ├ Attribute Modifiers

    // └ Modifiers
    public static final DeferredItem<Item> COMPOUND_PULLEY = ITEMS.registerItem("compound_pulley",
            BoltThrowerModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> REINFORCED_PULLEY = ITEMS.registerItem("reinforced_pulley",
            BoltThrowerModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> LUBRICATION_PULLEY = ITEMS.registerItem("lubrication_pulley",
            BoltThrowerModifierItem::new, new Item.Properties());
    public static final DeferredItem<Item> POWERED_PULLEY = ITEMS.registerItem("powered_pulley",
            BoltThrowerModifierItem::new, new Item.Properties());

    // Self Erosion Plate
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

    public static void registerAttributeEffects() {
        // Arrows

        // Bolts

        // Vanilla Items
        AttributeEffectsDictionary.registerToDict(Items.MAGMA_CREAM, AttributeEnum.FLAME.get(), 5);
        AttributeEffectsDictionary.registerToDict(Items.SNOWBALL, AttributeEnum.FROST.get(), 5);
        AttributeEffectsDictionary.registerToDict(Items.LIGHTNING_ROD, AttributeEnum.LIGHTNING.get(), 5);
        AttributeEffectsDictionary.registerToDict(Items.SLIME_BALL, AttributeEnum.ACID.get(), 3);
        AttributeEffectsDictionary.registerToDict(Items.ECHO_SHARD, AttributeEnum.ECHO.get(), 175);
        AttributeEffectsDictionary.registerToDict(Items.HEART_OF_THE_SEA, AttributeEnum.FLOOD.get(), 175);
        AttributeEffectsDictionary.registerToDict(Items.HEAVY_CORE, AttributeEnum.INJURY.get(), 10);
        AttributeEffectsDictionary.registerToDict(Items.POINTED_DRIPSTONE, AttributeEnum.INJURY.get(), 3);

        // Strings
        // ├ Attribute Modifiers

        // └ Modifiers
        ModifierDictionary.registerToDict(RANGE_STRING.get(), ModifierEnum.RANGE.get(), 25);
        ModifierDictionary.registerToDict(STURDY_STRING.get(), ModifierEnum.STURDY.get(), 25);
        ModifierDictionary.registerToDict(LIGHTWEIGHT_STRING.get(), ModifierEnum.LIGHTWEIGHT.get(), 15);
        ModifierDictionary.registerToDict(SLIMY_STRING.get(), ModifierEnum.RICOCHET.get(), 10);
        ModifierDictionary.registerToDict(PRECISION_STRING.get(), ModifierEnum.PRECISION.get(), 50);
        ModifierDictionary.registerToDict(OVERLOADED_STRING.get(), ModifierEnum.RANGE.get(), 40);
        ModifierDictionary.registerToDict(OVERLOADED_STRING.get(), ModifierEnum.LIGHTWEIGHT.get(), 10);
        ModifierDictionary.registerToDict(OVERLOADED_STRING.get(), ModifierEnum.PRECISION.get(), -100);

        // Pulleys
        // ├ Attribute Modifiers

        // └ Modifiers
        ModifierDictionary.registerToDict(COMPOUND_PULLEY.get(), ModifierEnum.RANGE.get(), 15);
        ModifierDictionary.registerToDict(REINFORCED_PULLEY.get(), ModifierEnum.STURDY.get(), 30);
        ModifierDictionary.registerToDict(LUBRICATION_PULLEY.get(), ModifierEnum.LIGHTWEIGHT.get(), 25);
        ModifierDictionary.registerToDict(POWERED_PULLEY.get(), ModifierEnum.CAPACITY.get(), 50);
        ModifierDictionary.registerToDict(POWERED_PULLEY.get(), ModifierEnum.LIGHTWEIGHT.get(), -15);
    }
}

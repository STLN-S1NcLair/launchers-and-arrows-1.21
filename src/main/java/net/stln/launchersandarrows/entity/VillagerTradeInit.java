package net.stln.launchersandarrows.entity;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.ItemInit;

import java.util.Optional;

public class VillagerTradeInit {
    public static void registerVillagerTrades() {
        LaunchersAndArrows.LOGGER.info("Registering Villager Trades for " + LaunchersAndArrows.MOD_ID);
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 1, factories -> {
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(Items.STICK, 30), new ItemStack(ItemInit.METAL_ARROWHEAD, 1),
                        20, 7, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(Items.FEATHER, 2), new ItemStack(ItemInit.METAL_ARROWHEAD, 1),
                        30, 7, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(Items.FLINT, 10), new ItemStack(ItemInit.METAL_ARROWHEAD, 1),
                        10, 7, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(Items.IRON_INGOT, 1), new ItemStack(ItemInit.METAL_ARROWHEAD, 3),
                        5, 7, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.IGNITION_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.FROSTBITE_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.CHARGING_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.DETERIORATION_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.PERMEATION_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.VIBRATING_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.RANGE_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.STURDY_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.LIGHTWEIGHT_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.SLIMY_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.PRECISION_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.OVERLOADED_STRING, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.IGNITION_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.COOLING_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.POWER_GENERATION_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.CORROSION_RESISTANT_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.HYDROPHILIC_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.CONDUCTION_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.COMPOUND_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.REINFORCED_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.LUBRICATION_PULLEY, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.CUSTOMMADE_TICKET, 1), new ItemStack(ItemInit.POWERED_PULLEY, 1),
                        2, 20, 0.05F)));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 2, factories -> {
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 15), new ItemStack(ItemInit.RAPID_BOW, 1),
                        2, 10, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 30), new ItemStack(ItemInit.LONG_BOW, 1),
                        2, 10, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 20), new ItemStack(ItemInit.MULTISHOT_BOW, 1),
                        2, 10, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 50),
                        Optional.of(new TradedItem(Items.NETHERITE_INGOT, 1)), new ItemStack(ItemInit.MODULAR_BOW, 1),
                        2, 10, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 50),
                        Optional.of(new TradedItem(Items.ECHO_SHARD, 1)), new ItemStack(ItemInit.RAINSHOT_BOW, 1),
                        2, 10, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 30), new ItemStack(ItemInit.SLINGSHOT, 1),
                        2, 10, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 30), new ItemStack(ItemInit.HOOK_LAUNCHER, 1),
                        2, 10, 0.05F)));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 3, factories -> {
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 1), new ItemStack(ItemInit.VOLATILE_FUEL, 5),
                        20, 5, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 1), new ItemStack(ItemInit.COOLANT, 5),
                        20, 5, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 1), new ItemStack(ItemInit.REDSTONE_CAPACITOR, 5),
                        20, 5, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 1), new ItemStack(ItemInit.ACID, 5),
                        20, 5, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 1), new ItemStack(ItemInit.VISCOUS_WATER, 5),
                        20, 5, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 1), new ItemStack(ItemInit.ECHO_COIL, 5),
                        20, 5, 0.05F)));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 4, factories -> {
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 64),
                        Optional.of(new TradedItem(ItemInit.METAL_ARROWHEAD, 36)), new ItemStack(ItemInit.CROSSLAUNCHER, 1),
                        2, 20, 0.05F)));
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 64),
                        Optional.of(new TradedItem(ItemInit.METAL_ARROWHEAD, 36)), new ItemStack(ItemInit.BOLT_THROWER, 1),
                        2, 20, 0.05F)));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 5, factories -> {
            factories.add(((entity, random) ->
                new TradeOffer(new TradedItem(ItemInit.METAL_ARROWHEAD, 40),
                        Optional.of(new TradedItem(Items.DIAMOND, 1)), new ItemStack(ItemInit.CUSTOMMADE_TICKET, 1),
                        2, 20, 0.05F)));
        });
    }
}

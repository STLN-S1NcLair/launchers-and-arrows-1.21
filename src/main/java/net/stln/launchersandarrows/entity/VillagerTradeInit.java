package net.stln.launchersandarrows.entity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.ItemInit;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = LaunchersAndArrows.MOD_ID)
public class VillagerTradeInit {
    @SubscribeEvent
    public static void registerVillagerTrades(VillagerTradesEvent event){
        if(event.getType() == VillagerProfession.FLETCHER){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            /*
            =========
            LEVEL 1
            =========
             */

            trades.get(1).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(Items.STICK, 30),
                            new ItemStack(ItemInit.METAL_ARROWHEAD.get(), 1),
                            20, 7, 0.05F));

            trades.get(1).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(Items.FEATHER, 2),
                            new ItemStack(ItemInit.METAL_ARROWHEAD.get(), 1),
                            30, 7, 0.05F));

            trades.get(1).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(Items.FLINT, 10),
                            new ItemStack(ItemInit.METAL_ARROWHEAD.get(), 1),
                            10, 7, 0.05F));

            trades.get(1).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(Items.IRON_INGOT, 1),
                            new ItemStack(ItemInit.METAL_ARROWHEAD.get(), 3),
                            5, 7, 0.05F));

            /*
            CUSTOM TICKET -> STRINGS
             */

            trades.get(1).add((e, r) -> new MerchantOffer(
                    new ItemCost(ItemInit.CUSTOMMADE_TICKET.get()),
                    new ItemStack(ItemInit.IGNITION_STRING.get()),
                    2, 20, 0.05F));

            trades.get(1).add((e, r) -> new MerchantOffer(
                    new ItemCost(ItemInit.CUSTOMMADE_TICKET.get()),
                    new ItemStack(ItemInit.FROSTBITE_STRING.get()),
                    2, 20, 0.05F));

            trades.get(1).add((e, r) -> new MerchantOffer(
                    new ItemCost(ItemInit.CUSTOMMADE_TICKET.get()),
                    new ItemStack(ItemInit.CHARGING_STRING.get()),
                    2, 20, 0.05F));

            trades.get(1).add((e, r) -> new MerchantOffer(
                    new ItemCost(ItemInit.CUSTOMMADE_TICKET.get()),
                    new ItemStack(ItemInit.DETERIORATION_STRING.get()),
                    2, 20, 0.05F));

            /*
            =========
            LEVEL 2
            =========
             */

            trades.get(2).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 15),
                            new ItemStack(ItemInit.RAPID_BOW.get()),
                            2, 10, 0.05F));

            trades.get(2).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 30),
                            new ItemStack(ItemInit.LONG_BOW.get()),
                            2, 10, 0.05F));

            trades.get(2).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 20),
                            new ItemStack(ItemInit.MULTISHOT_BOW.get()),
                            2, 10, 0.05F));

            trades.get(2).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 50),
                            Optional.of(new ItemCost(Items.NETHERITE_INGOT)),
                            new ItemStack(ItemInit.MODULAR_BOW.get()),
                            2, 10, 0.05F));

            trades.get(2).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 50),
                            Optional.of(new ItemCost(Items.ECHO_SHARD)),
                            new ItemStack(ItemInit.RAINSHOT_BOW.get()),
                            2, 10, 0.05F));

            trades.get(2).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 30),
                            new ItemStack(ItemInit.SLINGSHOT.get()),
                            2, 10, 0.05F));

            trades.get(2).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 30),
                            new ItemStack(ItemInit.HOOK_LAUNCHER.get()),
                            2, 10, 0.05F));

            /*
            =========
            LEVEL 3
            =========
             */

            trades.get(3).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 1),
                            new ItemStack(ItemInit.VOLATILE_FUEL.get(), 5),
                            20, 5, 0.05F));

            trades.get(3).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 1),
                            new ItemStack(ItemInit.COOLANT.get(), 5),
                            20, 5, 0.05F));

            /*
            =========
            LEVEL 4
            =========
             */

            trades.get(4).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 64),
                            Optional.of(new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 36)),
                            new ItemStack(ItemInit.CROSSLAUNCHER.get()),
                            2, 20, 0.05F));

            trades.get(4).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 64),
                            Optional.of(new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 36)),
                            new ItemStack(ItemInit.BOLT_THROWER.get()),
                            2, 20, 0.05F));

            /*
            =========
            LEVEL 5
            =========
             */

            trades.get(5).add((entity, random) ->
                    new MerchantOffer(
                            new ItemCost(ItemInit.METAL_ARROWHEAD.get(), 40),
                            Optional.of(new ItemCost(Items.DIAMOND)),
                            new ItemStack(ItemInit.CUSTOMMADE_TICKET.get()),
                            2, 20, 0.05F));
        }
    }
}

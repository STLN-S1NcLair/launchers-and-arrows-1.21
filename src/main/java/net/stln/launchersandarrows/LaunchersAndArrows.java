package net.stln.launchersandarrows;

import net.minecraft.resources.ResourceLocation;
import net.stln.launchersandarrows.creative_tab.CreativeTabInit;
import net.stln.launchersandarrows.entity.EntityInit;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.sound.SoundInit;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(LaunchersAndArrows.MOD_ID)
public class LaunchersAndArrows {
    public static final String MOD_ID = "launchers_and_arrows";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LaunchersAndArrows(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ComponentInit.registerComponents(modEventBus);

        ItemInit.register(modEventBus);
        CreativeTabInit.register(modEventBus);
        EntityInit.registerModEntities(modEventBus);
        SoundInit.registerSoundEvents(modEventBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ItemInit.registerAttributeEffects();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}

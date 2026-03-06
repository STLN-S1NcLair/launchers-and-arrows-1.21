package net.stln.launchersandarrows;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.stln.launchersandarrows.entity.EntityInit;
import net.stln.launchersandarrows.item.CustomModelPredicateProvider;

@Mod(value = LaunchersAndArrows.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = LaunchersAndArrows.MOD_ID, value = Dist.CLIENT)
public class LaunchersAndArrowsClient {
    public LaunchersAndArrowsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        CustomModelPredicateProvider.registerModModels();
        EntityInit.registerModEntitiesRenderer();
    }
}

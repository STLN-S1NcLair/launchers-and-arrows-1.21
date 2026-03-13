package net.stln.launchersandarrows.client.hud;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.stln.launchersandarrows.LaunchersAndArrows;

@EventBusSubscriber(modid = LaunchersAndArrows.MOD_ID, value = Dist.CLIENT)
public class HudInit {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LaunchersAndArrows.id("bolt_thrower_info"), new BoltThrowerInfoOverlay());
        event.registerAboveAll(LaunchersAndArrows.id("mechanical_bow_info"), new MechanicalBowInfoOverlay());
        event.registerAboveAll(LaunchersAndArrows.id("attribute_effect_info"), new AttributeEffectInfoOverlay());
    }
}

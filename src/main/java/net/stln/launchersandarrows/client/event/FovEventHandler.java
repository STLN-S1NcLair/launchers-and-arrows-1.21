package net.stln.launchersandarrows.client.event;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.FovModifierItem;

@EventBusSubscriber(modid = LaunchersAndArrows.MOD_ID, value = Dist.CLIENT)
public class FovEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onFovModifier(ComputeFovModifierEvent event) {

        Player player = event.getPlayer();
        ItemStack stack = player.getUseItem();

        if (stack.getItem() instanceof FovModifierItem item) {

            float fov = item.getFov();
            item.resetFov();

            if (player.getAbilities().flying) {
                fov *= 1.1F;
            }

            double speed = player.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float walkSpeed = player.getAbilities().getWalkingSpeed();

            fov *= ((float)speed / walkSpeed + 1.0F) / 2.0F;

            if (walkSpeed == 0.0F || Float.isNaN(fov) || Float.isInfinite(fov)) {
                fov = 1.0F;
            }

            event.setNewFovModifier(fov);
        }
    }
}

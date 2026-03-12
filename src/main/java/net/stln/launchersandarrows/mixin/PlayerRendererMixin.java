package net.stln.launchersandarrows.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> info) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) {

        }
        else {
            if (player.getUsedItemHand() == hand && player.getUseItemRemainingTicks() > 0) {

            }
            else if (!player.swinging && itemStack.getItem() instanceof BoltThrowerItem && BoltThrowerItem.isCharged(itemStack)){
                info.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
            }
        }
    }
}

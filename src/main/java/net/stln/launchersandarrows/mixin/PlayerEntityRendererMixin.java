package net.stln.launchersandarrows.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.item.launcher.CrossLauncherItem;
import net.stln.launchersandarrows.item.launcher.HookLauncherItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!player.handSwinging && (itemStack.isOf(ItemInit.CROSSLAUNCHER) || itemStack.isOf(ItemInit.QUICK_BOLT_THROWER)) && CrossLauncherItem.isCharged(itemStack)) {
            cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        } else if (!player.handSwinging && itemStack.isOf(ItemInit.HOOK_LAUNCHER) && HookLauncherItem.isCharged(itemStack)) {
            cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        } else if (!player.handSwinging && itemStack.isOf(ItemInit.BOLT_THROWER) && itemStack.get(ModComponentInit.BOLT_COUNT_COMPONENT) > 0) {
            cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        }
    }
}

package net.stln.launchersandarrows.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.item.launcher.SlingShotItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemInHandRenderer.class)
public class RenderFirstPersonItemMixin {

    @Shadow
    public void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed) {

    }

    @Shadow
    private void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProg) {

    }

    @Shadow
    private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm hand, float swingProgress) {

    }

    @ModifyExpressionValue(
            method = "evaluateWhichHandsToRender",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z")
    )
    private static boolean laA$patch(boolean original, @Local(index = 1) ItemStack main, @Local(index = 2) ItemStack off) {
        return main.getItem() instanceof BowItem || off.getItem() instanceof BowItem || main.getItem() instanceof CrossbowItem || off.getItem() instanceof  CrossbowItem;
    }

    @Redirect(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z")
    )
    private static boolean launchers_and_arrows$replaceCheck(ItemStack stack, Item item) {
        if (item == Items.BOW) {
            return stack.getItem() instanceof BowItem;
        }

        if (item == Items.CROSSBOW) {
            return stack.getItem() instanceof CrossbowItem;
        }
        return stack.is(item);
    }


    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void renderArmWithItem(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo info) {
        if (!player.isScoping()) {
            if(stack.getItem() instanceof BoltThrowerItem boltThrowerItem){
                boolean flag = hand == InteractionHand.MAIN_HAND;
                HumanoidArm humanoidarm = flag ? player.getMainArm() : player.getMainArm().getOpposite();
                poseStack.pushPose();

                boolean flag3 = stack.get(ComponentInit.BOLT_COUNT_COMPONENT) != 0;
                boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
                int i = flag2 ? 1 : -1;
                if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                    this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                    poseStack.translate((float)i * -0.4785682F, -0.094387F, 0.05731531F);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-11.935F));
                    poseStack.mulPose(Axis.YP.rotationDegrees((float)i * 65.3F));
                    poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * -9.785F));
                    float f12 = (float)stack.getUseDuration(player) - ((float)player.getUseItemRemainingTicks() - partialTicks + 1.0F);
                    float f7 = f12 / (float)boltThrowerItem.getTickUntilMaxCharge(stack);
                    if (f7 > 1.0F) {
                        f7 = 1.0F;
                    }

                    if (f7 > 0.1F) {
                        float f11 = Mth.sin((f12 - 0.1F) * 1.3F);
                        float f14 = f7 - 0.1F;
                        float f17 = f11 * f14;
                        poseStack.translate(f17 * 0.0F, f17 * 0.004F, f17 * 0.0F);
                    }

                    poseStack.translate(f7 * 0.0F, f7 * 0.0F, f7 * 0.04F);
                    poseStack.scale(1.0F, 1.0F, 1.0F + f7 * 0.2F);
                    poseStack.mulPose(Axis.YN.rotationDegrees((float)i * 45.0F));
                } else {
                    float f12 = -0.4F * Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
                    float f7 = 0.2F * Mth.sin(Mth.sqrt(swingProgress) * 6.2831855F);
                    float f11 = -0.2F * Mth.sin(swingProgress * 3.1415927F);
                    poseStack.translate((float)i * f12, f7, f11);
                    this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                    this.applyItemArmAttackTransform(poseStack, humanoidarm, swingProgress);
                    if (flag3 && swingProgress < 0.001F && flag) {
                        poseStack.translate((float)i * -0.641864F, 0.0F, 0.0F);
                        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * 10.0F));
                    }
                }
                this.renderItem(player, stack, flag2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !flag2, poseStack, buffer, combinedLight);
                poseStack.popPose();
                info.cancel();
            }
            else if(stack.getItem() instanceof SlingShotItem slingshotItem){
                boolean flag = hand == InteractionHand.MAIN_HAND;
                HumanoidArm humanoidarm = flag ? player.getMainArm() : player.getMainArm().getOpposite();
                poseStack.pushPose();

                boolean flag3 = SlingShotItem.isCharged(stack);
                boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
                int i = flag2 ? 1 : -1;
                if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                    this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                    poseStack.translate((float)i * -0.4785682F, -0.094387F, 0.05731531F);
                    poseStack.mulPose(Axis.YP.rotationDegrees((float)i * 45));
                    float f12 = (float)stack.getUseDuration(player) - ((float)player.getUseItemRemainingTicks() - partialTicks + 1.0F);
                    float f7 = f12 / (float)slingshotItem.getPulltime();
                    if (f7 > 1.0F) {
                        f7 = 1.0F;
                    }

                    if (f7 > 0.1F) {
                        float f11 = Mth.sin((f12 - 0.1F) * 1.3F);
                        float f14 = f7 - 0.1F;
                        float f17 = f11 * f14;
                        poseStack.translate(f17 * 0.0F, f17 * 0.004F, f17 * 0.0F);
                    }

                    poseStack.translate(f7 * 0.0F, f7 * 0.0F, f7 * 0.04F);
                    poseStack.scale(1.0F, 1.0F, 1.0F + f7 * 0.2F);
                } else {
                    float f12 = -0.4F * Mth.sin(Mth.sqrt(swingProgress) * 3.1415927F);
                    float f7 = 0.2F * Mth.sin(Mth.sqrt(swingProgress) * 6.2831855F);
                    float f11 = -0.2F * Mth.sin(swingProgress * 3.1415927F);
                    poseStack.translate((float)i * f12, f7, f11);
                    this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                    this.applyItemArmAttackTransform(poseStack, humanoidarm, swingProgress);
                    if (flag3 && swingProgress < 0.001F && flag) {
                        poseStack.translate((float)i * -0.641864F, 0.0F, 0.0F);
                        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * 10.0F));
                    }
                }
                this.renderItem(player, stack, flag2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !flag2, poseStack, buffer, combinedLight);
                poseStack.popPose();
                info.cancel();
            }
        }

    }

    @Unique
    private static boolean laA$isCharged(ItemStack stack) {
        if(stack.getItem() instanceof CrossbowItem){
            return CrossbowItem.isCharged(stack);
        }
        else if(stack.getItem() instanceof BoltThrowerItem){
            return BoltThrowerItem.isCharged(stack);
        }
        return false;
    }

    @Inject(method = "isChargedCrossbow", at = @At("HEAD"), cancellable = true)
    private static void isChargedCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean flag = false;
        if(stack.getItem() instanceof CrossbowItem){
            flag = CrossbowItem.isCharged(stack);
        }
        else if(stack.getItem() instanceof BoltThrowerItem){
            flag = BoltThrowerItem.isCharged(stack);
        }
        cir.setReturnValue(flag);
    }
}

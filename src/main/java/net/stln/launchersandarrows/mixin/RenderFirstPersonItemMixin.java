package net.stln.launchersandarrows.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.bow.ModfiableBowItem;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.item.launcher.SlingShotItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public class RenderFirstPersonItemMixin {

    @Shadow
    private void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress) {
    }

    @Shadow
    private void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress) {
    }

    @Shadow
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode,
                           boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
    }

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    public void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch,
                                      Hand hand, float swingProgress, ItemStack item, float equipProgress,
                                      MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (item.isOf(ItemInit.CROSSLAUNCHER) || item.isOf(ItemInit.HOOK_LAUNCHER)) {

            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            matrices.push();

            boolean bl2 = CrossbowItem.isCharged(item);
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
                this.applyEquipOffset(matrices, arm, equipProgress);
                matrices.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 65.3F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) i * -9.785F));
                float f = (float) item.getMaxUseTime(player) - ((float) player.getItemUseTimeLeft() - tickDelta + 1.0F);
                float g = f / (float) CrossbowItem.getPullTime(item, player);
                if (g > 1.0F) {
                    g = 1.0F;
                }

                if (g > 0.1F) {
                    float h = MathHelper.sin((f - 0.1F) * 1.3F);
                    float j = g - 0.1F;
                    float k = h * j;
                    matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
                }

                matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
                matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) i * 45.0F));
            } else {
                float fx = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                float gx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
                float h = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
                matrices.translate((float) i * fx, gx, h);
                this.applyEquipOffset(matrices, arm, equipProgress);
                this.applySwingOffset(matrices, arm, swingProgress);
                if (bl2 && swingProgress < 0.001F && bl) {
                    matrices.translate((float) i * -0.641864F, 0.0F, 0.0F);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 10.0F));
                }
            }

            this.renderItem(
                    player,
                    item,
                    bl3 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                    !bl3,
                    matrices,
                    vertexConsumers,
                    light
            );
            matrices.pop();
            ci.cancel();
        } else if (item.isOf(ItemInit.SLINGSHOT)) {
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            matrices.push();

            boolean bl2 = CrossbowItem.isCharged(item);
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
                this.applyEquipOffset(matrices, arm, equipProgress);
                matrices.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 45));
                float f = (float) item.getMaxUseTime(player) - ((float) player.getItemUseTimeLeft() - tickDelta + 1.0F);
                float g = f / (float) ((ModfiableBowItem) item.getItem()).getPulltime();
                if (g > 1.0F) {
                    g = 1.0F;
                }

                if (g > 0.1F) {
                    float h = MathHelper.sin((f - 0.1F) * 1.3F);
                    float j = g - 0.1F;
                    float k = h * j;
                    matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
                }

                matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
                matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
            } else {
                float fx = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                float gx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
                float h = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
                matrices.translate((float) i * fx, gx, h);
                this.applyEquipOffset(matrices, arm, equipProgress);
                this.applySwingOffset(matrices, arm, swingProgress);
                if (bl2 && swingProgress < 0.001F && bl) {
                    matrices.translate((float) i * -0.641864F, 0.0F, 0.0F);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 10.0F));
                }
            }

            this.renderItem(
                    player,
                    item,
                    bl3 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                    !bl3,
                    matrices,
                    vertexConsumers,
                    light
            );
            matrices.pop();
            ci.cancel();
        } else if (item.isOf(ItemInit.BOLT_THROWER) || item.isOf(ItemInit.QUICK_BOLT_THROWER)) {

            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            matrices.push();

            boolean bl2 = item.get(ModComponentInit.BOLT_COUNT_COMPONENT) != 0;
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand && item.get(ModComponentInit.BOLT_COUNT_COMPONENT) == 0) {
                this.applyEquipOffset(matrices, arm, equipProgress);
                matrices.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 65.3F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) i * -9.785F));
                float f = (float) item.getMaxUseTime(player) - ((float) player.getItemUseTimeLeft() - tickDelta + 1.0F);
                float g = f / (float) ((BoltThrowerItem)item.getItem()).getTickUntilMaxCharge(item);
                if (g > 1.0F) {
                    g = 1.0F;
                }

                if (g > 0.1F) {
                    float h = MathHelper.sin((f - 0.1F) * 1.3F);
                    float j = g - 0.1F;
                    float k = h * j;
                    matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
                }

                matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
                matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) i * 45.0F));
            } else {
                float fx = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                float gx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
                float h = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
                matrices.translate((float) i * fx, gx, h);
                this.applyEquipOffset(matrices, arm, equipProgress);
                this.applySwingOffset(matrices, arm, swingProgress);
                if (bl2 && swingProgress < 0.001F && bl) {
                    matrices.translate((float) i * -0.641864F, 0.0F, 0.0F);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 10.0F));
                }
            }

            this.renderItem(
                    player,
                    item,
                    bl3 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                    !bl3,
                    matrices,
                    vertexConsumers,
                    light
            );
            matrices.pop();
            ci.cancel();
        }
    }
}

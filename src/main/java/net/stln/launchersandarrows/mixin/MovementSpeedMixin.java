package net.stln.launchersandarrows.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.RainShotBowItem;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({ClientPlayerEntity.class})
public class MovementSpeedMixin {

    @Shadow
    public Input input;

    @Unique
    ClientPlayerEntity entity = (ClientPlayerEntity)(Object)this;

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getTutorialManager()Lnet/minecraft/client/tutorial/TutorialManager;", shift = At.Shift.AFTER))
    private void modifyMovementSpeed(CallbackInfo ci) {
        if (entity.getMainHandStack().getItem() instanceof BoltThrowerItem
                || entity.getOffHandStack().getItem() instanceof BoltThrowerItem) {
            entity.input.movementForward *= 0.5F;
            entity.input.movementSideways *= 0.5F;
        } else if (entity.getMainHandStack().getItem() instanceof RainShotBowItem
                || entity.getOffHandStack().getItem() instanceof RainShotBowItem) {
            entity.input.movementForward *= 5F;
            entity.input.movementSideways *= 5F;
        }
    }
}

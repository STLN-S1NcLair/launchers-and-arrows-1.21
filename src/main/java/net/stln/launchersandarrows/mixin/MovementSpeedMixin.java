package net.stln.launchersandarrows.mixin;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.launchersandarrows.item.bow.RainShotBowItem;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.item.launcher.QuickBoltThrowerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(LocalPlayer.class)
public class MovementSpeedMixin {
    @Shadow
    public Input input;

    @Unique
    LocalPlayer player = (LocalPlayer) (Object) this;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getTutorial()Lnet/minecraft/client/tutorial/Tutorial;", shift = At.Shift.AFTER))
    private void modifyMovementSpeed(CallbackInfo info){
        Item main = player.getMainHandItem().getItem();
        Item off = player.getOffhandItem().getItem();
        if(main instanceof BoltThrowerItem || off instanceof BoltThrowerItem){
            if(main instanceof QuickBoltThrowerItem || off instanceof QuickBoltThrowerItem){
                if(player.isUsingItem()){
                    player.input.forwardImpulse *= 5F;
                    player.input.leftImpulse *= 5F;
                }
            }
            else {
                player.input.forwardImpulse *= 0.75F;
                player.input.leftImpulse *= 0.75F;
            }
        }
        else if(main instanceof RainShotBowItem || off instanceof RainShotBowItem){
            if(player.isUsingItem()){
                player.input.forwardImpulse *= 5F;
                player.input.leftImpulse *= 5F;
            }
            if(player.isShiftKeyDown()){
                player.input.forwardImpulse *= 3F;
                player.input.leftImpulse *= 3F;
            }
        }
    }
}

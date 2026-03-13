package net.stln.launchersandarrows.mixin.projectile;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.stln.launchersandarrows.entity.projectile.Bolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class CancelKnockbackMixin {

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
    private double cancelKnockback(double strength, @Local(argsOnly = true)DamageSource source){
        if(source.getDirectEntity() instanceof Bolt){
            return strength / 10;
        }
        return strength;
    }
}

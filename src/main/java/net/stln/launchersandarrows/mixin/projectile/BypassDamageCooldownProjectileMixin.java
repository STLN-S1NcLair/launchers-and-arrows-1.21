package net.stln.launchersandarrows.mixin.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class BypassDamageCooldownProjectileMixin implements BypassDamageCooldownProjectile{
    @Unique
    private boolean launchersandarrows$bypassDamageCooldown = false;

    @Override
    public boolean getBypass() {
        return launchersandarrows$bypassDamageCooldown;
    }

    @Override
    public void setBypass(boolean flag) {
        launchersandarrows$bypassDamageCooldown = flag;
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void bypassInvulnerability(EntityHitResult hitResult, CallbackInfo ci) {

        Entity target = hitResult.getEntity();

        if (getBypass() && target instanceof LivingEntity living) {
            living.invulnerableTime = 0;
        }
    }
}

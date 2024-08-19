package net.stln.launchersandarrows.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class BypassDamageCooldownProjectileMixin extends Entity implements BypassDamageCooldownProjectile {

    TrackedData<Boolean> BYPASS_DAMAGE_COOLDOWN = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public BypassDamageCooldownProjectileMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setBypass(boolean flag) {
        this.dataTracker.set(BYPASS_DAMAGE_COOLDOWN, flag);
    }

    @Override
    public boolean getBypass() {
        return this.dataTracker.get(BYPASS_DAMAGE_COOLDOWN);
    }

    @Inject(method = "initDataTracker", at = {@At("HEAD")})
    protected void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(BYPASS_DAMAGE_COOLDOWN, false);
    }

    @Inject(method = "onEntityHit", at = {@At("HEAD")})
    protected void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity target = entityHitResult.getEntity();
        if (getBypass() && target instanceof LivingEntity) {
            target.timeUntilRegen = 0;
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = {@At("TAIL")})
    protected void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("BypassDamageCooldown", getBypass());
    }

    @Inject(method = "readCustomDataFromNbt", at = {@At("TAIL")})
    protected void readCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        setBypass(nbt.getBoolean("BypassDamageCooldown"));
    }
}

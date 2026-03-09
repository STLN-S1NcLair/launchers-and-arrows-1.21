package net.stln.launchersandarrows.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.stln.launchersandarrows.entity.AttributeSynchedEntityData;
import net.stln.launchersandarrows.mob_effect.MobEffectInit;
import net.stln.launchersandarrows.particle.ParticleInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements AttributeSynchedEntityData {
    // @Shadow public abstract Vec3 applyMovementInput(Vec3 movementInput, float slipperiness);

    @Shadow 
    protected abstract float getDamageAfterArmorAbsorb(DamageSource damageSource, float damageAmount);

    @Shadow 
    public abstract boolean hurt(DamageSource source, float amount);

    @Shadow @Nullable private DamageSource lastDamageSource;
    // @Shadow @Nullable private LivingEntity attacker;

    @Shadow 
    public abstract void hurtArmor(DamageSource source, float amount);

    @Shadow 
    public abstract boolean isFallFlying();

    // @Shadow public abstract void endCombat();

    @Unique
    private static final EntityDataAccessor<Boolean> BURNING_FLAG = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> FREEZE_FLAG = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> ELECTRIC_SHOCK_FLAG = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> CORROSION_FLAG = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> SUBMERGED_FLAG = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> CONFUSION_FLAG = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<Integer> BURNING_DURATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> FREEZE_DURATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ELECTRIC_SHOCK_DURATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> CORROSION_DURATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> SUBMERGED_DURATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> CONFUSION_DURATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);

    @Unique
    private static final EntityDataAccessor<Integer> FLAME_ACCUMULATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> FROST_ACCUMULATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> LIGHTNING_ACCUMULATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ACID_ACCUMULATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> FLOOD_ACCUMULATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ECHO_ACCUMULATION = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> SERIOUS_INJURY = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);

    @Unique
    LivingEntity entity = (LivingEntity) (Object) this;

    @Unique
    DamageSource damageSource = null;

    @Override
    public int getAccumulationTracker(int id) {
        switch (id) {
            case 0 -> {
                return entity.getEntityData().get(FLAME_ACCUMULATION);
            }
            case 1 -> {
                return entity.getEntityData().get(FROST_ACCUMULATION);
            }
            case 2 -> {
                return entity.getEntityData().get(LIGHTNING_ACCUMULATION);
            }
            case 3 -> {
                return entity.getEntityData().get(ACID_ACCUMULATION);
            }
            case 4 -> {
                return entity.getEntityData().get(FLOOD_ACCUMULATION);
            }
            case 5 -> {
                return entity.getEntityData().get(ECHO_ACCUMULATION);
            }
            case 6 -> {
                return entity.getEntityData().get(SERIOUS_INJURY);
            }
        }
        return 0;
    }

    @Override
    public boolean getEffectTracker(int id) {
        switch (id) {
            case 0 -> {
                return entity.getEntityData().get(BURNING_FLAG);
            }
            case 1 -> {
                return entity.getEntityData().get(FREEZE_FLAG);
            }
            case 2 -> {
                return entity.getEntityData().get(ELECTRIC_SHOCK_FLAG);
            }
            case 3 -> {
                return entity.getEntityData().get(CORROSION_FLAG);
            }
            case 4 -> {
                return entity.getEntityData().get(SUBMERGED_FLAG);
            }
            case 5 -> {
                return entity.getEntityData().get(CONFUSION_FLAG);
            }
        }
        return false;
    }

    @Override
    public int getEffectDuration(int id) {
        switch (id) {
            case 0 -> {
                return entity.getEntityData().get(BURNING_DURATION);
            }
            case 1 -> {
                return entity.getEntityData().get(FREEZE_DURATION);
            }
            case 2 -> {
                return entity.getEntityData().get(ELECTRIC_SHOCK_DURATION);
            }
            case 3 -> {
                return entity.getEntityData().get(CORROSION_DURATION);
            }
            case 4 -> {
                return entity.getEntityData().get(SUBMERGED_DURATION);
            }
            case 5 -> {
                return entity.getEntityData().get(CONFUSION_DURATION);
            }
        }
        return 0;
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void getDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        damageSource = source;
    }

    /*
    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0)
    private float modifyDamage(float damage) {
        if (entity.hasEffect(MobEffectInit.CORROSION) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            damage *= (float) (entity.getEffect(MobEffectInit.CORROSION).getAmplifier() + 3) / 2;
        }
        if (damageSource.getEntity() != null && damageSource.getEntity() instanceof LivingEntity attacker) {
            if (attacker.hasEffect(MobEffectInit.SUBMERGED)) {
                damage /= (attacker.getEffect(MobEffectInit.SUBMERGED).getAmplifier() + 2);
            }
        }
        if (entity.hasEffect(MobEffectInit.SERIOUS_INJURY)) {
            damage += (float) entity.getEffect(MobEffectInit.SERIOUS_INJURY).getAmplifier() / 4 + 1;
            entity.removeEffect(MobEffectInit.SERIOUS_INJURY);
        }
        return damage;
    }
    */

    /*
    @ModifyVariable(method = "heal", at = @At("HEAD"), ordinal = 0)
    private float modifyHeal(float amount) {
        if (entity.hasEffect(MobEffectInit.SERIOUS_INJURY)) {
            return amount * (1 - ((float) (entity.getEffect(MobEffectInit.SERIOUS_INJURY).getAmplifier() + 1) / (entity.getEffect(MobEffectInit.SERIOUS_INJURY).getAmplifier() + 3)));
        }
        return amount;
    }
    */

    /*
    @Inject(method = "decreaseAirSupply", at = @At("HEAD"), cancellable = true)
    private void checkHasSubmergedEffect(int air, CallbackInfoReturnable<Integer> cir) {
        if (entity.hasEffect(MobEffectInit.SUBMERGED)) {
            cir.setReturnValue(air);
        }
    }
    */

    /*
    @ModifyArg(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"),
            index = 3)
    private float getArmorLeft(float armor) {
        if (entity.hasEffect(MobEffectInit.CORROSION)) {
            return armor * (1 - ((float) (entity.getEffect(MobEffectInit.CORROSION).getAmplifier() + 1) / (entity.getEffect(MobEffectInit.CORROSION).getAmplifier() + 3)));
        }
        return armor;
    }
    */

    @Inject(method = "tickEffects", at = @At("TAIL"))
    private void tickEffects(CallbackInfo ci) {
        addStatusEffectParticle(MobEffectInit.BURNING.get(), BURNING_FLAG, ParticleInit.FLAME_EFFECT.get(), BURNING_DURATION);
        /*
        addStatusEffectParticle(MobEffectInit.FREEZE, FREEZE_FLAG, ParticleInit.FROST_EFFECT, FREEZE_DURATION);
        addStatusEffectParticle(MobEffectInit.ELECTRIC_SHOCK, ELECTRIC_SHOCK_FLAG, ParticleInit.LIGHTNING_EFFECT, ELECTRIC_SHOCK_DURATION);
        addStatusEffectParticle(MobEffectInit.CORROSION, CORROSION_FLAG, ParticleInit.ACID_EFFECT, CORROSION_DURATION);
        addStatusEffectParticle(MobEffectInit.SUBMERGED, SUBMERGED_FLAG, ParticleInit.FLOOD_EFFECT, SUBMERGED_DURATION);
        addStatusEffectParticle(MobEffectInit.CONFUSION, CONFUSION_FLAG, ParticleInit.ECHO_EFFECT, CONFUSION_DURATION);
        */
        if (!entity.level().isClientSide()) {
            if (entity.hasEffect(MobEffectInit.FLAME_ACCUMULATION)) {
                entity.getEntityData().set(FLAME_ACCUMULATION,
                        entity.getEffect(MobEffectInit.FLAME_ACCUMULATION).getAmplifier() + 1);
            } else {
                entity.getEntityData().set(FLAME_ACCUMULATION, 0);
            }
            /*
            if (entity.hasEffect(MobEffectInit.FROST_ACCUMULATION)) {
                entity.getEntityData().set(FROST_ACCUMULATION,
                        entity.getEffect(MobEffectInit.FROST_ACCUMULATION).getAmplifier() + 1);
            } else {
                entity.getEntityData().set(FROST_ACCUMULATION, 0);
            }
            if (entity.hasEffect(MobEffectInit.LIGHTNING_ACCUMULATION)) {
                entity.getEntityData().set(LIGHTNING_ACCUMULATION,
                        entity.getEffect(MobEffectInit.LIGHTNING_ACCUMULATION).getAmplifier() + 1);
            } else {
                entity.getEntityData().set(LIGHTNING_ACCUMULATION, 0);
            }
            if (entity.hasEffect(MobEffectInit.ACID_ACCUMULATION)) {
                entity.getEntityData().set(ACID_ACCUMULATION,
                        entity.getEffect(MobEffectInit.ACID_ACCUMULATION).getAmplifier() + 1);
            } else {
                entity.getEntityData().set(ACID_ACCUMULATION, 0);
            }
            if (entity.hasEffect(MobEffectInit.FLOOD_ACCUMULATION)) {
                entity.getEntityData().set(FLOOD_ACCUMULATION,
                        entity.getEffect(MobEffectInit.FLOOD_ACCUMULATION).getAmplifier() + 1);
            } else {
                entity.getEntityData().set(FLOOD_ACCUMULATION, 0);
            }
            if (entity.hasEffect(MobEffectInit.ECHO_ACCUMULATION)) {
                entity.getEntityData().set(ECHO_ACCUMULATION,
                        entity.getEffect(MobEffectInit.ECHO_ACCUMULATION).getAmplifier() + 1);
            } else {
                entity.getEntityData().set(ECHO_ACCUMULATION, 0);
            }
            if (entity.hasEffect(MobEffectInit.SERIOUS_INJURY)) {
                entity.getEntityData().set(SERIOUS_INJURY,
                        entity.getEffect(MobEffectInit.SERIOUS_INJURY).getAmplifier() + 1);
            } else {
                entity.getEntityData().set(SERIOUS_INJURY, 0);
            }
            */
        }
    }

    @Unique
    private void addStatusEffectParticle(MobEffect mobEffect, EntityDataAccessor<Boolean> data, ParticleOptions particleOptions, EntityDataAccessor<Integer> durationData) {
        if (entity.level().isClientSide() && entity.getEntityData().get(data)) {
            float w = entity.getBbWidth();
            float h = entity.getBbHeight();
            int entitySize = (int) (w * h * 10);
            for (int i = 0; i < entitySize; i++) {
                entity.level().addParticle(
                        particleOptions,
                        entity.getRandomX(0.5F),
                        entity.getRandomY(),
                        entity.getRandomZ(0.5F),
                        0.0, 0.0, 0.0
                );
            }
        } else if (entity.hasEffect(Holder.direct(mobEffect))) {
            entity.getEntityData().set(data, true);
            entity.getEntityData().set(durationData, entity.getEffect(Holder.direct(mobEffect)).getDuration());
        } else {
            entity.getEntityData().set(data, false);
            entity.getEntityData().set(durationData, 0);
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(BURNING_FLAG, false);
        builder.define(FREEZE_FLAG, false);
        builder.define(ELECTRIC_SHOCK_FLAG, false);
        builder.define(CORROSION_FLAG, false);
        builder.define(SUBMERGED_FLAG, false);
        builder.define(CONFUSION_FLAG, false);
        builder.define(BURNING_DURATION, 0);
        builder.define(FREEZE_DURATION, 0);
        builder.define(ELECTRIC_SHOCK_DURATION, 0);
        builder.define(CORROSION_DURATION, 0);
        builder.define(SUBMERGED_DURATION, 0);
        builder.define(CONFUSION_DURATION, 0);
        builder.define(FLAME_ACCUMULATION, 0);
        builder.define(FROST_ACCUMULATION, 0);
        builder.define(LIGHTNING_ACCUMULATION, 0);
        builder.define(ACID_ACCUMULATION, 0);
        builder.define(FLOOD_ACCUMULATION, 0);
        builder.define(ECHO_ACCUMULATION, 0);
        builder.define(SERIOUS_INJURY, 0);
    }
}

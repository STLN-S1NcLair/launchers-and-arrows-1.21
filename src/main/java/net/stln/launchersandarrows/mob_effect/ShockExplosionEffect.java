package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.particle.ParticleInit;

public class ShockExplosionEffect extends MobEffect {

    protected ShockExplosionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // f: applyUpdateEffect
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        MobEffectInstance instance = livingEntity.getEffect(MobEffectInit.SHOCK_EXPLOSION);
        if (instance == null) return super.applyEffectTick(livingEntity, amplifier);

        int duration = instance.getDuration();
        CompoundTag compound = livingEntity.getPersistentData();
        if (duration <= 1 && !compound.getBoolean("shock_exploded")) {
            compound.putBoolean("shock_exploded", true);
            generateExplosion(livingEntity, amplifier);
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    // f: canApplyUpdateEffect
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        CompoundTag compound = livingEntity.getPersistentData();
        compound.putBoolean("shock_exploded", false);
    }

    @Override
    public void onMobRemoved(LivingEntity livingEntity, int amplifier, Entity.RemovalReason reason) {
        super.onMobRemoved(livingEntity, amplifier, reason);
        generateExplosion(livingEntity, amplifier);
    }

    private void generateExplosion(LivingEntity entity, int amplifier){
        entity.level().explode(null, null, AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR,
                entity.getRandomX(0.5F), entity.getRandomY() - 0.1F, entity.getRandomZ(0.5F),
                amplifier * 2 + 2, false, Level.ExplosionInteraction.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE,
                SoundEvents.WIND_CHARGE_BURST
        );
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return ParticleInit.WAVE_EFFECT.get();
    }
}

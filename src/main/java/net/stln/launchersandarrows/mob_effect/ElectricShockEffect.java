package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.stln.launchersandarrows.mob_effect.util.MobEffectUtil;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;

public class ElectricShockEffect extends MobEffect {

    protected ElectricShockEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // f: applyUpdateEffect
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.level().playSound(livingEntity, livingEntity.blockPosition(), SoundInit.LIGHTNING_EFFECT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        livingEntity.hurt(livingEntity.damageSources().lightningBolt(), amplifier + 1);
        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 9));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 9));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 9));
        return super.applyEffectTick(livingEntity, amplifier);
    }

    // f: canApplyUpdateEffect
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 40 == 0 && duration > 0;
    }

    // f: onApplied ? onEffectAddedかも
    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        super.onEffectStarted(livingEntity, amplifier);
        MobEffectUtil.removeOtherAttributeEffect(livingEntity, 2);
        livingEntity.level().playSound(livingEntity, livingEntity.blockPosition(), SoundInit.LIGHTNING_EFFECT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return ParticleInit.LIGHTNING_EFFECT.get();
    }
}

package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.stln.launchersandarrows.mob_effect.util.MobEffectUtil;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;

public class BurningEffect extends MobEffect {
    protected BurningEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // f: applyUpdateEffect
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.hurt(livingEntity.damageSources().inFire(), amplifier + 1);
        return super.applyEffectTick(livingEntity, amplifier);
    }

    // f: canApplyUpdateEffect
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0 && duration > 0;
    }


    // f: onApplied ? onEffectAddedかも
    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        super.onEffectStarted(livingEntity, amplifier);
        MobEffectUtil.removeOtherAttributeEffect(livingEntity, 0);
        livingEntity.level().playSound(livingEntity, livingEntity.blockPosition(), SoundInit.FLAME_EFFECT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return ParticleInit.FLAME_EFFECT.get();
    }
}

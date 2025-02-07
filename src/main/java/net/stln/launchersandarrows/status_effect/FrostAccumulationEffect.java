package net.stln.launchersandarrows.status_effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.stln.launchersandarrows.particle.ParticleInit;

public class FrostAccumulationEffect extends AccumulationEffect {
    protected FrostAccumulationEffect() {
        super(StatusEffectCategory.HARMFUL, 0x89FEFF, ParticleInit.FROST_EFFECT);
    }

    @Override
    public void decreaseAmplifier() {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.FROST_ACCUMULATION, 20, amplifier - 1));
    }

    @Override
    public void applyEffect() {
        if (entity != null) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.FREEZE, 300, 0));
        entity.removeStatusEffect(StatusEffectInit.FROST_ACCUMULATION);
        }
    }
}

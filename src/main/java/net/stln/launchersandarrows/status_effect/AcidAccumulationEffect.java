package net.stln.launchersandarrows.status_effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.stln.launchersandarrows.particle.ParticleInit;

public class AcidAccumulationEffect extends AccumulationEffect {
    protected AcidAccumulationEffect() {
        super(StatusEffectCategory.HARMFUL, 0xA3FF4C, ParticleInit.ACID_EFFECT);
    }

    @Override
    public void decreaseAmplifier() {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.ACID_ACCUMULATION, 20, amplifier - 1));
    }

    @Override
    public void applyEffect() {
        if (entity != null) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.CORROSION, 300, 0));
            entity.removeStatusEffect(StatusEffectInit.ACID_ACCUMULATION);
        }
    }
}

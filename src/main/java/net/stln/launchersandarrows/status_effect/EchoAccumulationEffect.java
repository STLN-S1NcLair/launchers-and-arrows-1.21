package net.stln.launchersandarrows.status_effect;

import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.stln.launchersandarrows.particle.ParticleInit;

public class EchoAccumulationEffect extends AccumulationEffect {
    protected EchoAccumulationEffect() {
        super(StatusEffectCategory.HARMFUL, 0x00A6B0, ParticleInit.ECHO_EFFECT);
    }

    @Override
    public void decreaseAmplifier() {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.ECHO_ACCUMULATION, 20, amplifier - 1));
    }

    @Override
    public void applyEffect() {
        if (entity != null) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.CONFUSION, 300, 0));
        entity.removeStatusEffect(StatusEffectInit.ECHO_ACCUMULATION);
        }
    }
}

package net.stln.launchersandarrows.status_effect;

import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.stln.launchersandarrows.particle.ParticleInit;

public class LightningAccumulationEffect extends AccumulationEffect {
    protected LightningAccumulationEffect() {super(StatusEffectCategory.HARMFUL, 0x4C5CFF, ParticleInit.LIGHTNING_EFFECT);}

    @Override
    public void decreaseAmplifier() {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.LIGHTNING_ACCUMULATION, 20, amplifier - 1));
    }

    @Override
    public void applyEffect() {
        if (entity != null) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.ELECTRIC_SHOCK, 300, 0));
        entity.removeStatusEffect(StatusEffectInit.LIGHTNING_ACCUMULATION);
        }
    }
}

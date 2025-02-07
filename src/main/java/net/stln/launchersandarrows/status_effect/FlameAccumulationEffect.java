package net.stln.launchersandarrows.status_effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.particle.ParticleInit;

public class FlameAccumulationEffect extends AccumulationEffect {
    protected FlameAccumulationEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFAB32, ParticleInit.FLAME_EFFECT);
    }

    @Override
    public void decreaseAmplifier() {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.FLAME_ACCUMULATION, 20, amplifier - 1));
    }

    @Override
    public void applyEffect() {
        if (entity != null) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.BURNING, 300, 0));
            entity.removeStatusEffect(StatusEffectInit.FLAME_ACCUMULATION);
        }
    }
}

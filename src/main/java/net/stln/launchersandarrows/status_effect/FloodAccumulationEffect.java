package net.stln.launchersandarrows.status_effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.stln.launchersandarrows.particle.ParticleInit;

public class FloodAccumulationEffect extends AccumulationEffect {
    protected FloodAccumulationEffect() {
        super(StatusEffectCategory.HARMFUL, 0x74C6FF, ParticleInit.FLOOD_EFFECT);
    }

    @Override
    public void decreaseAmplifier() {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.FLOOD_ACCUMULATION, 20, amplifier - 1));
    }

    @Override
    public void applyEffect() {
        if (entity != null) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectInit.SUBMERGED, 300, 0));
        entity.removeStatusEffect(StatusEffectInit.FLOOD_ACCUMULATION);
        }
    }
}

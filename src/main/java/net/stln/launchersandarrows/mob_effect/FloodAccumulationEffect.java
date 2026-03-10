package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.stln.launchersandarrows.particle.ParticleInit;

public class FloodAccumulationEffect extends AccumulationEffect{

    protected FloodAccumulationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    protected Holder<MobEffect> getHolder() {
        return MobEffectInit.FLOOD_ACCUMULATION;
    }

    @Override
    protected Holder<MobEffect> getChangeHolder() {
        return MobEffectInit.SUBMERGED;
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return ParticleInit.FLOOD_EFFECT.get();
    }
}

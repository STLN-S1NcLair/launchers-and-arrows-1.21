package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.stln.launchersandarrows.particle.ParticleInit;

public class FlameAccumulationEffect extends AccumulationEffect {

    public FlameAccumulationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    protected void triggerEffect(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffectInit.BURNING, 300, 0));
    }

    @Override
    protected Holder<MobEffect> getHolder() {
        return MobEffectInit.FLAME_ACCUMULATION;
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return ParticleInit.FLAME_EFFECT.get();
    }
}

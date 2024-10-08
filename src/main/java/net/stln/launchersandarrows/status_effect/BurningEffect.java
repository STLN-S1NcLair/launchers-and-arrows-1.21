package net.stln.launchersandarrows.status_effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.stln.launchersandarrows.mixin.LivingEntityMixin;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.status_effect.util.StatusEffectUtil;

public class BurningEffect extends StatusEffect {
    protected BurningEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFAB32, ParticleInit.FLAME_EFFECT);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(entity.getDamageSources().inFire(), amplifier + 1);
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 20 == 0 && duration > 0;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        StatusEffectUtil.removeOtherAttributeEffect(entity, 0);
        entity.getWorld().playSound(entity, entity.getBlockPos(), SoundInit.FLAME_EFFECT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}

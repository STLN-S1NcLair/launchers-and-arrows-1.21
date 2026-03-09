package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public abstract class AccumulationEffect extends MobEffect {

    protected AccumulationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        MobEffectInstance instance = entity.getEffect(getHolder());
        if (instance == null) {
            return false;
        }

        int duration = instance.getDuration();

        // 閾値チェック
        if (amplifier + 1 > Math.sqrt(entity.getMaxHealth()) * 5) {
            triggerEffect(entity);
            entity.removeEffect(getHolder());
            return true;
        }

        // duration終了直前にスタック減衰
        if (duration <= 1 && amplifier > 0) {
            entity.addEffect(new MobEffectInstance(getHolder(), 20, amplifier - 1));
        }

        return true;
    }

    protected abstract void triggerEffect(LivingEntity entity);

    protected abstract Holder<MobEffect> getHolder();
}

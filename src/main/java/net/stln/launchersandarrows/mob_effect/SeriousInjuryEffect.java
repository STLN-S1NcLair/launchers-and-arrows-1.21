package net.stln.launchersandarrows.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class SeriousInjuryEffect extends MobEffect {

    protected SeriousInjuryEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // f: applyUpdateEffect
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        // ダッシュしていたらダッシュを切る
        if(entity.isSprinting()){
            entity.setSprinting(false);
        }

        MobEffectInstance instance = entity.getEffect(MobEffectInit.SERIOUS_INJURY);
        if (instance == null) return super.applyEffectTick(entity, amplifier);

        int duration = instance.getDuration();

        // duration終了直前にスタック減衰
        if (duration <= 1 && amplifier > 0) {
            entity.addEffect(new MobEffectInstance(MobEffectInit.SERIOUS_INJURY, 100, amplifier - 1));
        }

        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}

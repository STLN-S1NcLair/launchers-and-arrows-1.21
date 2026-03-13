package net.stln.launchersandarrows.mob_effect.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.stln.launchersandarrows.item.util.AttributeEffectsDictionary;
import net.stln.launchersandarrows.mob_effect.MobEffectInit;
import net.stln.launchersandarrows.util.AttributeEnum;

public class MobEffectUtil {
    public static void stackStatusEffect(LivingEntity target, MobEffectInstance effectInstance) {
        MobEffectInstance targetEffect = target.getEffect(effectInstance.getEffect());
        int amplifier = -1;
        if (targetEffect != null) {
            amplifier = targetEffect.getAmplifier();
        }
        if (effectInstance.getAmplifier() >= 0) {
            target.addEffect(new MobEffectInstance(effectInstance.getEffect(),
                    effectInstance.getDuration(), amplifier + effectInstance.getAmplifier() + 1));
        }
    }

    public static void removeOtherAttributeEffect(LivingEntity entity, int id) {
        if (id != AttributeEnum.FLAME.get()) {
            entity.removeEffect(MobEffectInit.BURNING);
        }
        if (id != AttributeEnum.FROST.get()) {
            entity.removeEffect(MobEffectInit.FREEZE);
        }
        if (id != AttributeEnum.LIGHTNING.get()) {
            entity.removeEffect(MobEffectInit.ELECTRIC_SHOCK);
        }
        if (id != AttributeEnum.ACID.get()) {
            entity.removeEffect(MobEffectInit.CORROSION);
        }
        if (id != AttributeEnum.FLOOD.get()) {
            entity.removeEffect(MobEffectInit.SUBMERGED);
        }
        if (id != AttributeEnum.ECHO.get()) {
            entity.removeEffect(MobEffectInit.CONFUSION);
        }
    }

    public static void applyAttributeEffect(LivingEntity entity, ItemStack stack) {
        Integer[] attributes = new Integer[7];
        Item item = stack.getItem();
        for (int i = 0; i < 7; i++) {
            attributes[i] = AttributeEffectsDictionary.getAttributeEffect(item, i);
        }
        if (attributes[AttributeEnum.FLAME.get()] != null) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FLAME_ACCUMULATION, 20, attributes[AttributeEnum.FLAME.get()] - 1));
        }
        if (attributes[AttributeEnum.FROST.get()] != null) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FROST_ACCUMULATION, 20, attributes[AttributeEnum.FROST.get()] - 1));
        }
        if (attributes[AttributeEnum.LIGHTNING.get()] != null) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.LIGHTNING_ACCUMULATION, 20, attributes[AttributeEnum.LIGHTNING.get()] - 1));
        }
        if (attributes[AttributeEnum.ACID.get()] != null) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.ACID_ACCUMULATION, 20, attributes[AttributeEnum.ACID.get()] - 1));
        }
        if (attributes[AttributeEnum.FLOOD.get()] != null) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FLOOD_ACCUMULATION, 20, attributes[AttributeEnum.FLOOD.get()] - 1));
        }
        if (attributes[AttributeEnum.ECHO.get()] != null) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.ECHO_ACCUMULATION, 20, attributes[AttributeEnum.ECHO.get()] - 1));
        }
        if (attributes[AttributeEnum.INJURY.get()] != null) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.SERIOUS_INJURY, 100, attributes[AttributeEnum.INJURY.get()] - 1));
        }
    }

    public static void applyAttributeModifier(LivingEntity entity, Integer[] data) {
        if (data[AttributeEnum.FLAME.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FLAME_ACCUMULATION, 20, data[AttributeEnum.FLAME.get()] - 1));
        }
        if (data[AttributeEnum.FROST.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FROST_ACCUMULATION, 20, data[AttributeEnum.FROST.get()] - 1));
        }
        if (data[AttributeEnum.LIGHTNING.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.LIGHTNING_ACCUMULATION, 20, data[AttributeEnum.LIGHTNING.get()] - 1));
        }
        if (data[AttributeEnum.ACID.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.ACID_ACCUMULATION, 20, data[AttributeEnum.ACID.get()] - 1));
        }
        if (data[AttributeEnum.FLOOD.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FLOOD_ACCUMULATION, 20, data[AttributeEnum.FLOOD.get()] - 1));
        }
        if (data[AttributeEnum.ECHO.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.ECHO_ACCUMULATION, 20, data[AttributeEnum.ECHO.get()] - 1));
        }
        if (data[AttributeEnum.INJURY.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.SERIOUS_INJURY, 100, data[AttributeEnum.INJURY.get()] - 1));
        }
    }

    public static void applyAttributeRatioModifier(LivingEntity entity, ItemStack stack, Integer[] data) {
        Integer[] attributes = new Integer[7];
        Item item = stack.getItem();
        for (int i = 0; i < 7; i++) {
            attributes[i] = AttributeEffectsDictionary.getAttributeEffect(item, i);
        }
        if (attributes[AttributeEnum.FLAME.get()] != null && data[AttributeEnum.FLAME.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FLAME_ACCUMULATION, 20, Math.round(attributes[AttributeEnum.FLAME.get()] * (data[AttributeEnum.FLAME.get()] / 100.0F) - 1)));
        }
        if (attributes[AttributeEnum.FROST.get()] != null && data[AttributeEnum.FROST.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FROST_ACCUMULATION, 20, Math.round(attributes[AttributeEnum.FROST.get()] * (data[AttributeEnum.FROST.get()] / 100.0F) - 1)));
        }
        if (attributes[AttributeEnum.LIGHTNING.get()] != null && data[AttributeEnum.LIGHTNING.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.LIGHTNING_ACCUMULATION, 20, Math.round(attributes[AttributeEnum.LIGHTNING.get()] * (data[AttributeEnum.LIGHTNING.get()] / 100.0F) - 1)));
        }
        if (attributes[AttributeEnum.ACID.get()] != null && data[AttributeEnum.ACID.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.ACID_ACCUMULATION, 20, Math.round(attributes[AttributeEnum.ACID.get()] * (data[AttributeEnum.ACID.get()] / 100.0F) - 1)));
        }
        if (attributes[AttributeEnum.FLOOD.get()] != null && data[AttributeEnum.FLOOD.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.FLOOD_ACCUMULATION, 20, Math.round(attributes[AttributeEnum.FLOOD.get()] * (data[AttributeEnum.FLOOD.get()] / 100.0F) - 1)));
        }
        if (attributes[AttributeEnum.ECHO.get()] != null && data[AttributeEnum.ECHO.get()] > 0) {
            MobEffectUtil.stackStatusEffect(entity, new MobEffectInstance(MobEffectInit.ECHO_ACCUMULATION, 20, Math.round(attributes[AttributeEnum.ECHO.get()] * (data[AttributeEnum.ECHO.get()] / 100.0F) - 1)));
        }
    }
}

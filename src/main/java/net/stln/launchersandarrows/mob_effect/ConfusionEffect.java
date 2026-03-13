package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.stln.launchersandarrows.mob_effect.util.MobEffectUtil;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;

import java.util.ArrayList;
import java.util.List;

public class ConfusionEffect extends MobEffect {

    protected ConfusionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // f: applyUpdateEffect
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof Player player){
            MobEffectInstance instance = livingEntity.getEffect(MobEffectInit.CONFUSION);
            if(instance == null) super.applyEffectTick(livingEntity, amplifier);

            int duration = instance.getDuration();

            CompoundTag compound = player.getPersistentData();

            // fabric版はフィールドに読み書きしていたが、Effectがシングルトンであることから不適切であり、CompoundTagに読み書きすることで解決
            if(duration % 20 == 0){
                compound.putFloat("confusionYaw", player.getRandom().nextFloat() - 0.5F);
                compound.putFloat("confusionPitch", player.getRandom().nextFloat() - 0.5F);
            }

            float yaw = compound.getFloat("confusionYaw");
            float pitch = compound.getFloat("confusionPitch");

            player.setYRot(player.getYRot() + (yaw * (amplifier + 1) * 10));
            player.setXRot(player.getXRot() + (pitch * (amplifier + 1) * 10));
        }
        else if(livingEntity instanceof Mob mob){
            List<Entity> list = mob.level().getEntities(mob,
                    new AABB(mob.getX() - 7, mob.getY() - 7, mob.getZ() - 7,
                            mob.getX() + 7, mob.getY() + 7, mob.getZ() + 7),
                    EntitySelector.NO_CREATIVE_OR_SPECTATOR
            );
            List<LivingEntity> targets = new ArrayList<>(List.of());
            for(int i = 0; i < list.size(); i++){
                if(list.get(i) instanceof LivingEntity living){
                    targets.add(living);
                }
            }
            if(!targets.isEmpty()) {
                mob.setTarget(targets.get(mob.getRandom().nextInt(targets.size())));
            }
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    // f: canApplyUpdateEffect
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration > 0;
    }

    // f: onApplied ? onEffectAddedかも
    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        super.onEffectStarted(livingEntity, amplifier);
        MobEffectUtil.removeOtherAttributeEffect(livingEntity, 5);
        livingEntity.level().playSound(livingEntity, livingEntity.blockPosition(), SoundInit.ECHO_EFFECT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return ParticleInit.ECHO_EFFECT.get();
    }
}

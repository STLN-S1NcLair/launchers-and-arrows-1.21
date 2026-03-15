package net.stln.launchersandarrows.mixin.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.entity.RicochetProjectile;
import net.stln.launchersandarrows.entity.renderer.RicochetEffectProjectile;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.ItemTagKeys;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.mob_effect.MobEffectInit;
import net.stln.launchersandarrows.mob_effect.util.MobEffectUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Arrow.class)
public class ArrowEffectMixin implements RicochetEffectProjectile {
    @Unique
    private int inGroundTime = 0;
    @Unique
    private int glitchCount = 0;
    @Unique
    private int trackingTime = 0;
    @Unique
    private boolean setRicochet = false;

    @Unique
    private static ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
            false, true, Optional.of(1.0F), Optional.empty());

    @Unique
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(Arrow.class, EntityDataSerializers.ITEM_STACK);

    @Unique
    Arrow arrow = (Arrow) (Object) this;
    @Unique
    ItemStack itemStack = ItemStack.EMPTY;
    @Unique
    LivingEntity target = null;

    @Override
    public void onRicochet(int count) {
        itemStack = arrow.getEntityData().get(ITEM_STACK);
        if (itemStack.is(ItemInit.WAVE_ARROW)) {
            Vec3 newPos = arrow.position().add(arrow.getDeltaMovement());
            arrow.setPos(newPos.x, newPos.y, newPos.z);
            generateWindExplosion(false);
        }
    }

    // f: getparticleEffect
    @Unique
    private ParticleOptions getParticleOptions() {
        if (itemStack.is(ItemInit.FLAME_ARROW)) return ParticleInit.FLAME_EFFECT.get();
        else if (itemStack.is(ItemInit.FREEZING_ARROW)) return ParticleInit.FROST_EFFECT.get();
        else if (itemStack.is(ItemInit.LIGHTNING_ARROW)) return ParticleInit.LIGHTNING_EFFECT.get();
        else if (itemStack.is(ItemInit.CORROSIVE_ARROW)) return ParticleInit.ACID_EFFECT.get();
        else if (itemStack.is(ItemInit.FLOOD_ARROW)) return ParticleInit.FLOOD_EFFECT.get();
        else if (itemStack.is(ItemInit.REVERBERATING_ARROW)) return ParticleInit.ECHO_EFFECT.get();
        else if (itemStack.is(ItemInit.WAVE_ARROW)) return ParticleInit.WAVE_EFFECT.get();
        else if (itemStack.is(ItemInit.HOMING_ARROW)) return ParticleInit.HOMING_EFFECT.get();
        else if (itemStack.is(ItemInit.GLITCH_ARROW)) return ParticleInit.GLITCH_EFFECT.get();
        else if (itemStack.is(ItemInit.BURST_ARROW)) return ParticleTypes.SMOKE;
        return null;
    }

    @Unique
    private void generateWindExplosion(boolean kill) {
        Vec3 pos = arrow.position();
        arrow.level().explode(null, null,
                EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(),
                2.0F, false, Level.ExplosionInteraction.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.WIND_CHARGE_BURST);
        if (kill) {
            arrow.kill();
        }
    }

    @Unique
    private void trackEntity(Arrow arrow) {
        Vec3 pos = arrow.position();
        LivingEntity nerarestEntity = arrow.level().getNearestEntity(LivingEntity.class,
                TargetingConditions.DEFAULT, (LivingEntity) arrow.getOwner(),
                pos.x(), pos.y(), pos.z(),
                AABB.ofSize(pos.add(arrow.getDeltaMovement()), 16, 16, 16)
        );
        if (target == null || target.isDeadOrDying()) {
            target = nerarestEntity;
            trackingTime = 0;
        }
        if (target != null) {
            Vec3 tarPos = target.getEyePosition();
            Vec3 distance = tarPos.subtract(pos);
            if (distance.length() > 8 && nerarestEntity != null) {
                target = nerarestEntity;
            }
            distance = distance.scale(1 / distance.length() / 7 * arrow.getDeltaMovement().length());
            arrow.addDeltaMovement(distance);
            trackingTime++;
        }
        if (trackingTime > 100) {
            arrow.discard();
        }
    }

    @Unique
    private void invertBlock(Arrow arrow, BlockPos pos) {
        var level = arrow.level();
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {

                    if (x > 0 || (x == 0 && y > 0) || (x == 0 && y == 0 && z >= 0)) continue;

                    BlockPos p1 = pos.offset(x, y, z);
                    BlockPos p2 = pos.offset(-x, -y, -z);

                    BlockState s1 = level.getBlockState(p1);
                    BlockState s2 = level.getBlockState(p2);

                    boolean b1 = s1.getDestroySpeed(level, p1) > 0;
                    boolean b2 = s2.getDestroySpeed(level, p2) > 0;

                    boolean a1 = s1.isAir();
                    boolean a2 = s2.isAir();

                    if ((b1 && b2) || (b1 && a2) || (b2 && a1)) {
                        level.setBlock(p1, s2, 3);
                        level.setBlock(p2, s1, 3);
                    }
                }
            }
        }
    }

    @Unique
    private void addGlitchEffect(BlockPos pos) {
        for (int i = 0; i < 125; i++) {
            arrow.level().addParticle(ParticleInit.GLITCH_EFFECT.get(), true,
                    pos.getX() + (arrow.getRandom().nextFloat() * 5 - 2),
                    pos.getY() + (arrow.getRandom().nextFloat() * 5 - 2),
                    pos.getZ() + (arrow.getRandom().nextFloat() * 5 - 2),
                    0, 0, 0
            );
        }
        if (arrow.getOwner() instanceof Player) {
            arrow.level().playSound((Player) arrow.getOwner(), pos, SoundInit.GLITCH.get(), SoundSource.PLAYERS);
        }
        else {
            arrow.level().playSound(null, pos, SoundInit.GLITCH.get(), SoundSource.PLAYERS);
        }
    }

    @Unique
    private void generateExplosion() {
        Vec3 pos = arrow.position();
        Level level = arrow.level();

        level.explode(arrow, Explosion.getDefaultDamageSource(level, arrow),
                EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(),
                2F, false, Level.ExplosionInteraction.MOB,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, Holder.direct(SoundInit.EXPLODE.get()));
        arrow.kill();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo info) {
        if (glitchCount >= 1) {
            arrow.discard();
        }
        if (!arrow.level().isClientSide()) {
            arrow.getEntityData().set(ITEM_STACK, arrow.getPickupItemStackOrigin());
        }
        itemStack = arrow.getEntityData().get(ITEM_STACK);
        if (itemStack.is(ItemInit.WAVE_ARROW)) {
            if (!setRicochet) {
                ((RicochetProjectile)arrow).setRicochet(1 + ((RicochetProjectile)arrow).getRicochet());
                setRicochet = true;
            }
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            CompoundTag compound = new CompoundTag();
            arrow.addAdditionalSaveData(compound);
            if (compound.getBoolean("inGround")) {
                if (inGroundTime == 0) {
                    arrow.level().playSound(null, arrow.blockPosition(), SoundInit.WAVE.get(), SoundSource.PLAYERS);
                }
                inGroundTime++;
            } else {
                inGroundTime = 0;
            }
            if (inGroundTime > 50) {
                generateWindExplosion(true);
                arrow.discard();
            }
        } else if (itemStack.is(ItemInit.HOMING_ARROW)) {
            trackEntity(arrow);
        } else if (itemStack.is(ItemInit.GLITCH_ARROW)) {
            CompoundTag compound = new CompoundTag();
            arrow.addAdditionalSaveData(compound);
            if (compound.getBoolean("inGround")) {
                glitchCount++;
                invertBlock(arrow, arrow.blockPosition());
                BlockPos pos = arrow.blockPosition();
                addGlitchEffect(pos);
            }
        } else if (itemStack.is(ItemInit.TAILWIND_ARROW)) {
            Vec3 velocity = arrow.getDeltaMovement();
            if (arrow.tickCount > 10) {
                arrow.addDeltaMovement(velocity.scale(1 / velocity.length() / 5));
            }
        } else if (itemStack.is(ItemInit.LINEAR_ARROW)) {
            arrow.setNoGravity(arrow.tickCount < 300);
        } else if (itemStack.is(ItemInit.BURST_ARROW)) {
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            CompoundTag nbt = new CompoundTag();
            arrow.addAdditionalSaveData(nbt);
            if (arrow.tickCount > 5) {
                generateExplosion();
            }
        }
    }

    // f: spawnParticles
    @Inject(method = "makeParticle", at = @At("HEAD"))
    private void makeParticle(int amount, CallbackInfo ci) {
        if (itemStack.is(ItemTagKeys.ARROWS_WITH_EFFECT) && !itemStack.is(ItemInit.PIERCING_ARROW)) {
            if (amount > 0) {
                CompoundTag nbt = new CompoundTag();
                arrow.addAdditionalSaveData(nbt);
                for (int j = 0; j < amount; j++) {
                    if (!nbt.getBoolean("inGround") || arrow.getRandom().nextFloat() >= 0.65F) {
                        arrow.level().addParticle(
                                        getParticleOptions(), arrow.getRandomX(0.25),
                                        arrow.getY() - 0.125 + (arrow.getRandom().nextFloat() / 4),
                                        arrow.getRandomZ(0.25),
                                        0.0, 0.0, 0.0
                        );
                    }
                }
            }
        }
    }

    @Inject(method = "doPostHurtEffects", at = @At("HEAD"))
    private void onHit(LivingEntity target, CallbackInfo ci) {
        if (target instanceof LivingEntity livingEntity) {
            MobEffectUtil.applyAttributeEffect(livingEntity, this.itemStack);
            if (itemStack.is(ItemInit.WAVE_ARROW)) {
                MobEffectUtil.stackStatusEffect(livingEntity, new MobEffectInstance(MobEffectInit.SHOCK_EXPLOSION, 50, 0));
                arrow.level().playSound(null, arrow.blockPosition(), SoundInit.WAVE.get(), SoundSource.PLAYERS);
            } else if (itemStack.is(ItemInit.GLITCH_ARROW)) {
                invertBlock(arrow, target.blockPosition());
                BlockPos pos = arrow.blockPosition();
                addGlitchEffect(pos);
            }
            MobEffectUtil.applyAttributeModifier(livingEntity, ((AttributedProjectile) arrow).getAttributes());
            MobEffectUtil.applyAttributeRatioModifier(livingEntity, this.itemStack, ((AttributedProjectile) arrow).getRatioAttributes());
        }
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo info){
        builder.define(ITEM_STACK, new ItemStack(Items.ARROW));
    }
}

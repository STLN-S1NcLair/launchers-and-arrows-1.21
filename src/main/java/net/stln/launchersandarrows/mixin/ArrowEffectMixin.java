package net.stln.launchersandarrows.mixin;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.entity.RicochetProjectile;
import net.stln.launchersandarrows.entity.renderer.RicochetEffectProjectile;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.ModItemTags;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.status_effect.StatusEffectInit;
import net.stln.launchersandarrows.status_effect.util.StatusEffectUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static net.minecraft.entity.projectile.AbstractWindChargeEntity.EXPLOSION_BEHAVIOR;

@Mixin(ArrowEntity.class)
public abstract class ArrowEffectMixin implements RicochetEffectProjectile {

    @Unique
    private int inGroundTime = 0;
    @Unique
    private int glitchCount = 0;
    @Unique
    private int trackingTime = 0;
    @Unique
    private boolean setRicochet = false;

    @Unique
    private static ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(
            false, true, Optional.of(1.0F), Optional.empty());

    @Unique
    private static final TrackedData<ItemStack> ITEM_STACK =
            DataTracker.registerData(ArrowEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Unique
    ArrowEntity arrowEntity = (ArrowEntity) (Object) this;
    @Unique
    ItemStack itemStack = ItemStack.EMPTY;
    @Unique
    LivingEntity target = null;

    @Override
    public void onRicochet(int count) {
        itemStack = arrowEntity.getDataTracker().get(ITEM_STACK);
        if (itemStack.isOf(ItemInit.WAVE_ARROW)) {
            Vec3d newPos = arrowEntity.getPos().add(arrowEntity.getVelocity());
            arrowEntity.setPos(newPos.x, newPos.y, newPos.z);
            generateWindExplosion(false);
        }
}

    @Unique
    private ParticleEffect getparticleEffect() {
        if (itemStack.isOf(ItemInit.FLAME_ARROW)) return ParticleInit.FLAME_EFFECT;
         else if (itemStack.isOf(ItemInit.FREEZING_ARROW)) return ParticleInit.FROST_EFFECT;
         else if (itemStack.isOf(ItemInit.LIGHTNING_ARROW)) return ParticleInit.LIGHTNING_EFFECT;
         else if (itemStack.isOf(ItemInit.CORROSIVE_ARROW)) return ParticleInit.ACID_EFFECT;
         else if (itemStack.isOf(ItemInit.FLOOD_ARROW)) return ParticleInit.FLOOD_EFFECT;
         else if (itemStack.isOf(ItemInit.REVERBERATING_ARROW)) return ParticleInit.ECHO_EFFECT;
         else if (itemStack.isOf(ItemInit.WAVE_ARROW)) return ParticleInit.WAVE_EFFECT;
         else if (itemStack.isOf(ItemInit.HOMING_ARROW)) return ParticleInit.HOMING_EFFECT;
         else if (itemStack.isOf(ItemInit.GLITCH_ARROW)) return ParticleInit.GLITCH_EFFECT;
        else if (itemStack.isOf(ItemInit.BURST_ARROW)) return ParticleTypes.SMOKE;
        return null;
    }

    @Unique
    private void generateWindExplosion(boolean kill) {
        Vec3d pos = arrowEntity.getPos();
        arrowEntity.getWorld().createExplosion(null, null,
                EXPLOSION_BEHAVIOR, pos.getX(), pos.getY(), pos.getZ(),
                2.0F, false, World.ExplosionSourceType.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);
        if (kill) {
            arrowEntity.kill();
        }
    }

    @Unique
    private void trackEntity(ArrowEntity arrowEntity) {
        Vec3d pos = arrowEntity.getPos();
        LivingEntity closestEntity = arrowEntity.getWorld().getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT, (LivingEntity) arrowEntity.getOwner(), pos.x, pos.y, pos.z, Box.of(pos.add(arrowEntity.getVelocity()), 16, 16, 16));
        if (target == null || target.isDead()) {
        target = closestEntity;
        trackingTime = 0;
        }
        if (target != null) {
            Vec3d tarPos = target.getEyePos();
            Vec3d distance = tarPos.subtract(pos);
            if (distance.length() > 8 && closestEntity != null) {
                target = closestEntity;
            }
            distance = distance.multiply(1 / distance.length() / 7 * arrowEntity.getVelocity().length());
            arrowEntity.addVelocity(distance);
            trackingTime++;
        }
        if (trackingTime > 100) {
            arrowEntity.discard();
        }
    }

    @Unique
    private void invertBlock(ArrowEntity arrowEntity, BlockPos pos) {

        BlockState[][][] array = new BlockState[5][5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    BlockPos currentBlockPos = pos.add(i - 2, j - 2, k - 2);
                    array[i][j][k] = arrowEntity.getWorld().getBlockState(currentBlockPos);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    BlockPos currentBlockPos = pos.add(2 - i, 2 - j, 2 - k);
                    boolean flag1 = array[i][j][k].getBlock().getHardness() > 0;
                    boolean flag2 = arrowEntity.getWorld().getBlockState(currentBlockPos).getBlock().getHardness() > 0;
                    boolean flag3 = array[i][j][k].getBlock() == Blocks.AIR;
                    boolean flag4 = arrowEntity.getWorld().getBlockState(currentBlockPos).getBlock() == Blocks.AIR;
                    if ((flag1 && flag2) || (flag1 && flag4) || (flag2 && flag3)) {
                        arrowEntity.getWorld().setBlockState(currentBlockPos, array[i][j][k]);
                    }
                }
            }
        }
    }

    @Unique
    private void addGlitchEffect(BlockPos pos) {
        for (int i = 0; i < 125; i++) {
            arrowEntity.getWorld().addParticle(ParticleInit.GLITCH_EFFECT, true,
                    pos.getX() + (arrowEntity.getRandom().nextFloat() * 5 - 2),
                    pos.getY() + (arrowEntity.getRandom().nextFloat() * 5 - 2),
                    pos.getZ() + (arrowEntity.getRandom().nextFloat() * 5 - 2),
                    0, 0, 0);
        }
        if (arrowEntity.getOwner() instanceof PlayerEntity) {
            arrowEntity.getWorld().playSound((PlayerEntity) arrowEntity.getOwner(), pos, SoundInit.GLITCH, SoundCategory.PLAYERS);
        } else {
            arrowEntity.getWorld().playSound(null, pos, SoundInit.GLITCH, SoundCategory.PLAYERS);
        }
    }

    @Unique
    private void generateExplosion() {
        Vec3d pos = arrowEntity.getPos();

        arrowEntity.getWorld().createExplosion(arrowEntity, Explosion.createDamageSource(arrowEntity.getWorld(), arrowEntity),
                EXPLOSION_BEHAVIOR, pos.getX(), pos.getY(), pos.getZ(),
                2F, false, World.ExplosionSourceType.MOB,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundInit.EXPLODE_ENTRY);
        arrowEntity.kill();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (glitchCount >= 1) {
            arrowEntity.discard();
        }
        if (!arrowEntity.getWorld().isClient()) {
            arrowEntity.getDataTracker().set(ITEM_STACK, arrowEntity.getItemStack());
        }
        itemStack = arrowEntity.getDataTracker().get(ITEM_STACK);
        if (itemStack.isOf(ItemInit.WAVE_ARROW)) {
            if (!setRicochet) {
                ((RicochetProjectile)arrowEntity).setRicochet(1 + ((RicochetProjectile)arrowEntity).getRicochet());
                setRicochet = true;
            }
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            NbtCompound nbt = new NbtCompound();
            arrowEntity.writeCustomDataToNbt(nbt);
            if (nbt.getBoolean("inGround")) {
                if (inGroundTime == 0) {
                    arrowEntity.getWorld().playSound(null, arrowEntity.getBlockPos(), SoundInit.WAVE, SoundCategory.PLAYERS);
                }
                inGroundTime++;
            } else {
                inGroundTime = 0;
            }
            if (inGroundTime > 50) {
                generateWindExplosion(true);
                arrowEntity.discard();
            }
        } else if (itemStack.isOf(ItemInit.HOMING_ARROW)) {
            trackEntity(arrowEntity);
        } else if (itemStack.isOf(ItemInit.GLITCH_ARROW)) {
            NbtCompound nbt = new NbtCompound();
            arrowEntity.writeCustomDataToNbt(nbt);
            if (nbt.getBoolean("inGround")) {
                glitchCount++;
                invertBlock(arrowEntity, arrowEntity.getBlockPos());
                BlockPos pos = arrowEntity.getBlockPos();
                addGlitchEffect(pos);
            }
        } else if (itemStack.isOf(ItemInit.TAILWIND_ARROW)) {
            Vec3d velocity = arrowEntity.getVelocity();
            if (arrowEntity.age > 10) {
                arrowEntity.addVelocity(velocity.multiply(1 / velocity.length() / 5));
            }
        } else if (itemStack.isOf(ItemInit.LINEAR_ARROW)) {
            arrowEntity.setNoGravity(true);
        } else if (itemStack.isOf(ItemInit.BURST_ARROW)) {
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            NbtCompound nbt = new NbtCompound();
            arrowEntity.writeCustomDataToNbt(nbt);
            if (arrowEntity.age > 5) {
                generateExplosion();
            }
        }
    }


    @Inject(method = "spawnParticles", at = @At("HEAD"))
    private void spawnParticles(int amount, CallbackInfo ci) {
        if (itemStack.isIn(ModItemTags.ARROWS_WITH_EFFECT) && !itemStack.isOf(ItemInit.PIERCING_ARROW)) {
            if (amount > 0) {
                NbtCompound nbt = new NbtCompound();
                arrowEntity.writeCustomDataToNbt(nbt);
                for (int j = 0; j < amount; j++) {
                    if (!nbt.getBoolean("inGround") || arrowEntity.getRandom().nextFloat() >= 0.65F) {
                        arrowEntity.getWorld()
                                .addParticle(
                                        getparticleEffect(), arrowEntity.getParticleX(0.25),
                                        arrowEntity.getY() - 0.125 + (arrowEntity.getRandom().nextFloat() / 4),
                                        arrowEntity.getParticleZ(0.25),
                                        0.0, 0.0, 0.0
                                );
                    }
                }
            }
        }
    }

    @Inject(method = "onHit", at = @At("HEAD"))
    private void onHit(LivingEntity target, CallbackInfo ci) {
        if (target instanceof LivingEntity livingEntity) {
            StatusEffectUtil.applyAttributeEffect(livingEntity, this.itemStack);
            if (itemStack.isOf(ItemInit.WAVE_ARROW)) {
                StatusEffectUtil.stackStatusEffect(livingEntity, new StatusEffectInstance(StatusEffectInit.SHOCK_EXPLOSION, 50, 0));
                arrowEntity.getWorld().playSound(null, arrowEntity.getBlockPos(), SoundInit.WAVE, SoundCategory.PLAYERS);
            } else if (itemStack.isOf(ItemInit.GLITCH_ARROW)) {
                invertBlock(arrowEntity, target.getBlockPos());
                BlockPos pos = arrowEntity.getBlockPos();
                addGlitchEffect(pos);
            }
            StatusEffectUtil.applyAttributeModifier(livingEntity, ((AttributedProjectile) arrowEntity).getAttributes());
            StatusEffectUtil.applyAttributeRatioModifier(livingEntity, this.itemStack, ((AttributedProjectile) arrowEntity).getRatioAttributes());
        }
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(ITEM_STACK, new ItemStack(Items.ARROW));
    }
}

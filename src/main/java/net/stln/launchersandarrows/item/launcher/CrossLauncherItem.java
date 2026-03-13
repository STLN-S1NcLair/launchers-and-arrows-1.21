package net.stln.launchersandarrows.item.launcher;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.launchersandarrows.entity.projectile.ItemProjectile;
import net.stln.launchersandarrows.sound.SoundInit;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CrossLauncherItem extends CrossbowItem {
    private static final float MAX_CHARGE_DURATION = 1.25F;
    public static final int DEFAULT_RANGE = 8;
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private static final float START_SOUND_PERCENT = 0.2F;
    private static final float MID_SOUND_PERCENT = 0.5F;
    private static final float ARROW_POWER = 3.15F;
    private static final float FIREWORK_POWER = 1.6F;
    public static final float MOB_ARROW_POWER = 1.6F;
    private static final ChargingSounds DEFAULT_SOUNDS = new ChargingSounds(
            Optional.of(SoundEvents.CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.CROSSBOW_LOADING_END)
    );

    public static final Predicate<ItemStack> CROSSLAUNCHER_HELD_PROJECTILES = ARROW_ONLY
            .or(stack -> stack.is(Items.FIREWORK_ROCKET))
            .or(stack -> stack.is(Items.POTION))
            .or(stack -> stack.is(Items.SPLASH_POTION))
            .or(stack -> stack.is(Items.LINGERING_POTION))
            .or(stack -> stack.is(Items.TORCH))
            .or(stack -> stack.is(Items.SOUL_TORCH))
            .or(stack -> stack.is(Items.REDSTONE_TORCH))
            .or(stack -> stack.is(Items.BLAZE_ROD))
            .or(stack -> stack.is(Items.WIND_CHARGE))
            .or(stack -> stack.is(Items.FIRE_CHARGE))
            .or(stack -> stack.is(Items.GLOW_INK_SAC))
            .or(stack -> stack.is(Items.INK_SAC))
            .or(stack -> stack.is(Items.AMETHYST_SHARD))
            .or(stack -> stack.is(Items.SLIME_BALL))
            .or(stack -> stack.is(Items.SNOWBALL))
            .or(stack -> stack.is(Items.EGG))
            .or(stack -> stack.is(Items.ENDER_PEARL))
            .or(stack -> stack.is(Items.TRIDENT))
            .or(stack -> stack.is(Items.DRAGON_BREATH))
            .or(stack -> stack.is(Items.END_ROD))
            .or(stack -> stack.is(Items.ENDER_EYE))
            .or(stack -> stack.is(Items.MAGMA_CREAM))
            .or(stack -> stack.is(Items.ECHO_SHARD))
            .or(stack -> stack.is(Items.HEART_OF_THE_SEA))
            .or(stack -> stack.is(Items.HEAVY_CORE))
            .or(stack -> stack.is(Items.POINTED_DRIPSTONE))
            .or(stack -> stack.is(Items.LIGHTNING_ROD));


    public CrossLauncherItem(Properties properties) {
        super(properties);
    }

    // f: getHeldProjectiles
    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return CROSSLAUNCHER_HELD_PROJECTILES;
    }

    // f: getProjectiles
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return CROSSLAUNCHER_HELD_PROJECTILES;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ChargedProjectiles chargedprojectiles = (ChargedProjectiles)itemstack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            this.performShooting(level, player, hand, itemstack, getShootingPower(chargedprojectiles), 1.0F, (LivingEntity)null);
            if(getShootSound(chargedprojectiles) != null){
                player.level().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        getShootSound(chargedprojectiles),
                        player.getSoundSource(),
                        1.0F,
                        1.0F / (player.getRandom().nextFloat() * 0.5F + 1.8F) * (float) Math.sqrt(getShootingPower(chargedprojectiles)) + 0.43F
                );
            }
            return InteractionResultHolder.consume(itemstack);
        } else if (!player.getProjectile(itemstack).isEmpty()) {
            this.startSoundPlayed = false;
            this.midLoadSoundPlayed = false;
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }
    // f: shootAll
    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
        float g = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
        float h = (float)((projectileItems.size() - 1) % 2) * g / 2.0F;
        float i = 1.0F;

        for (int j = 0; j < projectileItems.size(); j++) {
            ItemStack itemStack = (ItemStack)projectileItems.get(j);
            if (!itemStack.isEmpty()) {
                float k = h + i * (float)((j + 1) / 2) * g;
                i = -i;
                if (itemStack.is(Items.AMETHYST_SHARD)) {
                    for (int l = 0; l < 10; l++) {
                        Projectile projectile = this.createProjectile(level, shooter, weapon, itemStack, isCrit);
                        this.shootProjectile(shooter, projectile, j, velocity, inaccuracy * 10, k, target);
                        level.addFreshEntity(projectile);
                    }
                } else {
                    Projectile projectile = this.createProjectile(level, shooter, weapon, itemStack, isCrit);
                    this.shootProjectile(shooter, projectile, j, velocity, inaccuracy, k, target);
                    level.addFreshEntity(projectile);
                }
                weapon.hurtAndBreak(this.getDurabilityUse(itemStack), shooter, LivingEntity.getSlotForHand(hand));
                if (weapon.isEmpty()) {
                    break;
                }
            }
        }
    }

    // f: shoot
    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d0 = target.getX() - shooter.getX();
            double d1 = target.getZ() - shooter.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = target.getY(0.3333333333333333) - projectile.getY() + d2 * 0.20000000298023224;
            vector3f = getProjectileShotVector(shooter, new Vec3(d0, d3, d1), angle);
        } else {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(angle * 0.017453292F), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = shooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocity, inaccuracy);
        float h = 1.0F / (shooter.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundInit.CROSSLAUNCHER.get(), shooter.getSoundSource(), 1.0F, h);
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.COPPER_BULB_PLACE, shooter.getSoundSource(), 1.0F, h);
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.LANTERN_BREAK, shooter.getSoundSource(), 1.0F, h);
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.IRON_TRAPDOOR_OPEN, shooter.getSoundSource(), 1.0F, h - 0.7F);
    }

    // f: calcVelocity
    private static Vector3f getProjectileShotVector(LivingEntity shooter, Vec3 distance, float angle) {
        Vector3f vector3f = distance.toVector3f().normalize();
        Vector3f vector3f1 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            vector3f1 = (new Vector3f(vector3f)).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = (new Vector3f(vector3f)).rotateAxis((float)(Math.PI / 2), vector3f1.x, vector3f1.y, vector3f1.z);
        return (new Vector3f(vector3f)).rotateAxis(angle * (float)(Math.PI / 100.0), vector3f2.x, vector3f2.y, vector3f2.z);
    }

    // f: onStoppedUsing
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        float f = getPowerForTime(i, stack, entityLiving);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(entityLiving, stack)) {
            ChargingSounds chargingSounds = this.getChargingSounds(stack);
            chargingSounds.end()
                    .ifPresent(
                            sound -> level.playSound(
                                    null,
                                    entityLiving.getX(),
                                    entityLiving.getY(),
                                    entityLiving.getZ(),
                                    (SoundEvent)sound.value(),
                                    entityLiving.getSoundSource(),
                                    1.0F,
                                    1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                            )
                    );
        }
        float h = 1.0F / (entityLiving.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
        entityLiving.level().playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.DISPENSER_FAIL, entityLiving.getSoundSource(), 1.0F, h + 1.0F);
        entityLiving.level().playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.IRON_DOOR_OPEN, entityLiving.getSoundSource(), 1.0F, h + 1.0F);
    }

    // f: loadProjectiles
    private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack crossbowStack) {
        List<ItemStack> list = draw(crossbowStack, shooter.getProjectile(crossbowStack), shooter);
        if (!list.isEmpty()) {
            crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        } else {
            return false;
        }
    }

    // f: createArrowEntity
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        if (ammo.is(Items.FIREWORK_ROCKET)) {
            return new FireworkRocketEntity(level, ammo, shooter, shooter.getX(), shooter.getEyeY() - 0.15F, shooter.getZ(), true);
        } else if (ammo.is(Items.POTION) || ammo.is(Items.SPLASH_POTION) || ammo.is(Items.LINGERING_POTION)) {
            ThrownPotion potionEntity = new ThrownPotion(level, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
            potionEntity.setItem(ammo);
            return potionEntity;

        } else if (ammo.is(Items.BLAZE_ROD)) {
            Projectile entity = new SmallFireball(level, shooter, new Vec3(0, 0, 0));
            entity.setPos(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
            return entity;

        } else if (ammo.is(Items.WIND_CHARGE)) {
            return new AbstractWindCharge(EntityType.BREEZE_WIND_CHARGE, level, shooter, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ()) {
                @Override
                protected void explode(Vec3 pos) {
                    this.level()
                            .explode(
                                    this,
                                    null,
                                    EXPLOSION_DAMAGE_CALCULATOR,
                                    pos.x(),
                                    pos.y(),
                                    pos.z(),
                                    10.0F,
                                    false,
                                    Level.ExplosionInteraction.TRIGGER,
                                    ParticleTypes.GUST_EMITTER_SMALL,
                                    ParticleTypes.GUST_EMITTER_LARGE,
                                    SoundEvents.BREEZE_WIND_CHARGE_BURST
                            );
                }
            };

        } else if (ammo.is(Items.FIRE_CHARGE)) {
            Projectile entity = new LargeFireball(level, shooter, new Vec3(0, 0, 0), 1);
            entity.setPos(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
            return entity;

        } else if (ammo.is(Items.SNOWBALL)) {
            return new Snowball(level, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());

        } else if (ammo.is(Items.EGG)) {
            return new ThrownEgg(level, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());

        } else if (ammo.is(Items.ENDER_PEARL)) {
            return new ThrownEnderpearl(level, shooter);

        } else if (ammo.is(Items.TRIDENT)) {
            return new ThrownTrident(level, shooter, ammo);

        } else if (ammo.is(Items.DRAGON_BREATH)) {
            Projectile entity = new DragonFireball(level, shooter, new Vec3(0, 0, 0));
            entity.setPos(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
            return entity;

        } else if (ammo.is(Items.END_ROD)) {
            Projectile entity = new ShulkerBullet(EntityType.SHULKER_BULLET, level);
            entity.setOwner(shooter);
            entity.setPos(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
            return entity;

        } else if (ammo.is(ItemTags.ARROWS)) {
            Projectile projectile = super.createProjectile(level, shooter, weapon, ammo, isCrit);
            if (projectile instanceof AbstractArrow arrow) {
                arrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
            }
            return projectile;
        } else {
            return new ItemProjectile(level, shooter, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), ammo);
        }
    }


    private static float getShootingPower(ChargedProjectiles stack) {
        float v;
        if (stack.contains(Items.POTION)) {
            v = 0.5F;
        } else if (stack.contains(Items.BLAZE_ROD) || stack.contains(Items.HEAVY_CORE)) {
            v = 1.0F;
        } else if (stack.contains(Items.POINTED_DRIPSTONE)) {
            v = 2.0F;
        } else if (stack.contains(Items.FIRE_CHARGE) || stack.contains(Items.DRAGON_BREATH) || stack.contains(Items.END_ROD)) {
            v = 2.5F;
        } else if (stack.contains(Items.SPLASH_POTION) || stack.contains(Items.LINGERING_POTION)
                || stack.contains(Items.SLIME_BALL)
                || stack.contains(Items.TORCH) || stack.contains(Items.SOUL_TORCH) || stack.contains(Items.REDSTONE_TORCH)
                || stack.contains(Items.GLOW_INK_SAC) || stack.contains(Items.INK_SAC)
                || stack.contains(Items.ENDER_EYE) || stack.contains(Items.MAGMA_CREAM)
                || stack.contains(Items.ECHO_SHARD) || stack.contains(Items.HEART_OF_THE_SEA)
                || stack.contains(Items.LIGHTNING_ROD)) {
            v = 3.0F;
        } else if (stack.contains(Items.WIND_CHARGE) || stack.contains(Items.SNOWBALL) || stack.contains(Items.EGG) || stack.contains(Items.ENDER_PEARL) || stack.contains(Items.TRIDENT)) {
            v = 5.0F;
        } else {
            v = 1.5F;
        }
        return v;
    }


    private static SoundEvent getShootSound(ChargedProjectiles stack) {
        SoundEvent soundEvent;
        if (stack.contains(Items.POTION) || stack.contains(Items.SPLASH_POTION) || stack.contains(Items.LINGERING_POTION)) {
            soundEvent = SoundEvents.BOTTLE_FILL;
        } else if (stack.contains(Items.SNOWBALL) || stack.contains(Items.EGG) || stack.contains(Items.ENDER_PEARL)) {
            soundEvent = SoundEvents.ENDER_PEARL_THROW;
        } else if (stack.contains(Items.BLAZE_ROD)) {
            soundEvent = SoundEvents.BLAZE_SHOOT;
        } else if (stack.contains(Items.WIND_CHARGE)) {
            soundEvent = SoundEvents.BREEZE_SHOOT;
        } else if (stack.contains(Items.FIRE_CHARGE)) {
            soundEvent = SoundEvents.FIRECHARGE_USE;
        } else if (stack.contains(Items.TRIDENT)) {
            soundEvent = SoundEvents.TRIDENT_THROW.value();
        } else if (stack.contains(Items.DRAGON_BREATH)) {
            soundEvent = SoundEvents.ENDER_DRAGON_SHOOT;
        } else if (stack.contains(Items.SLIME_BALL) || stack.contains(Items.MAGMA_CREAM)) {
            soundEvent = SoundEvents.SLIME_JUMP;
        } else if (stack.contains(Items.TORCH)) {
            soundEvent = SoundEvents.WOOD_BREAK;
        } else if (stack.contains(Items.GLOW_INK_SAC) || stack.contains(Items.INK_SAC)) {
            soundEvent = SoundEvents.ELDER_GUARDIAN_FLOP;
        } else if (stack.contains(Items.END_ROD)) {
            soundEvent = SoundEvents.SHULKER_SHOOT;
        } else if (stack.contains(Items.AMETHYST_SHARD)) {
            soundEvent = SoundEvents.AMETHYST_BLOCK_BREAK;
        } else if (stack.contains(Items.ENDER_EYE)) {
            soundEvent = SoundEvents.ENDER_EYE_DEATH;
        } else if (stack.contains(Items.ECHO_SHARD)) {
            soundEvent = SoundEvents.SCULK_SHRIEKER_SHRIEK;
        } else if (stack.contains(Items.HEART_OF_THE_SEA)) {
            soundEvent = SoundEvents.CONDUIT_ACTIVATE;
        } else if (stack.contains(Items.HEAVY_CORE)) {
            soundEvent = SoundEvents.HEAVY_CORE_BREAK;
        } else if (stack.contains(Items.POINTED_DRIPSTONE)) {
            soundEvent = SoundEvents.DRIPSTONE_BLOCK_BREAK;
        } else if (stack.contains(Items.LIGHTNING_ROD)) {
            soundEvent = SoundEvents.TRIDENT_THUNDER.value();
        } else {
            soundEvent = null;
        }
        return soundEvent;
    }

    // f: usageTick
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (!level.isClientSide) {
            ChargingSounds chargingsounds = this.getChargingSounds(stack);
            float f = (float)(stack.getUseDuration(livingEntity) - count) / (float)getChargeDuration(stack, livingEntity);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                chargingsounds.start().ifPresent((sound) -> {
                    level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F);
                });
            }

            if (f >= 0.5F && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                chargingsounds.mid().ifPresent((sound) -> {
                    level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F);
                });
            }
        }
    }

    // f: getMaxUseTime
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return getChargeDuration(stack, entity) + 3;
    }

    // f: getPullTime
    public static int getChargeDuration(ItemStack stack, LivingEntity shooter) {
        float f = EnchantmentHelper.modifyCrossbowChargingTime(stack, shooter, 0.5F);
        return Mth.floor(f * 20.0F);
    }

    // f: getLoadingSounds
    ChargingSounds getChargingSounds(ItemStack stack) {
        return (ChargingSounds)EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS)
                .orElse(DEFAULT_SOUNDS);
    }

    // f: getPullProgress
    private static float getPowerForTime(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float)useTicks / (float)getChargeDuration(stack, user);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}

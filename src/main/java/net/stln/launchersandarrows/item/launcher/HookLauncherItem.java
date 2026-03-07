package net.stln.launchersandarrows.item.launcher;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.launchersandarrows.entity.projectile.ItemProjectile;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.sound.SoundInit;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class HookLauncherItem extends CrossbowItem {
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

    public static final Predicate<ItemStack> HOOK_LAUNCHER_HELD_PROJECTILES =
            (stack) -> stack.is(ItemInit.GRAPPLING_HOOK);

    public HookLauncherItem(Properties properties) {
        super(properties);
    }

    // f: getHeldProjectiles
    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return HOOK_LAUNCHER_HELD_PROJECTILES;
    }

    // f: getProjectiles
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return HOOK_LAUNCHER_HELD_PROJECTILES;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ChargedProjectiles chargedprojectiles = (ChargedProjectiles)itemstack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            this.performShooting(level, player, hand, itemstack, 5, 1.0F, (LivingEntity)null);
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

    // f: onStoppedUsing
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        float f = getPowerForTime(i, stack, entityLiving);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(entityLiving, stack)) {
            ChargingSounds crossbowitem$chargingsounds = this.getChargingSounds(stack);
            crossbowitem$chargingsounds.end().ifPresent((sound) -> {
                level.playSound((Player)null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), (SoundEvent)sound.value(), entityLiving.getSoundSource(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
            });
        }
        float h = 1.0F / (entityLiving.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
        entityLiving.level().playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.DISPENSER_FAIL, entityLiving.getSoundSource(), 1.0F, h + 1.0F);
        entityLiving.level().playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.IRON_DOOR_OPEN, entityLiving.getSoundSource(), 1.0F, h + 1.0F);
    }

    private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack crossbowStack) {
        List<ItemStack> list = draw(crossbowStack, shooter.getProjectile(crossbowStack), shooter);
        if (!list.isEmpty()) {
            crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        } else {
            return false;
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
            double d3 = target.getY(0.3333333333333333) - projectile.getY() + d2 * 0.2F;
            vector3f = getProjectileShotVector(shooter, new Vec3(d0, d3, d1), angle);
        } else {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(angle * (float)(Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = shooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocity, inaccuracy);
        float f = 1.0F / (shooter.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
        shooter.level().playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundInit.BOW_RELEASE.get(), shooter.getSoundSource(), 1.0F, f);
        shooter.level().playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.VAULT_INSERT_ITEM, shooter.getSoundSource(), 1.0F, f);
        shooter.level().playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.IRON_TRAPDOOR_OPEN, shooter.getSoundSource(), 1.0F, f - 0.7F);
    }

    // f: calcVelocity
    private static Vector3f getProjectileShotVector(LivingEntity shooter, Vec3 distance, float angle) {
        Vector3f vector3f = distance.toVector3f().normalize();
        Vector3f vector3f1 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            vector3f1 = (new Vector3f(vector3f)).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = (new Vector3f(vector3f)).rotateAxis(1.5707964F, vector3f1.x, vector3f1.y, vector3f1.z);
        return (new Vector3f(vector3f)).rotateAxis(angle * 0.017453292F, vector3f2.x, vector3f2.y, vector3f2.z);
    }

    // f: createArrowEntity
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        return new ItemProjectile(level, shooter, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), ammo);
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
    public static int getChargeDuration(ItemStack stack, LivingEntity user) {
        float f = EnchantmentHelper.modifyCrossbowChargingTime(stack, user, 0.5F);
        return Mth.floor(f * 20.0F);
    }

    // f: getLoadingSounds
    ChargingSounds getChargingSounds(ItemStack stack) {
        return (ChargingSounds) EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS)
                .orElse(DEFAULT_SOUNDS);
    }

    // f: getPullProgress
    private static float getPowerForTime(int timeLeft, ItemStack stack, LivingEntity shooter) {
        float f = (float)timeLeft / (float)getChargeDuration(stack, shooter);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}

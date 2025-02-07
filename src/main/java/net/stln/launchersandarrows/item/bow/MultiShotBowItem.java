package net.stln.launchersandarrows.item.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.item.FovModifierItem;
import net.stln.launchersandarrows.sound.SoundInit;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MultiShotBowItem extends ModfiableBowItem implements FovModifierItem{

    float fov = 1.0f;

    public MultiShotBowItem(Settings settings) {
        super(settings);
        pulltime = 20;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound((Entity) user, user.getBlockPos(), SoundEvents.ITEM_CROSSBOW_LOADING_END.value(), SoundCategory.PLAYERS, 1f, 1.5f);
        world.playSound((Entity) user, user.getBlockPos(), SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE.value(), SoundCategory.PLAYERS, 1f, 1.0f);
        return super.use(world, user, hand);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        this.fov = 1.0f - getModifiedPullProgress(getMaxUseTime(stack, user) - remainingUseTicks, stack) / 9.0f;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            for (int j = 0; j < 3; j++) {
                ItemStack itemStack = this.getProjectileTypeWithSelector(playerEntity, stack);
                if (!itemStack.isEmpty()) {
                    int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                    float f = getModifiedPullProgress(i, stack);
                    if (!((double) f < 0.3)) {
                        List<ItemStack> list = load(stack, itemStack, playerEntity);
                        if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
                            this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, f * 2.0F, 10.0F, f == 1.0F, null);
                        }

                        world.playSound(
                                null,
                                playerEntity.getX(),
                                playerEntity.getY(),
                                playerEntity.getZ(),
                                SoundInit.BOW_RELEASE,
                                SoundCategory.PLAYERS,
                                1.5F,
                                1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                        );
                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    }
                }
            }
        }
    }

    @Override
    protected void shootAll(
            ServerWorld world,
            LivingEntity shooter,
            Hand hand,
            ItemStack stack,
            List<ItemStack> projectiles,
            float speed,
            float divergence,
            boolean critical,
            @Nullable LivingEntity target
    ) {
        speed = applySpeedModifier(shooter, stack, speed);
        float f = EnchantmentHelper.getProjectileSpread(world, stack, shooter, 0.0F);
        float g = projectiles.size() == 1 ? 0.0F : 2.0F * f / (float)(projectiles.size() - 1);
        float h = (float)((projectiles.size() - 1) % 2) * g / 2.0F;
        float i = 1.0F;

        for (int j = 0; j < projectiles.size(); j++) {
            ItemStack itemStack = (ItemStack)projectiles.get(j);
            if (!itemStack.isEmpty()) {
                float k = h + i * (float)((j + 1) / 2) * g;
                i = -i;
                ProjectileEntity projectileEntity = this.createArrowEntity(world, shooter, stack, itemStack, critical);
                ((BypassDamageCooldownProjectile)projectileEntity).setBypass(true);
                speed = applySpeedModifier(shooter, stack, speed);
                divergence = applyPrecisionModifier(shooter, stack, divergence);
                this.shoot(shooter, projectileEntity, j, speed, divergence, k, target);
                world.spawnEntity(projectileEntity);
                stack.damage(this.getWeaponStackDamage(itemStack), shooter, LivingEntity.getSlotForHand(hand));
                if (stack.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    public float getFov() {
        return this.fov;
    }

    @Override
    public void resetFov() {
        this.fov = 1.0F;
    }
}

package net.stln.launchersandarrows.item.bow;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.item.FovModifierItem;
import net.stln.launchersandarrows.sound.SoundInit;

import javax.annotation.Nullable;
import java.util.List;

public class MultiShotBowItem extends ModifiableBowItem implements FovModifierItem{

    public MultiShotBowItem(Properties properties) {
        super(properties);
        pulltime = 20;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.5F);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 1.0F, 1.0F);
        return super.use(level, player, hand);
    }

    //f: onStoppedUsing
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            for(int j = 0; j < 3; j++){
                ItemStack itemstack = player.getProjectile(stack);
                if (!itemstack.isEmpty()) {
                    int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                    float f = getModifiedPullProgress(i, stack);
                    if (!((double) f < 0.3)) {
                        List<ItemStack> list = draw(stack, itemstack, player);
                        if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 2.0F, 10.0F, f == 1.0F, (LivingEntity) null);
                        }

                        level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundInit.BOW_RELEASE.get(), SoundSource.PLAYERS, 1.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        player.awardStat(Stats.ITEM_USED.get(this));
                    }
                }
            }
        }
    }

    //f: shootAll
    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        velocity = applySpeedModifier(shooter, weapon, velocity);
        float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0F); //f: getProjectileSpread
        float g = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
        float h = (float)((projectileItems.size() - 1) % 2) * g / 2.0F;
        float i = 1.0F;

        for (int j = 0; j < projectileItems.size(); j++) {
            ItemStack itemStack = projectileItems.get(j);
            if (!itemStack.isEmpty()) {
                float k = h + i * (float)((j + 1) / 2) * g;
                i = -i;
                Projectile projectile = this.createProjectile(level, shooter, weapon, itemStack, isCrit);
                ((BypassDamageCooldownProjectile)projectile).setBypass(true);
                velocity = applySpeedModifier(shooter, weapon, velocity);
                inaccuracy = applyPrecisionModifier(shooter, weapon, inaccuracy);
                this.shootProjectile(shooter, projectile, j, velocity, inaccuracy, k, target);
                level.addFreshEntity(projectile);
                weapon.hurtAndBreak(this.getDurabilityUse(itemStack), shooter, LivingEntity.getSlotForHand(hand));
                if (weapon.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    public float getFovModifier(Player player, ItemStack stack) {
        int useTicks = player.getTicksUsingItem();
        float progress = getModifiedPullProgress(useTicks, stack);
        return 1.0F - progress / 9.0F;
    }
}

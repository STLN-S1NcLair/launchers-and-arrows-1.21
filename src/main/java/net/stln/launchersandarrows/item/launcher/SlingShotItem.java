package net.stln.launchersandarrows.item.launcher;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.entity.projectile.ItemProjectile;
import net.stln.launchersandarrows.item.FovModifierItem;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;
import net.stln.launchersandarrows.sound.SoundInit;

import java.util.List;
import java.util.function.Predicate;

public class SlingShotItem extends ModifiableBowItem implements FovModifierItem {
    float fov = 1.0F;

    public static final Predicate<ItemStack> SLINGSHOT_HELD_PROJECTILES = (stack -> {
        return stack.getItem() instanceof BlockItem && !stack.is(Items.HEAVY_CORE);
    });

    public SlingShotItem(Properties properties) {
        super(properties);
        pulltime = 10;
        slotsize = 0;
    }

    // f: getHeldProjectiles
    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return SLINGSHOT_HELD_PROJECTILES;
    }

    // f: getProjectiles
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return SLINGSHOT_HELD_PROJECTILES;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1F, 1.5F);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 1F, 1.5F);
        return super.use(level, player, hand);
    }

    // f: usageTick
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        this.fov = 1.0F - getModifiedPullProgress(getUseDuration(stack, livingEntity) - remainingUseDuration, stack) / 4.0F;
        if(livingEntity.isShiftKeyDown()){
            this.fov *= 0.5F;
        }
    }

    // f: onStoppedUsing
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                float f = getModifiedPullProgress(i, stack);
                if (!((double) f < 0.3)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 6.0F, 1.0F, f == 1.0F, (LivingEntity) null);
                    }

                    level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundInit.BOW_RELEASE.get(), SoundSource.PLAYERS, 1.5F, 0.75F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    // f: createArrowEntity
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        return new ItemProjectile(level, shooter, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), ammo);
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

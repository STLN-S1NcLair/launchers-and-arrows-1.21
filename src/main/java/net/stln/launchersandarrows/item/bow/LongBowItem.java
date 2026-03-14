package net.stln.launchersandarrows.item.bow;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.item.FovModifierItem;
import net.stln.launchersandarrows.sound.SoundInit;

import java.util.List;

public class LongBowItem extends ModifiableBowItem implements FovModifierItem{

    public LongBowItem(Properties properties) {
        super(properties);
        pulltime = 40;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.5F);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 1.0F, 0.5F);
        return super.use(level, player, hand);
    }

    //f: onStoppedUsing
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
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 6.0F, 0.5F, f == 1.0F, (LivingEntity) null);
                    }

                    level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundInit.BOW_RELEASE.get(), SoundSource.PLAYERS, 1.5F, 0.75F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public float getFovModifier(Player player, ItemStack stack) {
        int useTicks = player.getTicksUsingItem();
        float progress = getModifiedPullProgress(useTicks, stack);
        float fov = 1.0F - progress / 4.0F;
        if(player.isShiftKeyDown()){
            fov *= 0.5F;
        }
        return fov;
    }
}

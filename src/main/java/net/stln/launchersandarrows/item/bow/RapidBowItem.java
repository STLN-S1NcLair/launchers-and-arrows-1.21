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
import net.stln.launchersandarrows.sound.SoundInit;

import java.util.List;

public class RapidBowItem extends ModifiableBowItem{

    public RapidBowItem(Properties properties) {
        super(properties);
        pulltime = 10;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.5F);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 1.0F, 2.0F);
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
                if (!((double) f < 0.5)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 1.5F, 1.0F, f == 1.0F, (LivingEntity) null);
                    }

                    level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundInit.BOW_RELEASE.get(), SoundSource.PLAYERS, 1.5F, 2.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        this.releaseUsing(stack, level, livingEntity, 0);
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        float lightweightMod = 1F;
        /*
        for(int i = 0; i< slotsize; i++){
            if(i < getModifiers(stack).size()){
                ItemStack modifier = getModifier(i, stack);
                //lightweight modifierの分を引く処理
            }
        }
        */
        // lightweightMod = lightweightMod < 0 ? 0 : lightweightMod;
        return (int) Math.ceil(lightweightMod * this.pulltime);
    }
}

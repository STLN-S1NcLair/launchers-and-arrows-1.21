package net.stln.launchersandarrows.item.bow;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.item.FovModifierItem;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.sound.SoundInit;

import javax.annotation.Nullable;
import java.util.List;

public class RainShotBowItem extends ModifiableBowItem implements FovModifierItem {

    float fov = 1.0F;

    public RainShotBowItem(Properties properties) {
        super(properties);
        pulltime = 40;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.5F);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 1.0F, 1.0F);
        return super.use(level, player, hand);
    }

    //f: usageTick
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        this.fov = 1.0F - getModifiedPullProgress(getUseDuration(stack, livingEntity) - remainingUseDuration, stack) / 9.0F;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        Integer charge = stack.get(ComponentInit.CHARGE_COUNT_COMPONENT);
        if(entity instanceof Player player && (player.getMainHandItem().equals(stack) || player.getOffhandItem().equals(stack)) && charge > 0){
            generateArrow(stack, level, (LivingEntity)entity, getUseDuration(stack, (LivingEntity)entity) - pulltime, player);
            stack.set(ComponentInit.CHARGE_COUNT_COMPONENT, charge - 1);
        }
        else {
            stack.set(ComponentInit.CHARGE_COUNT_COMPONENT, 0);
        }
    }

    //f: onStoppedUsing
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            generateArrow(stack, level, entityLiving, timeLeft, player);
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            float f = getModifiedPullProgress(i, stack);
            if(f > 0.3){
                stack.set(ComponentInit.CHARGE_COUNT_COMPONENT, Math.round(f*5));
            }
        }
    }

    private void generateArrow(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, Player player){
        ItemStack itemstack = this.getProjectileTypeWithSelector(player, stack);
        if(!itemstack.isEmpty()){
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            float f = getModifiedPullProgress(i, stack);
            if((double)f > 0.95){
                List<ItemStack> list = draw(stack, itemstack, player);
                if( level instanceof ServerLevel serverLevel && !list.isEmpty()){
                    this.shoot(serverLevel, player, player.getUsedItemHand(), stack, list, f * 2.0F, 5.0F, f == 1.0F, null);
                }

                level.playSound(null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundInit.BOW_RELEASE.get(),
                        SoundSource.PLAYERS,
                        1.5F,
                        1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                );

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    //f: shootAll
    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        velocity = applySpeedModifier(shooter, weapon, velocity);
        float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
        float f2 = (float)((projectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for(int i = 0; i < projectileItems.size(); ++i) {
            ItemStack itemstack = (ItemStack)projectileItems.get(i);
            if (!itemstack.isEmpty()) {
                float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile projectile = this.createProjectile(level, shooter, weapon, itemstack, isCrit);
                ((BypassDamageCooldownProjectile)projectile).setBypass(true);
                velocity = applySpeedModifier(shooter, weapon, velocity);
                inaccuracy = applyPrecisionModifier(shooter, weapon, inaccuracy);
                this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
                level.addFreshEntity(projectile);
                weapon.hurtAndBreak(this.getDurabilityUse(itemstack), shooter, LivingEntity.getSlotForHand(hand));
                if (weapon.isEmpty()) {
                    break;
                }
            }
        }
    }


    @Override
    public float getFov(){
        return this.fov;
    }

    @Override
    public void resetFov(){
        this.fov = 1.0F;
    }
}

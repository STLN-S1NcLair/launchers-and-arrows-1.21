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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.item.component.ChargeComponent;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.sound.SoundInit;

import java.util.ArrayList;
import java.util.List;

public class MechanicalBowItem extends ModifiableBowItem {

    // float fov = 1.0F;

    int chargeSlot = 3;

    public MechanicalBowItem(Properties properties) {
        super(properties);
        pulltime = 15;
        slotsize = 3;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!player.isShiftKeyDown()){
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.5F);
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        else {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.MECHANICAL_BOW_CHARGE.get(), SoundSource.PLAYERS, 1.0F, 0.8F);
        }
        return super.use(level, player, hand);
    }

    //f: onStoppedUsing
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if(entityLiving instanceof Player player){
            if(player.isShiftKeyDown() && stack.get(ComponentInit.CHARGE_COMPONENT).getCharges().getLast() == 0){
                ChargeComponent component = stack.get(ComponentInit.CHARGE_COMPONENT);
                List<Double> charges = component.getCharges();
                List<Double> newCharges = new ArrayList<>(List.of());
                newCharges.addAll(charges);
                int index = 0;
                for(int j = 0; j < charges.size(); j++){
                    if(charges.get(j) == 0){
                        index = j;
                        break;
                    }
                }
                float f = getModifiedPullProgress(this.getUseDuration(stack, entityLiving) - timeLeft, stack);
                newCharges.set(index, (double) f);
                stack.set(ComponentInit.CHARGE_COMPONENT, ChargeComponent.of(newCharges));
                float h = 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F;
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.MECHANICAL_BOW_LOAD.get(), SoundSource.PLAYERS, 1.5F, h);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.BOW_RELEASE.get(), SoundSource.PLAYERS, 1.5F, h);
            }
            else {
                ItemStack itemStack = this.getProjectileTypeWithSelector(player, stack);
                if(!itemStack.isEmpty()){
                    int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                    float f = getModifiedPullProgress(i, stack);
                    ChargeComponent component = stack.get(ComponentInit.CHARGE_COMPONENT);
                    List<Double> charges = component.getCharges();
                    if((double) f > 0.5 && i > 2){
                        generateArrow(stack, level, player, itemStack, f);
                    }
                    else if(stack.get(ComponentInit.CHARGE_COMPONENT).getCharges().getFirst() != 0){
                        int index = charges.size() - 1;
                        for(int j = 0; j < charges.size(); j++){
                            if(charges.get(j) == 0){
                                index = j - 1;
                                break;
                            }
                        }
                        double charge = charges.get(index);
                        List<Double> newCharges = new ArrayList<>(List.of());
                        newCharges.addAll(charges);
                        newCharges.set(index, 0.0);
                        stack.set(ComponentInit.CHARGE_COMPONENT, ChargeComponent.of(newCharges));
                        generateArrow(stack, level, player, itemStack, (float) charge);
                        level.playSound(null,
                                player.getX(), player.getY(), player.getZ(),
                                SoundInit.BOW_RELEASE.get(),
                                SoundSource.PLAYERS,
                                1.5F,
                                1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                        );
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        ChargeComponent component = stack.get(ComponentInit.CHARGE_COMPONENT);
        List<Double> charges = component.getCharges();
        if(!isSelected){
            int index = 0;
            for(int i = 0; i < charges.size(); i++){
                if(charges.get(i) != 0){
                    index = i;
                }
            }
            stack.set(ComponentInit.CHARGE_COMPONENT, ChargeComponent.of(List.of(charges.get(index), 0.0, 0.0)));
        }
    }

    /*
    //f: usageTick
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
        if(livingEntity instanceof Player player && player.isShiftKeyDown()){
            stack.set(ComponentInit.CHARGING_COMPONENT, true);
        }
    }
    */

    private void generateArrow(ItemStack weapon, Level level, Player player, ItemStack itemStack, float f) {
        List<ItemStack> projectileList = draw(weapon, itemStack, player);
        if(level instanceof ServerLevel serverLevel && !projectileList.isEmpty()){
            this.shoot(serverLevel, player, player.getUsedItemHand(), weapon, projectileList, f * 3.5F, 1.0F, f == 1.0F, null);
        }
        float h = 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F;
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.MECHANICAL_BOW_LOAD.get(), SoundSource.PLAYERS, 1.5F, h);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.BOW_RELEASE.get(), SoundSource.PLAYERS, 1.5F, h);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    /*
    @Override
    public float getFov(){
        return this.fov;
    }

    @Override
    public void resetFov(){
        this.fov = 1.0F;
    }
    */
}

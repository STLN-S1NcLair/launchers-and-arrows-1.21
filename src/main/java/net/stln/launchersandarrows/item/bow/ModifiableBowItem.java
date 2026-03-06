package net.stln.launchersandarrows.item.bow;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.entity.RicochetProjectile;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.util.ModifierEnum;

import javax.annotation.Nullable;
import java.util.List;

public class ModifiableBowItem extends BowItem {
    protected int slotsize = 3;
    protected int pulltime = 40;

    public ModifiableBowItem(Properties properties) {
        super(properties);
    }

    public void setModifier(int slot, ItemStack bow, ItemStack modifier){
        //後で書く
    }

    public ItemStack getModifier(int slot, ItemStack bow){
        //後で書く
        return null;
    }

    public List<ItemStack> getModifiers(ItemStack bow){
        //後で書く
        return List.of(); //仮
        //return null;
    }

    // f: createArrowEntity
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        Projectile entity = super.createProjectile(level, shooter, weapon, ammo, isCrit);
        /*
        if(entity instanceof AbstractArrow arrow){
            for (int i = 0; i < slotsize; i++){
                if (i < getModifiers(weapon).size()){
                    ItemStack modifier = getModifier(i, weapon);
                    if (modifier != null){
                        for (int j = 0; j < 13; j++){
                            if (AttributeModifierDictionary.getDict().containsKey2(modifier.getItem(), j - 6)){
                                ((AttributedProjectile) arrow).setAttribute(j - 6,
                                        AttributeModifierDictionary.getAttributeEffect(modifier.getItem(), j - 6) + ((AttributedProjectile) arrow).getAttribute(j - 6));
                            }
                        }
                        if(ModifierDictionary.getDict().containsKey2(modifier.getItem(), ModifierEnum.RICOCHET.get())){
                            ((RicochetProjectile) arrow).setRicochet(ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.RICOCHET.get()) + ((RicochetProjectile) arrow).getRicochet());
                        }
                    }
                }
            }
            return arrow;
        }
        */
        return entity;
    }

    /*
    protected ItemStack getProjectileTypeWithSelector(Player player, ItemStack stack){
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        Predicate<ItemStack> predicate = ((ProjectileWeaponItem)stack.getItem()).getSupportedHeldProjectiles();
        String selector; //Arrow Selector Componentがどうのこうの
        if(!offHandStack.isEmpty() && predicate.test(offHandStack)){
            //Arrow Selector Componentに関する処理
            return offHandStack;
        }
        else if(!mainHandStack.isEmpty() && predicate.test(mainHandStack)){
            //Arrow Selector Componentに関する処理
            return mainHandStack;
        }
        else if(selector != null && !selector.isEmpty()) {
            for(int i=0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack invStack = player.getInventory().getItem(i);
                if(selector.equals(invStack.getItem().getName().getString())) {
                    return invStack;
                }
            }
        }
        return player.getProjectileType(stack);
    }
    */

    //f: shootAll
    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        velocity = applySpeedModifier(shooter, weapon, velocity);
        inaccuracy = applyPrecisionModifier(shooter, weapon, inaccuracy);
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        // Self Repair Componentに関する処理がここに来る
    }

    protected float applySpeedModifier(LivingEntity shooter, ItemStack stack, float velocity){
        float sturdyPercentage = 0F;
        for(int i=0; i < slotsize; i++){
            if(i < getModifiers(stack).size()){
                ItemStack modifier = getModifier(i, stack);
                if(ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.RANGE.get()) != null){
                    velocity *= (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.RANGE.get()) + 100) / 100.0F;
                }
                if(ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.STURDY.get()) != null){
                    sturdyPercentage += (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.STURDY.get())) / 100.0F;
                }
            }
        }
        if(shooter.getRandom().nextFloat() < sturdyPercentage){
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1)); //AIに文句言われたので負数回避入れてみた
        }
        return velocity;
    }

    protected float applyPrecisionModifier(LivingEntity shooter, ItemStack stack, float inaccuracy){
        for(int i=0; i < slotsize; i++){
            if(i < getModifiers(stack).size()){
                ItemStack modifier = getModifier(i, stack);
                if(ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.PRECISION.get()) != null){
                    inaccuracy *= 1 - ((ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.PRECISION.get())) / 100.0F);
                }
            }
        }
        return inaccuracy;
    }

    public int getSlotsize() {
        return slotsize;
    }

    public int getPulltime() {
        return pulltime;
    }

    public float getModifiedPullProgress(int useTicks, ItemStack stack){
        float lightweightMod = 1F;
        for(int i=0; i < slotsize; i++){
            if(i < getModifiers(stack).size()){
                ItemStack modifier = getModifier(i, stack);
                if(ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.LIGHTWEIGHT.get()) != null){
                    lightweightMod -= ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.LIGHTWEIGHT.get()) / 100.0F;
                }
            }
        }
        lightweightMod = lightweightMod < 0 ? 0 : lightweightMod;
        float f = (float)useTicks / (this.pulltime * lightweightMod);
        f = (f*f + f*2.0F) / 3.0F;
        if(f > 1.0F){
            f = 1.0F;
        }

        return f;
    }
}

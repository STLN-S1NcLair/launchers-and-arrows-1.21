package net.stln.launchersandarrows.item.bow;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.entity.RicochetProjectile;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.item.component.ModifierComponent;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.util.AttributeEnum;
import net.stln.launchersandarrows.util.ModifierEnum;
import net.stln.launchersandarrows.util.TextUtil;
import net.stln.launchersandarrows.util.TooltipUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModifiableBowItem extends BowItem {
    protected int slotsize = 3;
    protected int pulltime = 40;

    public ModifiableBowItem(Properties properties) {
        super(properties);
    }

    public void setModifier(int slot, ItemStack bow, ItemStack modifier){
        if(slot < slotsize){
            ModifierComponent modifierComponent = bow.get(ComponentInit.MODIFIER_COMPONENT);
            if(modifierComponent != null){
                List<ItemStack> modifiers = new ArrayList<>(List.copyOf(modifierComponent.getModifiers()));
                modifiers.add(modifier);
                bow.set(ComponentInit.MODIFIER_COMPONENT, ModifierComponent.of(modifiers));
            }
            else {
                bow.set(ComponentInit.MODIFIER_COMPONENT, ModifierComponent.of(List.of(modifier)));
            }
        }
    }

    public ItemStack getModifier(int slot, ItemStack bow){
        ModifierComponent modifierComponent = bow.get(ComponentInit.MODIFIER_COMPONENT);
        if(modifierComponent != null){
            List<ItemStack> modifiers = modifierComponent.getModifiers();
            if(slot < modifiers.size()){
                return modifiers.get(slot);
            }
        }
        return null;
    }

    public List<ItemStack> getModifiers(ItemStack bow){
        ModifierComponent modifierComponent = bow.get(ComponentInit.MODIFIER_COMPONENT);
        if(modifierComponent != null){
            return modifierComponent.getModifiers();
        }
        return null;
    }

    // f: createArrowEntity
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        Projectile entity = super.createProjectile(level, shooter, weapon, ammo, isCrit);
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
        return entity;
    }

    protected ItemStack getProjectileTypeWithSelector(Player player, ItemStack stack){
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        Predicate<ItemStack> predicate = ((ProjectileWeaponItem)stack.getItem()).getSupportedHeldProjectiles();
        String selector = stack.get(ComponentInit.ARROW_SELECTOR_COMPONENT);
        if(!offHandStack.isEmpty() && predicate.test(offHandStack)){
            stack.set(ComponentInit.ARROW_SELECTOR_COMPONENT, offHandStack.getItem().getDescription().getString());
            return offHandStack;
        }
        else if(!mainHandStack.isEmpty() && predicate.test(mainHandStack)){
            stack.set(ComponentInit.ARROW_SELECTOR_COMPONENT, mainHandStack.getItem().getDescription().getString());
            return mainHandStack;
        }
        else if(selector != null && !selector.isEmpty()) {
            for(int i=0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack invStack = player.getInventory().getItem(i);
                if(selector.equals(invStack.getItem().getDescription().getString())) {
                    return invStack;
                }
            }
        }
        return player.getProjectile(stack);
    }

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
        if (Boolean.TRUE.equals(stack.get(ComponentInit.SELF_REPAIR_COMPONENT)) && entity.getRandom().nextFloat() < 0.005) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ModifiableBowItem modifiableBowItem = (ModifiableBowItem) stack.getItem();
        int slotsize = modifiableBowItem.getSlotsize();
        if(slotsize > 0){
            tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.modifier").withStyle(ChatFormatting.GRAY));
            List<ItemStack> modifiers = stack.get(ComponentInit.MODIFIER_COMPONENT).getModifiers();
            Integer[] attributeModifier = new Integer[13];
            Integer[] otherModifier = new Integer[6];
            for(int i = 0; i < modifiableBowItem.getSlotsize(); i++){
                if(i < modifiers.size() && modifiers.get(i) != null){
                    tooltipComponents.add(Component.literal("- ").withColor(0x808080)
                            .append(TextUtil.getIconComponent(modifiers.get(i).getItem())).append(" ")
                            .append(modifiers.get(i).getHoverName()).withColor(modifiers.get(i).getRarity().color().getColor()));

                    Item mod = modifiers.get(i).getItem();
                    for (int j = 0; j < 13; j++) {
                        if (attributeModifier[j] == null) {
                            attributeModifier[j] = AttributeModifierDictionary.getAttributeEffect(mod, j - 6);
                        } else if (AttributeModifierDictionary.getAttributeEffect(mod, j - 6) != null) {
                            attributeModifier[j] += AttributeModifierDictionary.getAttributeEffect(mod, j - 6);
                        }
                    }
                    for (int j = 0; j < 6; j++) {
                        if (otherModifier[j] == null) {
                            otherModifier[j] = ModifierDictionary.getEffect(mod, j);
                        } else if (ModifierDictionary.getEffect(mod, j) != null) {
                            otherModifier[j] += ModifierDictionary.getEffect(mod, j);
                        }
                    }
                }
                else {
                    tooltipComponents.add(Component.literal("- ").append(Component.translatable("tooltip.launchers_and_arrows.empty")).withColor(0x808080));
                }
            }
            ResourceLocation iconFont = ResourceLocation.fromNamespaceAndPath(LaunchersAndArrows.MOD_ID, "icons");
            TooltipUtil.getAttributeModifierTooltip(tooltipComponents, attributeModifier, iconFont);
            TooltipUtil.getOtherModifierTooltip(tooltipComponents, otherModifier, iconFont);
        }
        if(stack.has(ComponentInit.SELF_REPAIR_COMPONENT) && stack.get(ComponentInit.SELF_REPAIR_COMPONENT)){
            tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.self_repair").withColor(0x60FFC0));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}

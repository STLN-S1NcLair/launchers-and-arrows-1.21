package net.stln.launchersandarrows.item.bow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.entity.RicochetProjectile;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.component.ModifierComponent;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.util.ModifierEnum;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ModfiableBowItem extends BowItem {
    protected int slotsize = 3;
    protected int pulltime = 40;

    public ModfiableBowItem(Settings settings) {
        super(settings);
    }


    public void setModifier(int slot, ItemStack bow, ItemStack modifier) {
        if (slot < slotsize) {
            ModifierComponent modifierComponent = bow.get(ModComponentInit.MODIFIER_COMPONENT);
            if (modifierComponent != null) {
                List<ItemStack> modifiers = new java.util.ArrayList<>(List.copyOf(modifierComponent.getModifiers()));
                modifiers.add(modifier);
                bow.set(ModComponentInit.MODIFIER_COMPONENT, ModifierComponent.of(modifiers));
            } else {
                bow.set(ModComponentInit.MODIFIER_COMPONENT, ModifierComponent.of(List.of(modifier)));
            }
        }
    }

    public ItemStack getModifier(int slot, ItemStack bow) {
        ModifierComponent modifierComponent = bow.get(ModComponentInit.MODIFIER_COMPONENT);
        if (modifierComponent !=null) {
            List<ItemStack> modifiers = modifierComponent.getModifiers();
            if (slot < modifiers.size()) {
                return modifiers.get(slot);
            }
        }
        return null;
    }

    public List<ItemStack> getModifiers(ItemStack bow) {
        ModifierComponent modifierComponent = bow.get(ModComponentInit.MODIFIER_COMPONENT);
        if (modifierComponent !=null) {
            return modifierComponent.getModifiers();
        }
        return null;
    }

    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        ProjectileEntity entity = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
        if (entity instanceof PersistentProjectileEntity projectile) {
            for (int i = 0; i < slotsize; i++) {
                if (i < getModifiers(weaponStack).size()) {
                    ItemStack modifier = getModifier(i, weaponStack);
                    if (modifier != null) {
                        for (int j = 0; j < 13; j++) {
                            if (AttributeModifierDictionary.getDict().containsKey2(modifier.getItem(), j - 6)) {
                                ((AttributedProjectile) projectile).setAttribute(j - 6,
                                        AttributeModifierDictionary.getAttributeEffect(modifier.getItem(), j - 6) + ((AttributedProjectile) projectile).getAttribute(j - 6));
                            }
                        }
                        if (ModifierDictionary.getDict().containsKey2(modifier.getItem(), ModifierEnum.RICOCHET.get())) {
                            ((RicochetProjectile) projectile).setRicochet(ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.RICOCHET.get()) + ((RicochetProjectile) projectile).getRicochet());
                        }
                    }
                }
            }
            return projectile;
        }
        return entity;
    }

    protected ItemStack getProjectileTypeWithSelector(PlayerEntity playerEntity, ItemStack stack) {
        ItemStack mainHandStack = playerEntity.getMainHandStack();
        ItemStack offHandStack = playerEntity.getOffHandStack();
        Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
        String selector = stack.get(ModComponentInit.ARROW_SELECTOR_COMPONENT);
        if (!offHandStack.isEmpty() && predicate.test(offHandStack)) {
            stack.set(ModComponentInit.ARROW_SELECTOR_COMPONENT, offHandStack.getItem().getName().getString());
            return offHandStack;
        } else if (!mainHandStack.isEmpty() && predicate.test(mainHandStack)) {
            stack.set(ModComponentInit.ARROW_SELECTOR_COMPONENT, mainHandStack.getItem().getName().getString());
            return mainHandStack;
        } else if (selector != null && !selector.isEmpty()) {
            for (int i = 0; i < playerEntity.getInventory().size(); i++) {
                ItemStack invStack = playerEntity.getInventory().getStack(i);
                if (selector.equals(invStack.getItem().getName().getString())) {
                    return invStack;
                }
            }
        }
        return playerEntity.getProjectileType(stack);
    }

    @Override
    protected void shootAll(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
        speed = applySpeedModifier(shooter, stack, speed);
        divergence = applyPrecisionModifier(shooter, stack, divergence);
        super.shootAll(world, shooter, hand, stack, projectiles, speed, divergence, critical, target);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (stack.getComponents().contains(ModComponentInit.SELF_REPAIR_COMPONENT) && stack.getComponents().get(ModComponentInit.SELF_REPAIR_COMPONENT) && entity.getRandom().nextFloat() < 0.005) {
            stack.setDamage(stack.getDamage() - 1);
        }
    }

    protected float applySpeedModifier(LivingEntity shooter, ItemStack stack, float speed) {
        float sturdyPercentage = 0F;
        for (int i = 0; i < slotsize; i++) {
            if (i < getModifiers(stack).size()) {
                ItemStack modifier = getModifier(i, stack);
                if (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.RANGE.get()) != null) {
                    speed *= (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.RANGE.get()) + 100) / 100.0F;
                }
                if (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.STURDY.get()) != null) {
                    sturdyPercentage += (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.STURDY.get())) / 100.0F;
                }
            }
        }
        if (shooter.getRandom().nextFloat() < sturdyPercentage) {
            stack.setDamage(stack.getDamage() - 1);
        }
        return speed;
    }

    protected float applyPrecisionModifier(LivingEntity shooter, ItemStack stack, float divergence) {
        for (int i = 0; i < slotsize; i++) {
            if (i < getModifiers(stack).size()) {
                ItemStack modifier = getModifier(i, stack);
                if (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.PRECISION.get()) != null) {
                    divergence *= 1 - ((ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.PRECISION.get())) / 100.0F);
                }
            }
        }
        return divergence;
    }

    public int getSlotsize() {
        return slotsize;
    }

    public int getPulltime() {
        return pulltime;
    }


    public float getModifiedPullProgress(int useTicks, ItemStack stack) {
        float lightweightMod = 1F;
        for (int i = 0; i < slotsize; i++) {
            if (i < getModifiers(stack).size()) {
                ItemStack modifier = getModifier(i, stack);
                if (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.LIGHTWEIGHT.get()) != null) {
                    lightweightMod -= ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.LIGHTWEIGHT.get()) / 100.0F;
                }
            }
        }
        lightweightMod = lightweightMod < 0 ? 0 : lightweightMod;
        float f = (float)useTicks / (this.pulltime * lightweightMod);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}

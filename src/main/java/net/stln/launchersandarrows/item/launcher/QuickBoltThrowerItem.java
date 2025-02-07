package net.stln.launchersandarrows.item.launcher;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.item.BoltItem;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.ModItemTags;
import net.stln.launchersandarrows.item.bow.ModfiableBowItem;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.util.InventoryUtil;
import net.stln.launchersandarrows.util.ModifierEnum;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class QuickBoltThrowerItem extends BoltThrowerItem {

    public static final Predicate<ItemStack> BOLT_THROWER_PROJECTILES = (stack) -> stack.isIn(ModItemTags.BOXED_BOLTS);
    private static final CrossbowItem.LoadingSounds DEFAULT_LOADING_SOUNDS = new CrossbowItem.LoadingSounds(
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
    );

    public QuickBoltThrowerItem(Settings settings) {
        super(settings);
        pulltime = 60;
        maxCount = 40;
        maxChargeCount = 10;
        chargeDelay = 2;
        shootDelay = 1;
        slotsize = 4;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity playerEntity && !(playerEntity.getMainHandStack().equals(stack) || playerEntity.getOffHandStack().equals(stack))) {
            stack.set(ModComponentInit.CHARGING_COMPONENT, false);
        }
        if (entity instanceof PlayerEntity playerEntity && !stack.get(ModComponentInit.CHARGING_COMPONENT)
                && (playerEntity.getMainHandStack().equals(stack) || playerEntity.getOffHandStack().equals(stack))
                && stack.get(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT) != null
                && stack.get(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT) != null && !world.isClient) {
            int count = stack.get(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT);
            int leftCount = stack.get(ModComponentInit.BOLT_COUNT_COMPONENT);
            if (count > 0 && this.shootCooldown == 0) {
                if (leftCount > 0) {
                    ChargedProjectilesComponent chargedProjectilesComponent = (ChargedProjectilesComponent) stack.get(DataComponentTypes.CHARGED_PROJECTILES);
                    if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
                        ItemStack itemStack = chargedProjectilesComponent.getProjectiles().get(0);
                        if (!itemStack.isEmpty()) {
                            List<ItemStack> list = load(stack, itemStack, playerEntity);
                            if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
                                boolean critical = playerEntity.getRandom().nextFloat() > 0.6;
                                this.shootAll(serverWorld, playerEntity,
                                        playerEntity.getMainHandStack().equals(stack) ? Hand.MAIN_HAND : Hand.OFF_HAND,
                                        stack, list, 2.0F, 7.5F, critical, null);
                                this.shootCooldown = this.shootDelay - 1;
                            }
                            world.playSound(
                                    null,
                                    playerEntity.getX(),
                                    playerEntity.getY(),
                                    playerEntity.getZ(),
                                    SoundInit.BOLT_THROWER,
                                    SoundCategory.PLAYERS,
                                    1.5F,
                                    1.5F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                            );
                        }
                    }
                    stack.set(ModComponentInit.BOLT_COUNT_COMPONENT, leftCount - 1);
                } else {
                    world.playSound(
                            null,
                            playerEntity.getX(),
                            playerEntity.getY(),
                            playerEntity.getZ(),
                            SoundEvents.BLOCK_DISPENSER_FAIL,
                            SoundCategory.PLAYERS,
                            1.5F,
                            1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                    );
                }
                stack.set(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT, count - 1);
            } else if (shootCooldown > 0) {
                shootCooldown--;
            } else if (shootCooldown < 0) {
                shootCooldown = 0;
            }
            if (count == 0 && leftCount == 0) {
                stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
            }
        } else if (entity instanceof PlayerEntity playerEntity
                && !(playerEntity.getMainHandStack().equals(stack) || playerEntity.getOffHandStack().equals(stack))
                && !stack.get(ModComponentInit.CHARGING_COMPONENT)) {
            stack.set(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT, 0);
        }
    }
}

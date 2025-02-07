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
import net.minecraft.item.*;
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
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.item.BoltItem;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.ModItemTags;
import net.stln.launchersandarrows.item.ModifierItem;
import net.stln.launchersandarrows.item.bow.ModfiableBowItem;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.util.InventoryUtil;
import net.stln.launchersandarrows.util.ModifierEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class BoltThrowerItem extends ModfiableBowItem {

    protected int maxCount = 60;
    protected int maxChargeCount = 20;
    protected int chargeDelay = 2;
    protected int shootDelay = 2;

    protected int shootCooldown = 0;

    protected boolean played0 = false;
    protected boolean played1 = false;
    protected boolean played2 = false;

    public static final Predicate<ItemStack> BOLT_THROWER_PROJECTILES = (stack) -> stack.isIn(ModItemTags.BOXED_BOLTS);
    private static final CrossbowItem.LoadingSounds DEFAULT_LOADING_SOUNDS = new CrossbowItem.LoadingSounds(
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
    );

    public BoltThrowerItem(Settings settings) {
        super(settings);
        pulltime = 40;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOLT_THROWER_PROJECTILES;
    }

    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return BOLT_THROWER_PROJECTILES;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        this.played0 = false;
        this.played1 = false;
        this.played2 = false;
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.get(ModComponentInit.BOLT_COUNT_COMPONENT) > 0) {
            itemStack.set(ModComponentInit.CHARGING_COMPONENT, true);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        boolean bl = !user.getProjectileType(itemStack).isEmpty();
        if (!user.isInCreativeMode() && !bl) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        stack.set(ModComponentInit.CHARGING_COMPONENT, false);
        if (user instanceof PlayerEntity player) {
            if (stack.get(ModComponentInit.BOLT_COUNT_COMPONENT) == 0) {
                this.loadBolt(stack, player, remainingUseTicks);
            }
        }
    }

    private void loadBolt(ItemStack stack, PlayerEntity playerEntity, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack, playerEntity) - remainingUseTicks;
        float f = getModifiedPullProgress(i, stack);
        if (f >= 1.0F) {
            ItemStack itemStack = this.getProjectileTypeWithSelector(playerEntity, stack);
            ChargedProjectilesComponent component = ChargedProjectilesComponent.of(itemStack);
            int loadCount = 0;
            if (playerEntity.isCreative()) {
                loadCount = getModifiedMaxCount(stack);
            } else {
                for (int j = 0; j < Math.ceilDiv(getModifiedMaxCount(stack), 10); j++) {
                    ItemStack itemInInventory = InventoryUtil.getItemInInventory(playerEntity, itemStack.getItem());
                    if (itemInInventory != null && !itemInInventory.isEmpty()) {
                        itemInInventory.setCount(itemInInventory.getCount() - 1);
                        loadCount = Math.min(loadCount + 10, getModifiedMaxCount(stack));
                    }
                }
            }
            stack.set(DataComponentTypes.CHARGED_PROJECTILES, component);
            stack.set(ModComponentInit.BOLT_COUNT_COMPONENT, loadCount);
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int count = stack.get(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT);
        int leftCount = stack.get(ModComponentInit.BOLT_COUNT_COMPONENT);
        if (!world.isClient) {
            CrossbowItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            float f = getModifiedPullProgress(stack.getMaxUseTime(user) - remainingUseTicks, stack);
            if (f >= 0.2F && leftCount == 0 && !played0) {
                loadingSounds.start()
                        .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
                played0 = true;
            }

            if (f >= 0.5F && leftCount == 0 && !played1) {
                loadingSounds.mid()
                        .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
                played1 = true;
            }
            if (f == 1.0F && leftCount == 0 && !played2) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN,
                        SoundCategory.PLAYERS,
                        1.5F,
                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                );
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundEvents.BLOCK_PISTON_CONTRACT,
                        SoundCategory.PLAYERS,
                        0.5F,
                        2.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                );
                played2 = true;
            }
            if (count >= Math.min(getModifiedMaxChargeCount(stack), leftCount) && leftCount > 0 && !played2) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundEvents.BLOCK_SMITHING_TABLE_USE,
                        SoundCategory.PLAYERS,
                        1.0F,
                        2.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                );
                played2 = true;
            }
        }
        int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
        if (leftCount > 0 && stack.get(ModComponentInit.CHARGING_COMPONENT) && remainingUseTicks % chargeDelay == 0) {
            if (count < Math.min(getModifiedMaxChargeCount(stack), leftCount)) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundInit.RELOAD,
                        SoundCategory.PLAYERS,
                        0.5F,
                        world.getRandom().nextFloat() * 0.2F + 0.9F
                );
            }
            stack.set(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT, Math.min(count + 1, Math.min(getModifiedMaxChargeCount(stack), leftCount)));
        }
        float f = getModifiedPullProgress(i, stack);
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public float getModifiedPullProgress(int useTicks, ItemStack stack) {
        if (stack.get(ModComponentInit.BOLT_COUNT_COMPONENT) == 0) {
            return super.getModifiedPullProgress(useTicks, stack);
        }
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
        float f = (float)useTicks / (getModifiedMaxChargeCount(stack) * chargeDelay * lightweightMod);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
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
                                        stack, list, 2.0F, 3.0F, critical, null);
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
                                    1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
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

    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        BoltItem var10000;
        if (BOLT_THROWER_PROJECTILES.test(projectileStack)) {
            var10000 = (BoltItem) projectileStack.getItem();
        } else {
            var10000 = (BoltItem) ItemInit.BOXED_BOLTS;
        }

        BoltItem arrowItem2 = var10000;
        PersistentProjectileEntity persistentProjectileEntity = arrowItem2.createArrow(world, projectileStack, shooter, weaponStack);
        if (critical) {
            persistentProjectileEntity.setCritical(true);
        }
        persistentProjectileEntity.setDamage(0.5F);
        persistentProjectileEntity.setPos(shooter.getX(), shooter.getEyeY() - 0.2, shooter.getZ());
        for (int i = 0; i < slotsize; i++) {
            if (i < getModifiers(weaponStack).size()) {
                ItemStack modifier = getModifier(i, weaponStack);
                if (modifier != null) {
                    for (int j = 0; j < 13; j++) {
                        if (AttributeModifierDictionary.getDict().containsKey2(modifier.getItem(), j - 6)) {
                            ((AttributedProjectile) persistentProjectileEntity).setAttribute(j - 6,
                                    AttributeModifierDictionary.getAttributeEffect(modifier.getItem(), j - 6) + ((AttributedProjectile) persistentProjectileEntity).getAttribute(j - 6));
                        }
                    }
                }
            }
        }
        return persistentProjectileEntity;
    }



    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    CrossbowItem.LoadingSounds getLoadingSounds(ItemStack stack) {
        return (CrossbowItem.LoadingSounds) EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.CROSSBOW_CHARGING_SOUNDS)
                .orElse(DEFAULT_LOADING_SOUNDS);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            ItemStack itemStack = (ItemStack)chargedProjectilesComponent.getProjectiles().get(0);
            tooltip.add(Text.translatable("item.minecraft.crossbow.projectile").append(ScreenTexts.SPACE).append(itemStack.toHoverableText()));
            if (type.isAdvanced() && itemStack.isOf(Items.FIREWORK_ROCKET)) {
                List<Text> list = Lists.<Text>newArrayList();
                Items.FIREWORK_ROCKET.appendTooltip(itemStack, context, list, type);
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, Text.literal("  ").append((Text)list.get(i)).formatted(Formatting.GRAY));
                    }

                    tooltip.addAll(list);
                }
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return stack.isOf(this);
    }

    @Override
    public int getRange() {
        return 8;
    }

    public int getMaxChargeCount() {
        return maxChargeCount;
    }

    public int getTickUntilMaxCharge(ItemStack stack) {
        if (stack.get(ModComponentInit.BOLT_COUNT_COMPONENT) == 0) {
            return this.pulltime;
        } else {
            return (Math.min(getModifiedMaxChargeCount(stack), stack.get(ModComponentInit.BOLT_COUNT_COMPONENT))
                    - stack.get(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT)) * this.chargeDelay;
        }
    }

    protected int getModifiedMaxChargeCount(ItemStack stack) {
        float capacityModifier = getCapacityModifier(stack);
        return (int) (capacityModifier * this.maxChargeCount);
    }

    protected int getModifiedMaxCount(ItemStack stack) {
        float capacityModifier = getCapacityModifier(stack);
        return (int) (capacityModifier * this.maxCount);
    }

    protected float getCapacityModifier(ItemStack stack) {
        float capacityModifier = 1F;
        for (int i = 0; i < slotsize; i++) {
            if (i < getModifiers(stack).size()) {
                ItemStack modifier = getModifier(i, stack);
                if (ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.CAPACITY.get()) != null) {
                    capacityModifier += ModifierDictionary.getEffect(modifier.getItem(), ModifierEnum.CAPACITY.get()) / 100F;
                }
            }
        }
        return capacityModifier;
    }

    protected static record LoadingSounds(Optional<RegistryEntry<SoundEvent>> start, Optional<RegistryEntry<SoundEvent>> mid, Optional<RegistryEntry<SoundEvent>> end) {
        public static final Codec<CrossbowItem.LoadingSounds> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                SoundEvent.ENTRY_CODEC.optionalFieldOf("start").forGetter(CrossbowItem.LoadingSounds::start),
                                SoundEvent.ENTRY_CODEC.optionalFieldOf("mid").forGetter(CrossbowItem.LoadingSounds::mid),
                                SoundEvent.ENTRY_CODEC.optionalFieldOf("end").forGetter(CrossbowItem.LoadingSounds::end)
                        )
                        .apply(instance, CrossbowItem.LoadingSounds::new)
        );
    }
}

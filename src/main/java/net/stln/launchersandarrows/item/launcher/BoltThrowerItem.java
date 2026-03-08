package net.stln.launchersandarrows.item.launcher;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.item.BoltItem;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.ItemTagKeys;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.util.InventoryUtil;
import net.stln.launchersandarrows.util.ModifierEnum;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


public class BoltThrowerItem extends ModifiableBowItem {
    protected int maxCount = 60;
    protected int maxChargeCount = 20;
    protected int chargeDelay = 2;
    protected int shootDelay = 2;

    protected int shootCooldown = 0;

    protected boolean played0 = false;
    protected boolean played1 = false;
    protected boolean played2 = false;

    public static final Predicate<ItemStack> BOLT_THROWER_HELD_PROJECTILES = (stack) -> stack.is(ItemTagKeys.BOXED_BOLTS);

    private static final CrossbowItem.ChargingSounds DEFAULT_CHARGING_SOUNDS = new CrossbowItem.ChargingSounds(
            Optional.of(SoundEvents.CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.CROSSBOW_LOADING_END)
    );

    public BoltThrowerItem(Properties properties) {
        super(properties);
        pulltime = 40;
    }

    // f: getHeldProjectiles
    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return BOLT_THROWER_HELD_PROJECTILES;
    }

    // f: getProjectiles
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return BOLT_THROWER_HELD_PROJECTILES;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        this.played0 = false;
        this.played1 = false;
        this.played2 = false;
        ItemStack itemStack = player.getItemInHand(hand);
        if(itemStack.get(ComponentInit.BOLT_COUNT_COMPONENT) > 0){
            itemStack.set(ComponentInit.CHARGING_COMPONENT, true);
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        if(!player.isCreative() && player.getProjectile(itemStack).isEmpty()){
            return InteractionResultHolder.fail(itemStack);
        }
        else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
    }

    // f: onStoppedUsing
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        stack.set(ComponentInit.CHARGING_COMPONENT, false);
        if(entityLiving instanceof Player player){
            if(stack.has(ComponentInit.BOLT_COUNT_COMPONENT) && stack.get(ComponentInit.BOLT_COUNT_COMPONENT) == 0){
                int i = this.getUseDuration(stack, player) - timeLeft;
                float f = getModifiedPullProgress(i, stack);
                if(f >= 1.0F){
                    ItemStack itemStack = this.getProjectileTypeWithSelector(player, stack);
                    ChargedProjectiles component = ChargedProjectiles.of(itemStack);
                    int loadCount = 0;
                    if(player.isCreative()){
                        loadCount = getModifiedMaxCount(stack);
                    }
                    else {
                        for(int j = 0; j < Math.ceilDiv(getModifiedMaxCount(stack), 10); j++){
                            ItemStack itemInInventory = InventoryUtil.getItemInInventory(player, itemStack.getItem());
                            if(itemInInventory != null && !itemInInventory.isEmpty()){
                                itemInInventory.setCount(itemInInventory.getCount() - 1);
                                loadCount = Math.min(loadCount + 10, getModifiedMaxCount(stack));
                            }
                        }
                    }
                    stack.set(DataComponents.CHARGED_PROJECTILES, component);
                    stack.set(ComponentInit.BOLT_COUNT_COMPONENT, loadCount);
                }
            }
        }
    }

    // f: usageTick
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        int chargedBoltCount = stack.get(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT);
        int boltCount = stack.get(ComponentInit.BOLT_COUNT_COMPONENT);
        if(!level.isClientSide()){
            CrossbowItem.ChargingSounds chargingSounds = this.getChargingSounds(stack);
            float f = getModifiedPullProgress(stack.getUseDuration(livingEntity) - count, stack);
            if(f >= 0.2F && boltCount == 0 && !played0){
                chargingSounds.start().ifPresent(sound -> level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                ));
                played0 = true;
            }
            if(f >= 0.5F && boltCount == 0 && !played1){
                chargingSounds.mid().ifPresent(sound -> level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                ));
                played1 = true;
            }
            if(f == 1.0F && boltCount == 0 && !played2){
                float h = level.getRandom().nextFloat() * 0.4F + 1.2F;
                level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEvents.IRON_TRAPDOOR_OPEN,
                        SoundSource.PLAYERS,
                        1.5F,
                        1.0F / h + 0.5F
                );
                level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEvents.IRON_TRAPDOOR_OPEN,
                        SoundSource.PLAYERS,
                        1.5F,
                        2.0F / h + 0.5F
                );
                played2 = true;
            }
            if(chargedBoltCount > Math.min(getModifiedMaxChargeCount(stack), boltCount) && boltCount > 0 && !played2){
                level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEvents.SMITHING_TABLE_USE,
                        SoundSource.PLAYERS,
                        1.5F,
                        2.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                );
                played2 = true;
            }
        }
        int i = this.getUseDuration(stack, livingEntity) - count;
        if(boltCount > 0 && stack.get(ComponentInit.CHARGING_COMPONENT) && count % chargeDelay == 0){
            if(chargedBoltCount < Math.min(getModifiedMaxChargeCount(stack), boltCount)){
                level.playSound(null,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundInit.RELOAD.get(),
                        SoundSource.PLAYERS,
                        1.5F,
                        level.getRandom().nextFloat() * 0.2F + 0.9F
                );
            }
            stack.set(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT, Math.min(chargedBoltCount + 1, Math.min(getModifiedMaxChargeCount(stack), boltCount)));
        }
        float f = getModifiedPullProgress(i, stack); //これ要る?
        super.onUseTick(level, livingEntity, stack, count);
    }

    @Override
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
        float f = (float)useTicks / (getModifiedMaxChargeCount(stack) * chargeDelay * lightweightMod);
        f = (f*f + f*2.0F) / 3.0F;
        if(f > 1.0F){
            f = 1.0F;
        }

        return f;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(entity instanceof Player player){
            boolean b = player.getMainHandItem().equals(stack);
            boolean b1 = b || player.getOffhandItem().equals(stack);

            if(!b1){
                stack.set(ComponentInit.CHARGING_COMPONENT, false);
            }

            if(!stack.get(ComponentInit.CHARGING_COMPONENT)){
                if(b1 && stack.get(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT) != null && !level.isClientSide()){
                    int chargedBoltCount = stack.get(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT);
                    int boltCount = stack.get(ComponentInit.BOLT_COUNT_COMPONENT);
                    if(chargedBoltCount > 0 && this.shootCooldown == 0){
                        if(boltCount > 0){
                            ChargedProjectiles chargedProjectiles = (ChargedProjectiles) stack.get(DataComponents.CHARGED_PROJECTILES);
                            if(chargedProjectiles != null && !chargedProjectiles.isEmpty()){
                                ItemStack itemStack = chargedProjectiles.getItems().get(0);
                                if(!itemStack.isEmpty()){
                                    List<ItemStack> list = draw(stack, itemStack, player);
                                    if(level instanceof ServerLevel serverLevel && !list.isEmpty()){
                                        boolean isCrit = player.getRandom().nextFloat() > 0.6;
                                        this.shoot(serverLevel, player,
                                                b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
                                                stack, list, 2.0F, 3.0F, isCrit, null);
                                    }
                                    level.playSound(null,
                                            player.getX(), player.getY(), player.getZ(),
                                            SoundInit.BOLT_THROWER.get(),
                                            SoundSource.PLAYERS,
                                            1.5F,
                                            1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                                    );
                                }
                            }
                            stack.set(ComponentInit.BOLT_COUNT_COMPONENT, boltCount - 1);
                        }
                        else {
                            level.playSound(null,
                                    player.getX(), player.getY(), player.getZ(),
                                    SoundEvents.DISPENSER_FAIL,
                                    SoundSource.PLAYERS,
                                    1.5F,
                                    1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
                            );
                        }
                        stack.set(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT, chargedBoltCount - 1);
                    }
                    else if(shootCooldown > 0){
                        shootCooldown--;
                    }
                    else if(shootCooldown < 0){
                        shootCooldown = 0;
                    }
                    if(chargedBoltCount == 0 && boltCount == 0){
                        stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
                    }
                }
                else if(!b1){
                    stack.set(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT, 0);
                }
            }
        }
    }

    // f: createArrowEntity
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        BoltItem var10000;
        if(BOLT_THROWER_HELD_PROJECTILES.test(ammo)){
            var10000 = (BoltItem) ammo.getItem();
        }
        else {
            var10000 = (BoltItem) ItemInit.BOXED_BOLTS.get();
        }

        BoltItem arrowItem2 = var10000;
        AbstractArrow arrow = arrowItem2.createArrow(level, ammo, shooter, weapon);
        if(isCrit){
            arrow.setCritArrow(true);
        }
        arrow.setBaseDamage(0.5F);
        arrow.setPos(shooter.getX(), shooter.getEyeY() - 0.2, shooter.getZ());
        /*
        for(int i = 0; i < slotsize; i++){
            if(i < getModifiers(weapon).size()){
                ItemStack modifier = getModifier(i, weapon);
                if(modifier != null){
                    for (int j = 0; j < 13; j++) {
                        if (AttributeModifierDictionary.getDict().containsKey2(modifier.getItem(), j - 6)) {
                            ((AttributedProjectile) arrow).setAttribute(j - 6,
                                    AttributeModifierDictionary.getAttributeEffect(modifier.getItem(), j - 6) + ((AttributedProjectile) arrow).getAttribute(j - 6));
                        }
                    }
                }
            }
        }
        */
        return arrow;
    }

    // f: getUseAction
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    // f: getLoadingSounds
    CrossbowItem.ChargingSounds getChargingSounds(ItemStack stack) {
        return (CrossbowItem.ChargingSounds) EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS)
                .orElse(DEFAULT_CHARGING_SOUNDS);
    }

    // f: appendToolTip
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ChargedProjectiles chargedprojectiles = (ChargedProjectiles)stack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            ItemStack itemstack = (ItemStack)chargedprojectiles.getItems().get(0);
            tooltipComponents.add(Component.translatable("item.minecraft.crossbow.projectile").append(CommonComponents.SPACE).append(itemstack.getDisplayName()));
            if (tooltipFlag.isAdvanced() && itemstack.is(Items.FIREWORK_ROCKET)) {
                List<Component> list = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemstack, context, list, tooltipFlag);
                if (!list.isEmpty()) {
                    for(int i = 0; i < list.size(); ++i) {
                        list.set(i, Component.literal("  ").append((Component)list.get(i)).withStyle(ChatFormatting.GRAY));
                    }

                    tooltipComponents.addAll(list);
                }
            }
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

    }

    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

    public int getMaxChargeCount() {
        return maxChargeCount;
    }

    public int getTickUntilMaxCharge(ItemStack stack) {
        if (stack.has(ComponentInit.BOLT_COUNT_COMPONENT) && stack.get(ComponentInit.BOLT_COUNT_COMPONENT) == 0) {
            return this.pulltime;
        } else {
            return (Math.min(getModifiedMaxChargeCount(stack), stack.get(ComponentInit.BOLT_COUNT_COMPONENT))
                    - stack.get(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT)) * this.chargeDelay;
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

    @Override
    public ItemStack getDefaultCreativeAmmo(@Nullable Player player, ItemStack projectileWeaponItem) {
        return ItemInit.BOXED_BOLTS.get().getDefaultInstance();
    }

    public static record ChargingSounds(Optional<Holder<SoundEvent>> start, Optional<Holder<SoundEvent>> mid, Optional<Holder<SoundEvent>> end) {
        public static final Codec<CrossbowItem.ChargingSounds> CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                            SoundEvent.CODEC.optionalFieldOf("start").forGetter(CrossbowItem.ChargingSounds::start),
                            SoundEvent.CODEC.optionalFieldOf("mid").forGetter(CrossbowItem.ChargingSounds::mid),
                            SoundEvent.CODEC.optionalFieldOf("end").forGetter(CrossbowItem.ChargingSounds::end)
                    ).apply(instance, CrossbowItem.ChargingSounds::new)
        );
    }
}

package net.stln.launchersandarrows.item.bow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.stln.launchersandarrows.item.FovModifierItem;
import net.stln.launchersandarrows.item.component.ChargeComponent;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.sound.SoundInit;

import java.util.ArrayList;
import java.util.List;

public class MechanicalBowItem extends ModfiableBowItem implements FovModifierItem {

    float fov = 1.0f;

    int chargeSlot = 3;

    public MechanicalBowItem(Settings settings) {
        super(settings);
        pulltime = 15;
        slotsize = 3;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.isSneaking()) {
            world.playSound((Entity) user, user.getBlockPos(), SoundEvents.ITEM_CROSSBOW_LOADING_END.value(), SoundCategory.PLAYERS, 1f, 1.5f);
            world.playSound((Entity) user, user.getBlockPos(), SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE.value(), SoundCategory.PLAYERS, 1f, 1.0f);
        } else {
            world.playSound((Entity) user, user.getBlockPos(), SoundInit.MECHANICAL_BOW_CHARGE, SoundCategory.PLAYERS, 0.5f, 0.8f);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            if (playerEntity.isSneaking() && stack.getComponents().get(ModComponentInit.CHARGE_COMPONENT).getCharges().getLast() == 0) {
                ChargeComponent component = stack.getComponents().get(ModComponentInit.CHARGE_COMPONENT);
                List<Double> charges = component.getCharges();
                List<Double> newCharges = new ArrayList<>(List.of());
                newCharges.addAll(charges);
                int index = 0;
                for (int j = 0; j < charges.size(); j++) {
                    if (charges.get(j) == 0) {
                        index = j;
                        break;
                    }
                }
                float f = getModifiedPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks, stack);
                newCharges.set(index, (double) f);
                stack.set(ModComponentInit.CHARGE_COMPONENT, ChargeComponent.of(newCharges));
                world.playSound(
                        null,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundInit.MECHANICAL_BOW_LOAD,
                        SoundCategory.PLAYERS,
                        1.5F,
                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                );
                world.playSound(
                        null,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundInit.BOW_RELEASE,
                        SoundCategory.PLAYERS,
                        1.5F,
                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                );
            } else {
                ItemStack itemStack = this.getProjectileTypeWithSelector(playerEntity, stack);
                if (!itemStack.isEmpty()) {
                    int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                    float f = getModifiedPullProgress(i, stack);
                    ChargeComponent component = stack.getComponents().get(ModComponentInit.CHARGE_COMPONENT);
                    List<Double> charges = component.getCharges();
                    if (((double) f > 0.5 && i > 2)) {
                        generateArrow(stack, world, playerEntity, itemStack, f);
                    } else if (stack.getComponents().get(ModComponentInit.CHARGE_COMPONENT).getCharges().getFirst() != 0) {
                        int index = charges.size() - 1;
                        for (int j = 0; j < charges.size(); j++) {
                            if (charges.get(j) == 0) {
                                index = j - 1;
                                break;
                            }
                        }
                        double charge = charges.get(index);
                        List<Double> newCharges = new ArrayList<>(List.of());
                        newCharges.addAll(charges);
                        newCharges.set(index, 0.0);
                        stack.set(ModComponentInit.CHARGE_COMPONENT, ChargeComponent.of(newCharges));
                        generateArrow(stack, world, playerEntity, itemStack, (float) charge);
                        world.playSound(
                                null,
                                playerEntity.getX(),
                                playerEntity.getY(),
                                playerEntity.getZ(),
                                SoundInit.MECHANICAL_BOW_RELEASE,
                                SoundCategory.PLAYERS,
                                1.5F,
                                1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                        );
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        ChargeComponent component = stack.getComponents().get(ModComponentInit.CHARGE_COMPONENT);
        List<Double> charges = component.getCharges();
        if (!selected) {
            int index = 0;
            for (int i = 0; i < charges.size(); i++) {
                if (charges.get(i) != 0) {
                    index = i;
                }
            }
            stack.set(ModComponentInit.CHARGE_COMPONENT, ChargeComponent.of(List.of(charges.get(index), 0.0, 0.0)));
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        if (user instanceof PlayerEntity playerEntity && playerEntity.isSneaking()) {
            stack.set(ModComponentInit.CHARGING_COMPONENT, true);
        }
    }

    private void generateArrow(ItemStack stack, World world, PlayerEntity playerEntity, ItemStack itemStack, float f) {
        List<ItemStack> list = load(stack, itemStack, playerEntity);
        if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
            this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, f * 3.5F, 1.0F, f == 1.0F, null);
        }

        world.playSound(
                null,
                playerEntity.getX(),
                playerEntity.getY(),
                playerEntity.getZ(),
                SoundInit.MECHANICAL_BOW_LOAD,
                SoundCategory.PLAYERS,
                1.5F,
                1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
        );

        world.playSound(
                null,
                playerEntity.getX(),
                playerEntity.getY(),
                playerEntity.getZ(),
                SoundInit.BOW_RELEASE,
                SoundCategory.PLAYERS,
                1.5F,
                1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
        );
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public float getFov() {
        return this.fov;
    }

    @Override
    public void resetFov() {
        this.fov = 1.0F;
    }
}

package net.stln.launchersandarrows.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;
import net.stln.launchersandarrows.item.component.ComponentInit;

import java.util.List;

public class SelfErosionPlateItem extends Item {
    public SelfErosionPlateItem(Properties properties) {
        super(properties);
    }

    protected Class<?> targetItemClass;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        if(usedHand == InteractionHand.MAIN_HAND
                && getCorrectTarget(offHandStack.getItem()) != null
                && player.isShiftKeyDown()
                && !Boolean.TRUE.equals(offHandStack.get(ComponentInit.SELF_REPAIR_COMPONENT.get()))){
            offHandStack.set(ComponentInit.SELF_REPAIR_COMPONENT.get(), true);
            mainHandStack.shrink(1);

            float h = 1.0F / (player.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.DISPENSER_FAIL, player.getSoundSource(), 1.0F, h);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_DOOR_OPEN, player.getSoundSource(), 1.0F, h);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_DIAMOND, player.getSoundSource(), 1.0F, h);
            return InteractionResultHolder.consume(player.getMainHandItem());
        }
        return super.use(level, player, usedHand);
    }

    protected Item getCorrectTarget(Item item){
        if (item instanceof ModifiableBowItem) {
            return item;
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (!Screen.hasShiftDown()) {
            // tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.shift").withColor(0x808080));
        } else {
            // tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.self_erosion_plate").withColor(0x408070));
            tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.self_erosion_plate_2").withColor(0x408070));
        }
    }
}

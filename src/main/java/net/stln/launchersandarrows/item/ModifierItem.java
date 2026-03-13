package net.stln.launchersandarrows.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.util.AttributeEnum;
import net.stln.launchersandarrows.util.ModifierEnum;
import net.stln.launchersandarrows.util.TooltipUtil;

import java.util.List;

public abstract class ModifierItem extends Item {
    public ModifierItem(Properties properties) {
        super(properties);
    }

    protected Class<?> targetItemClass;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        if(usedHand == InteractionHand.MAIN_HAND && getCorrectTarget(offHandStack.getItem()) != null & player.isShiftKeyDown()){
            ModifiableBowItem bow = (ModifiableBowItem)offHandStack.getItem();
            for(int i = 0; i < bow.getSlotsize(); i++){
                if(bow.getModifier(i, offHandStack) == null){
                    bow.setModifier(i, offHandStack, mainHandStack.copy());
                    mainHandStack.setCount(mainHandStack.getCount() -1);

                    float h = 1.0F / (player.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.DISPENSER_FAIL, player.getSoundSource(), 1.0F, h);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_DOOR_OPEN, player.getSoundSource(), 1.0F, h);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_DIAMOND, player.getSoundSource(), 1.0F, h);

                    return InteractionResultHolder.consume(player.getMainHandItem());
                }
            }
        }
        return super.use(level, player, usedHand);
    }

    protected abstract Item getCorrectTarget(Item item);

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Integer[] attributeModifiers = new Integer[13];
        Integer[] otherModifiers = new Integer[6];
        Item modifier = stack.getItem();
        for (int i = 0; i < 13; i++) {
            attributeModifiers[i] = AttributeModifierDictionary.getAttributeEffect(modifier, i - 6);
        }
        for (int i = 0; i < 6; i++) {
            otherModifiers[i] = ModifierDictionary.getEffect(modifier, i);
        }
        // 説明
        if (AttributeModifierDictionary.getDict().containsKey1(modifier) || ModifierDictionary.getDict().containsKey1(modifier)) {
            if (!Screen.hasShiftDown()) {
                // tooltipComponents.add(Component.empty());
                tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.shift").withColor(0x808080));
            } else {
                // tooltipComponents.add(Component.empty());
                if (stack.getItem() instanceof BoltThrowerModifierItem) {
                    tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.bolt_thrower_modifier").withColor(0x406080));
                    tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.bolt_thrower_modifier_2").withColor(0x406080));
                } else {
                    tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.bow_modifier").withColor(0x406080));
                    tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.bow_modifier_2").withColor(0x406080));
                }
            }
            // tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.attribute_modifiers").withColor(0xC0C0C0)); //装備したとき…
        }
        ResourceLocation iconFont = ResourceLocation.fromNamespaceAndPath(LaunchersAndArrows.MOD_ID, "icons");
        TooltipUtil.getAttributeModifierTooltip(tooltipComponents, attributeModifiers, iconFont);
        TooltipUtil.getOtherModifierTooltip(tooltipComponents, otherModifiers, iconFont);
    }
}

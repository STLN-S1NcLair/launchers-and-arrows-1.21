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
        getAttributeModifierTooltip(tooltipComponents, attributeModifiers, iconFont);
        getOtherModifierTooltip(tooltipComponents, otherModifiers, iconFont);
    }

    private static void getAttributeModifierTooltip(List<Component> tooltipComponents, Integer[] attributeModifiers, ResourceLocation iconFont) {
        if (attributeModifiers[AttributeEnum.FLAME.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0001").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.FLAME.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLAME.get() + 6], 0xFFC080))));
        }
        if (attributeModifiers[AttributeEnum.FLAME_RATIO.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0001").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.FLAME_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLAME_RATIO.get() + 6], 0xFFC080))));
        }

        if (attributeModifiers[AttributeEnum.FROST.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0002").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.FROST.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.FROST.get() + 6], 0x80FFFF))));
        }
        if (attributeModifiers[AttributeEnum.FROST_RATIO.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0002").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.FROST_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.FROST_RATIO.get() + 6], 0x80FFFF))));
        }

        if (attributeModifiers[AttributeEnum.LIGHTNING.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0003").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.LIGHTNING.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.LIGHTNING.get() + 6], 0x8080FF))));
        }
        if (attributeModifiers[AttributeEnum.LIGHTNING_RATIO.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0003").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.LIGHTNING_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.LIGHTNING_RATIO.get() + 6], 0x8080FF))));
        }

        if (attributeModifiers[AttributeEnum.ACID.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0004").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.ACID.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.ACID.get() + 6], 0xC0FF80))));
        }
        if (attributeModifiers[AttributeEnum.ACID_RATIO.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0004").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.ACID_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.ACID_RATIO.get() + 6], 0xC0FF80))));
        }

        if (attributeModifiers[AttributeEnum.FLOOD.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0005").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.FLOOD.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLOOD.get() + 6], 0x80C0FF))));
        }
        if (attributeModifiers[AttributeEnum.FLOOD_RATIO.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0005").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.FLOOD_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLOOD_RATIO.get() + 6], 0x80C0FF))));
        }

        if (attributeModifiers[AttributeEnum.ECHO.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0006").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.ECHO.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.ECHO.get() + 6], 0x008080))));
        }

        if (attributeModifiers[AttributeEnum.ECHO_RATIO.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0006").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.ECHO_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.ECHO_RATIO.get() + 6], 0x008080))));
        }
        if (attributeModifiers[AttributeEnum.INJURY.get() + 6] != null) {
            tooltipComponents.add(Component.literal("\u0007").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(attributeModifiers[AttributeEnum.INJURY.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.INJURY.get() + 6], 0xFFFFFF))));
        }
    }

    private static void getOtherModifierTooltip(List<Component> tooltip, Integer[] modifiers, ResourceLocation iconFont) {
        if (modifiers[ModifierEnum.RANGE.get()] != null) {
            tooltip.add(Component.literal("\u0008").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(modifiers[ModifierEnum.RANGE.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.RANGE.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.STURDY.get()] != null) {
            tooltip.add(Component.literal("\u0009").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(modifiers[ModifierEnum.STURDY.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.STURDY.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.LIGHTWEIGHT.get()] != null) {
            tooltip.add(Component.literal("\u000b").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(modifiers[ModifierEnum.LIGHTWEIGHT.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.LIGHTWEIGHT.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.CAPACITY.get()] != null) {
            tooltip.add(Component.literal("\u000c").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(modifiers[ModifierEnum.CAPACITY.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.CAPACITY.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.RICOCHET.get()] != null) {
            tooltip.add(Component.literal("\u000e").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(modifiers[ModifierEnum.RICOCHET.get()])).withColor(getColorWithSign(modifiers[ModifierEnum.RICOCHET.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.PRECISION.get()] != null) {
            tooltip.add(Component.literal("\u000f").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(getSign(modifiers[ModifierEnum.PRECISION.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.PRECISION.get()], 0xFFFFFF))));
        }
    }

    private static String getSign(int i) {
        if (i >= 0) {
            return "+" + i;
        } else {
            return String.valueOf(i);
        }
    }

    private static int getColorWithSign(int i, int color) {
        int R = color >> 16;
        int G = color >> 8 & 0x00FF;
        int B = color & 0x0000FF;

        if (i < 0) {
            R /= 2;
            G /= 2;
            B /= 2;
        }
        return (R << 16) + (G << 8) + B;
    }
}

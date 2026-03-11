package net.stln.launchersandarrows.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TooltipUtil {
    public static void getAttributeModifierTooltip(List<Component> tooltipComponents, Integer[] attributeModifiers, ResourceLocation iconFont) {
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

    public static void getOtherModifierTooltip(List<Component> tooltip, Integer[] modifiers, ResourceLocation iconFont) {
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

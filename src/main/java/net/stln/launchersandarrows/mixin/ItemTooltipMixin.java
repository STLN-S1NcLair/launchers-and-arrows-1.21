package net.stln.launchersandarrows.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.BoltThrowerModifierItem;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.bow.ModfiableBowItem;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.item.util.AttributeEffectsDictionary;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.util.AttributeEnum;
import net.stln.launchersandarrows.util.ModifierEnum;
import net.stln.launchersandarrows.util.TextUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(Item.class)
public class ItemTooltipMixin {

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void addTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
        Identifier iconFont = Identifier.of(LaunchersAndArrows.MOD_ID, "icons");
        Integer[] attributes = new Integer[7];
        Item item = stack.getItem();
        for (int i = 0; i < 7; i++) {
            attributes[i] = AttributeEffectsDictionary.getAttributeEffect(item, i);
        }
        if (AttributeEffectsDictionary.getDict().containsKey1(item)) {
            tooltip.add(Text.empty());
            tooltip.add(Text.translatable("tooltip.launchers_and_arrows.attribute_effects").append(":").withColor(0xC0C0C0));
        }
        if (attributes[AttributeEnum.FLAME.get()] != null) {
            tooltip.add(Text.literal("\u0001").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(String.valueOf(attributes[AttributeEnum.FLAME.get()])).withColor(0xFFC080)));
        }
        if (attributes[AttributeEnum.FROST.get()] != null) {
            tooltip.add(Text.literal("\u0002").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(String.valueOf(attributes[AttributeEnum.FROST.get()])).withColor(0x80FFFF)));
        }
        if (attributes[AttributeEnum.LIGHTNING.get()] != null) {
            tooltip.add(Text.literal("\u0003").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(String.valueOf(attributes[AttributeEnum.LIGHTNING.get()])).withColor(0x8080FF)));
        }
        if (attributes[AttributeEnum.ACID.get()] != null) {
            tooltip.add(Text.literal("\u0004").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(String.valueOf(attributes[AttributeEnum.ACID.get()])).withColor(0xC0FF80)));
        }
        if (attributes[AttributeEnum.FLOOD.get()] != null) {
            tooltip.add(Text.literal("\u0005").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(String.valueOf(attributes[AttributeEnum.FLOOD.get()])).withColor(0x80C0FF)));
        }
        if (attributes[AttributeEnum.ECHO.get()] != null) {
            tooltip.add(Text.literal("\u0006").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(String.valueOf(attributes[AttributeEnum.ECHO.get()])).withColor(0x008080)));
        }
        if (attributes[AttributeEnum.INJURY.get()] != null) {
            tooltip.add(Text.literal("\u0007").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(String.valueOf(attributes[AttributeEnum.INJURY.get()])).withColor(0xFFFFFF)));
        }


        Integer[] attributeModifiers = new Integer[13];
        Integer[] otherModifiers = new Integer[6];
        Item modifier = stack.getItem();
        for (int i = 0; i < 13; i++) {
            attributeModifiers[i] = AttributeModifierDictionary.getAttributeEffect(modifier, i - 6);
        }
        for (int i = 0; i < 6; i++) {
            otherModifiers[i] = ModifierDictionary.getEffect(modifier, i);
        }
        if (AttributeModifierDictionary.getDict().containsKey1(modifier) || ModifierDictionary.getDict().containsKey1(modifier)) {
            if (!Screen.hasShiftDown()) {
                tooltip.add(Text.empty());
                tooltip.add(Text.translatable("tooltip.launchers_and_arrows.shift").withColor(0x808080));
            } else {
                tooltip.add(Text.empty());
                if (stack.getItem() instanceof BoltThrowerModifierItem) {
                    tooltip.add(Text.translatable("tooltip.launchers_and_arrows.bolt_thrower_modifier").withColor(0x406080));
                    tooltip.add(Text.translatable("tooltip.launchers_and_arrows.bolt_thrower_modifier_2").withColor(0x406080));
                } else {
                    tooltip.add(Text.translatable("tooltip.launchers_and_arrows.bow_modifier").withColor(0x406080));
                    tooltip.add(Text.translatable("tooltip.launchers_and_arrows.bow_modifier_2").withColor(0x406080));
                }
            }
            tooltip.add(Text.empty());
            tooltip.add(Text.translatable("tooltip.launchers_and_arrows.attribute_modifiers").append(":").withColor(0xC0C0C0));
        }
        getAttributeModifierTooltip(tooltip, attributeModifiers, iconFont);
        getOtherModifierTooltip(tooltip, otherModifiers, iconFont);

        if (stack.getItem() instanceof ModfiableBowItem modfiableBowItem && modfiableBowItem.getSlotsize() > 0) {
            tooltip.add(Text.empty());
            tooltip.add(Text.translatable("tooltip.launchers_and_arrows.modifier").append(":").withColor(0xC0C0C0));
            if (stack.get(ModComponentInit.MODIFIER_COMPONENT) != null) {
                List<ItemStack> modifiers = stack.get(ModComponentInit.MODIFIER_COMPONENT).getModifiers();
                Integer[] attributeModifier = new Integer[13];
                Integer[] otherModifier = new Integer[6];
                for (int i = 0; i < ((ModfiableBowItem) stack.getItem()).getSlotsize(); i++) {
                    if (i < modifiers.size() && modifiers.get(i) != null) {
                        tooltip.add(Text.literal("- ").withColor(0x808080)
                                        .append(TextUtil.getIconText(modifiers.get(i).getItem())).append(" ")
                                .append(modifiers.get(i).getName())
                                .withColor(modifiers.get(i).getRarity().getFormatting().getColorValue()));
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
                    } else {
                        tooltip.add(Text.literal("- ").append(Text.translatable("tooltip.launchers_and_arrows.empty")).withColor(0x808080));
                    }
                }
                getAttributeModifierTooltip(tooltip, attributeModifier, iconFont);
                getOtherModifierTooltip(tooltip, otherModifier, iconFont);
            }
        }

        if (stack.getItem() == ItemInit.GLITCH_ARROW) {
            tooltip.add(Text.empty());
            tooltip.add(Text.translatable("tooltip.launchers_and_arrows.warning").withColor(0xFF0000));
        }
    }

    @Unique
    private static void getAttributeModifierTooltip(List<Text> tooltip, Integer[] attributeModifiers, Identifier iconFont) {
        if (attributeModifiers[AttributeEnum.FLAME.get() + 6] != null) {
            tooltip.add(Text.literal("\u0001").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.FLAME.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLAME.get() + 6], 0xFFC080))));
        }
        if (attributeModifiers[AttributeEnum.FLAME_RATIO.get() + 6] != null) {
            tooltip.add(Text.literal("\u0001").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.FLAME_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLAME_RATIO.get() + 6], 0xFFC080))));
        }

        if (attributeModifiers[AttributeEnum.FROST.get() + 6] != null) {
            tooltip.add(Text.literal("\u0002").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.FROST.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.FROST.get() + 6], 0x80FFFF))));
        }
        if (attributeModifiers[AttributeEnum.FROST_RATIO.get() + 6] != null) {
            tooltip.add(Text.literal("\u0002").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.FROST_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.FROST_RATIO.get() + 6], 0x80FFFF))));
        }

        if (attributeModifiers[AttributeEnum.LIGHTNING.get() + 6] != null) {
            tooltip.add(Text.literal("\u0003").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.LIGHTNING.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.LIGHTNING.get() + 6], 0x8080FF))));
        }
        if (attributeModifiers[AttributeEnum.LIGHTNING_RATIO.get() + 6] != null) {
            tooltip.add(Text.literal("\u0003").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.LIGHTNING_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.LIGHTNING_RATIO.get() + 6], 0x8080FF))));
        }

        if (attributeModifiers[AttributeEnum.ACID.get() + 6] != null) {
            tooltip.add(Text.literal("\u0004").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.ACID.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.ACID.get() + 6], 0xC0FF80))));
        }
        if (attributeModifiers[AttributeEnum.ACID_RATIO.get() + 6] != null) {
            tooltip.add(Text.literal("\u0004").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.ACID_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.ACID_RATIO.get() + 6], 0xC0FF80))));
        }

        if (attributeModifiers[AttributeEnum.FLOOD.get() + 6] != null) {
            tooltip.add(Text.literal("\u0005").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.FLOOD.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLOOD.get() + 6], 0x80C0FF))));
        }
        if (attributeModifiers[AttributeEnum.FLOOD_RATIO.get() + 6] != null) {
            tooltip.add(Text.literal("\u0005").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.FLOOD_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.FLOOD_RATIO.get() + 6], 0x80C0FF))));
        }

        if (attributeModifiers[AttributeEnum.ECHO.get() + 6] != null) {
            tooltip.add(Text.literal("\u0006").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.ECHO.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.ECHO.get() + 6], 0x008080))));
        }

        if (attributeModifiers[AttributeEnum.ECHO_RATIO.get() + 6] != null) {
            tooltip.add(Text.literal("\u0006").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.ECHO_RATIO.get() + 6]) + "%").withColor(getColorWithSign(attributeModifiers[AttributeEnum.ECHO_RATIO.get() + 6], 0x008080))));
        }
        if (attributeModifiers[AttributeEnum.INJURY.get() + 6] != null) {
            tooltip.add(Text.literal("\u0007").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(attributeModifiers[AttributeEnum.INJURY.get() + 6])).withColor(getColorWithSign(attributeModifiers[AttributeEnum.INJURY.get() + 6], 0xFFFFFF))));
        }
    }

    @Unique
    private static void getOtherModifierTooltip(List<Text> tooltip, Integer[] modifiers, Identifier iconFont) {
        if (modifiers[ModifierEnum.RANGE.get()] != null) {
            tooltip.add(Text.literal("\u0008").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(modifiers[ModifierEnum.RANGE.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.RANGE.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.STURDY.get()] != null) {
            tooltip.add(Text.literal("\u0009").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(modifiers[ModifierEnum.STURDY.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.STURDY.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.LIGHTWEIGHT.get()] != null) {
            tooltip.add(Text.literal("\u000b").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(modifiers[ModifierEnum.LIGHTWEIGHT.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.LIGHTWEIGHT.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.CAPACITY.get()] != null) {
            tooltip.add(Text.literal("\u000c").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(modifiers[ModifierEnum.CAPACITY.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.CAPACITY.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.RICOCHET.get()] != null) {
            tooltip.add(Text.literal("\u000e").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(modifiers[ModifierEnum.RICOCHET.get()])).withColor(getColorWithSign(modifiers[ModifierEnum.RICOCHET.get()], 0xFFFFFF))));
        }
        if (modifiers[ModifierEnum.PRECISION.get()] != null) {
            tooltip.add(Text.literal("\u000f").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Text.literal(getSign(modifiers[ModifierEnum.PRECISION.get()]) + "%").withColor(getColorWithSign(modifiers[ModifierEnum.PRECISION.get()], 0xFFFFFF))));
        }
    }

    @Unique
    private static String getSign(int i) {
        if (i >= 0) {
            return "+" + i;
        } else {
            return String.valueOf(i);
        }
    }

    @Unique
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

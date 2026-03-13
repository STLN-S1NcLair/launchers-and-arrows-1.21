package net.stln.launchersandarrows.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.util.AttributeEffectsDictionary;
import net.stln.launchersandarrows.util.AttributeEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(Item.class)
public class ItemTooltipMixin {
    // 矢, バニラアイテム
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo info){
        ResourceLocation iconFont = ResourceLocation.fromNamespaceAndPath(LaunchersAndArrows.MOD_ID, "icons");
        Integer[] attributes = new Integer[7];
        Item item = stack.getItem();
        for (int i = 0; i < 7; i++) {
            attributes[i] = AttributeEffectsDictionary.getAttributeEffect(item, i);
        }
        if (AttributeEffectsDictionary.getDict().containsKey1(item)) {
            // tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.launchers_and_arrows.attribute_effects").withColor(0xC0C0C0));
        }
        if (attributes[AttributeEnum.FLAME.get()] != null) {
            tooltipComponents.add(Component.literal("\u0001").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(String.valueOf(attributes[AttributeEnum.FLAME.get()])).withColor(0xFFC080)));
        }
        if (attributes[AttributeEnum.FROST.get()] != null) {
            tooltipComponents.add(Component.literal("\u0002").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(String.valueOf(attributes[AttributeEnum.FROST.get()])).withColor(0x80FFFF)));
        }
        if (attributes[AttributeEnum.LIGHTNING.get()] != null) {
            tooltipComponents.add(Component.literal("\u0003").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(String.valueOf(attributes[AttributeEnum.LIGHTNING.get()])).withColor(0x8080FF)));
        }
        if (attributes[AttributeEnum.ACID.get()] != null) {
            tooltipComponents.add(Component.literal("\u0004").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(String.valueOf(attributes[AttributeEnum.ACID.get()])).withColor(0xC0FF80)));
        }
        if (attributes[AttributeEnum.FLOOD.get()] != null) {
            tooltipComponents.add(Component.literal("\u0005").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(String.valueOf(attributes[AttributeEnum.FLOOD.get()])).withColor(0x80C0FF)));
        }
        if (attributes[AttributeEnum.ECHO.get()] != null) {
            tooltipComponents.add(Component.literal("\u0006").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(String.valueOf(attributes[AttributeEnum.ECHO.get()])).withColor(0x008080)));
        }
        if (attributes[AttributeEnum.INJURY.get()] != null) {
            tooltipComponents.add(Component.literal("\u0007").setStyle(Style.EMPTY.withFont(iconFont))
                    .append(Component.literal(String.valueOf(attributes[AttributeEnum.INJURY.get()])).withColor(0xFFFFFF)));
        }
    }
}

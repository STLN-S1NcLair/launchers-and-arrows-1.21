package net.stln.launchersandarrows.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.ItemInit;

public class TextUtil {
    static ResourceLocation iconFont = ResourceLocation.fromNamespaceAndPath(LaunchersAndArrows.MOD_ID, "icons");

    public static Component getIconComponent(Item item){
        if (item == ItemInit.IGNITION_STRING.get()) {
            return Component.literal("\u1000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.FROSTBITE_STRING.get()) {
            return Component.literal("\u1001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.CHARGING_STRING.get()) {
            return Component.literal("\u1002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.DETERIORATION_STRING.get()) {
            return Component.literal("\u1003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.PERMEATION_STRING.get()) {
            return Component.literal("\u1004").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.VIBRATING_STRING.get()) {
            return Component.literal("\u1005").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.RANGE_STRING.get()) {
            return Component.literal("\u2000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.STURDY_STRING.get()) {
            return Component.literal("\u2001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.LIGHTWEIGHT_STRING.get()) {
            return Component.literal("\u2002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.SLIMY_STRING.get()) {
            return Component.literal("\u2003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.PRECISION_STRING.get()) {
            return Component.literal("\u2004").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.OVERLOADED_STRING.get()) {
            return Component.literal("\u2005").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.IGNITION_PULLEY.get()) {
            return Component.literal("\u3000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.COOLING_PULLEY.get()) {
            return Component.literal("\u3001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.POWER_GENERATION_PULLEY.get()) {
            return Component.literal("\u3002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.CORROSION_RESISTANT_PULLEY.get()) {
            return Component.literal("\u3003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.HYDROPHILIC_PULLEY.get()) {
            return Component.literal("\u3004").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.CONDUCTION_PULLEY.get()) {
            return Component.literal("\u3005").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.COMPOUND_PULLEY.get()) {
            return Component.literal("\u4000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.REINFORCED_PULLEY.get()) {
            return Component.literal("\u4001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.LUBRICATION_PULLEY.get()) {
            return Component.literal("\u4002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.POWERED_PULLEY.get()) {
            return Component.literal("\u4003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        return Component.empty();
    }

    public static int getNumberCenter(int i) {
        return getNumberLength(i) / 2;
    }

    public static int getNumberLength(int i) {
        String s = String.valueOf(i);
        return s.length() * 6;
    }

    public static int getNumberCenter(double i) {
        return getNumberLength(i) / 2;
    }

    public static int getNumberLength(double i) {
        String s = String.valueOf(i);
        return s.length() * 6 - 4;
    }
}

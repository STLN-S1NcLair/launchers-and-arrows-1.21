package net.stln.launchersandarrows.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.ItemInit;

public class TextUtil {
    static Identifier iconFont = Identifier.of(LaunchersAndArrows.MOD_ID, "icons");

    public static Text getIconText(Item item) {
        if (item == ItemInit.IGNITION_STRING) {
            return Text.literal("\u1000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.FROSTBITE_STRING) {
            return Text.literal("\u1001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.CHARGING_STRING) {
            return Text.literal("\u1002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.DETERIORATION_STRING) {
            return Text.literal("\u1003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.PERMEATION_STRING) {
            return Text.literal("\u1004").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.VIBRATING_STRING) {
            return Text.literal("\u1005").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.RANGE_STRING) {
            return Text.literal("\u2000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.STURDY_STRING) {
            return Text.literal("\u2001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.LIGHTWEIGHT_STRING) {
            return Text.literal("\u2002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.SLIMY_STRING) {
            return Text.literal("\u2003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.PRECISION_STRING) {
            return Text.literal("\u2004").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.OVERLOADED_STRING) {
            return Text.literal("\u2005").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.IGNITION_PULLEY) {
            return Text.literal("\u3000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.COOLING_PULLEY) {
            return Text.literal("\u3001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.POWER_GENERATION_PULLEY) {
            return Text.literal("\u3002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.CORROSION_RESISTANT_PULLEY) {
            return Text.literal("\u3003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.HYDROPHILIC_PULLEY) {
            return Text.literal("\u3004").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.CONDUCTION_PULLEY) {
            return Text.literal("\u3005").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.COMPOUND_PULLEY) {
            return Text.literal("\u4000").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.REINFORCED_PULLEY) {
            return Text.literal("\u4001").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.LUBRICATION_PULLEY) {
            return Text.literal("\u4002").setStyle(Style.EMPTY.withFont(iconFont));
        }
        if (item == ItemInit.POWERED_PULLEY) {
            return Text.literal("\u4003").setStyle(Style.EMPTY.withFont(iconFont));
        }
        return Text.empty();
    }

    public static int getNumberCenter(int i) {
        return getNumberLength(i) / 2;
    }

    public static int getNumberLength(int i) {
        String s = String.valueOf(i);
        return s.length() * 6;
    }
}

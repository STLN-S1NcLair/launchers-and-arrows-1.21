package net.stln.launchersandarrows.client.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.launchersandarrows.item.bow.MechanicalBowItem;
import net.stln.launchersandarrows.item.component.ChargeComponent;
import net.stln.launchersandarrows.item.component.ComponentInit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MechanicalBowInfoOverlay implements LayeredDraw.Layer {

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        ItemStack renderStack = main.getItem() instanceof MechanicalBowItem ? main : off.getItem() instanceof MechanicalBowItem ? off : ItemStack.EMPTY;

        if (renderStack.isEmpty()) return;

        ChargeComponent component = renderStack.get(ComponentInit.CHARGE_COMPONENT);
        List<Double> charges = component.getCharges();

        Font font = mc.font;
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();
        int cw = w / 2;
        int ch = h / 2;

        for(int i = 0; i < charges.size(); i++){
            String string = "";
            int index = 0;
            for(int j = 0; j < charges.size(); j++){
                if(charges.get(j) > 0){
                    index = j;
                }
            }
            int value = (int) Math.round(charges.get(i) * 100);
            if(i <= index){
                string = String.valueOf(value);
            }
            if(value == 0){
                string = "---";
            }
            while(string.length() < 3){
                string = 0 + string;
            }
            int color = i == index ? 0xFFFFFF : 0x404040;

            guiGraphics.drawString(font, "| " + string + " |", cw - 15, (int) (ch * 1.3) + 8 * (i - charges.size()), color, true);
        }
    }
}

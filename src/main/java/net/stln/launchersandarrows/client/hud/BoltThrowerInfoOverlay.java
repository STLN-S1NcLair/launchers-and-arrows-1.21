package net.stln.launchersandarrows.client.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.util.TextUtil;
import org.jetbrains.annotations.NotNull;

public class BoltThrowerInfoOverlay implements LayeredDraw.Layer {
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        ItemStack renderStack = main.getItem() instanceof BoltThrowerItem ? main : off.getItem() instanceof BoltThrowerItem ? off : ItemStack.EMPTY;

        if (renderStack.isEmpty()) return;

        int charged = renderStack.getOrDefault(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT, 0);
        int count = renderStack.getOrDefault(ComponentInit.BOLT_COUNT_COMPONENT, 0);

        Font font = mc.font;

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int cw = w / 2;
        int ch = h / 2;

        guiGraphics.drawString(font, "| " + charged + " |", cw - TextUtil.getNumberCenter(charged) - 6, (int)(ch * 1.3), 0xFFFFFF, true);
        guiGraphics.drawString(font, "| " + (count - charged) + " |", cw - TextUtil.getNumberCenter(count - charged) - 6, (int)(ch * 1.3) + 8, 0x808080, true);
    }
}

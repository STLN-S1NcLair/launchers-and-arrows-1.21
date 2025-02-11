package net.stln.launchersandarrows.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.stln.launchersandarrows.item.bow.MechanicalBowItem;
import net.stln.launchersandarrows.item.component.ChargeComponent;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.util.TextUtil;

import java.util.List;

public class MechanicalBowInfoRenderer {
    public static void register() {
        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            PlayerEntity playerEntity = MinecraftClient.getInstance().player;
            int h = MinecraftClient.getInstance().getWindow().getScaledHeight();
            int w = MinecraftClient.getInstance().getWindow().getScaledWidth();
            int ch = h / 2;
            int cw = w / 2;
            if (playerEntity != null) {
                ItemStack mainStack = playerEntity.getMainHandStack();
                ItemStack offStack = playerEntity.getOffHandStack();
                ItemStack renderStack = mainStack.getItem() instanceof MechanicalBowItem ? mainStack :
                        offStack.getItem() instanceof MechanicalBowItem ? offStack : null;
                if (renderStack != null) {
                    ChargeComponent component = renderStack.getComponents().get(ModComponentInit.CHARGE_COMPONENT);
                    List<Double> charges = component.getCharges();
                    for (int i = 0; i < charges.size(); i++) {
                        String string = "";
                        int index = 0;
                        for (int j = 0; j < charges.size(); j++) {
                            if (charges.get(j) > 0) {
                                index = j;
                            }
                        }
                        int value = (int) Math.round(charges.get(i) * 100);
                        if (i <= index) {
                            string = String.valueOf(value);
                        }
                        if (value == 0) {
                            string = "---";
                        }
                        while (string.length() < 3) {
                            string = 0 + string;
                        }
                        int color = i == index ? 0xFFFFFF : 0x404040;
                        drawContext.drawText(renderer, "| " + string + " |",
                                cw - 15, (int) (ch * 1.3) + 8 * (i - charges.size()), color, true);
                    }
                }
            }
        }));
    }
}

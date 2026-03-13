package net.stln.launchersandarrows.client.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.AttributeSynchedEntityData;
import org.jetbrains.annotations.NotNull;

public class AttributeEffectInfoOverlay implements LayeredDraw.Layer{
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LaunchersAndArrows.MOD_ID, "textures/gui/effect_bar.png");
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        Font font = mc.font;

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int cw = w / 2;
        int ch = h / 2;

        int offsetH = 32;
        int barW = 0;
        int frameW = 0;

        int flame = ((AttributeSynchedEntityData)player).getAccumulationTracker(0);
        int burning = ((AttributeSynchedEntityData)player).getEffectDuration(0);
        barW = getScaledBarWidth(player, flame, burning);
        frameW = getScaledFrameWidth(player, burning);
        if(barW > -1){
            guiGraphics.blit(TEXTURE, 2, h - offsetH, 24, 0, 24, 8, 64, 64);
            guiGraphics.blit(TEXTURE, 3, h - offsetH + 1, 1, 1, barW, 6, 64, 64);
            if(frameW > -1){
                guiGraphics.blit(TEXTURE, 2, h - offsetH, 0, 56, frameW, 8, 64, 64);
                guiGraphics.drawString(font, String.valueOf(burning / 20), 30, h - offsetH, 0xFFFFFF, true);
            }
            else {
                guiGraphics.drawString(font, String.valueOf(flame), 30, h - offsetH, 0xFFFFFF, true);
            }
            offsetH += 10;
        }

        int frost = ((AttributeSynchedEntityData)player).getAccumulationTracker(1);
        int freeze = ((AttributeSynchedEntityData)player).getEffectDuration(1);
        barW = getScaledBarWidth(player, frost, freeze);
        frameW = getScaledFrameWidth(player, freeze);
        if(barW > -1){
            guiGraphics.blit(TEXTURE, 2, h - offsetH, 24, 8, 24, 8, 64, 64);
            guiGraphics.blit(TEXTURE, 3, h - offsetH + 1, 1, 9, barW, 6, 64, 64);
            if(frameW > -1){
                guiGraphics.blit(TEXTURE, 2, h - offsetH, 0, 56, frameW, 8, 64, 64);
                guiGraphics.drawString(font, String.valueOf(freeze / 20), 30, h - offsetH, 0xFFFFFF, true);
            }
            else {
                guiGraphics.drawString(font, String.valueOf(frost), 30, h - offsetH, 0xFFFFFF, true);
            }
            offsetH += 10;
        }

        int lightning = ((AttributeSynchedEntityData)player).getAccumulationTracker(2);
        int electricShock = ((AttributeSynchedEntityData)player).getEffectDuration(2);
        barW = getScaledBarWidth(player, lightning, electricShock);
        frameW = getScaledFrameWidth(player, electricShock);
        if (barW > -1) {
            guiGraphics.blit(TEXTURE, 2, h - offsetH, 24, 16, 24, 8, 64, 64);
            guiGraphics.blit(TEXTURE, 3, h - offsetH + 1, 1, 17, barW, 6, 64, 64);
            if (frameW > -1) {
                guiGraphics.blit(TEXTURE, 2, h - offsetH, 0, 56, frameW, 8, 64, 64);
                guiGraphics.drawString(font,
                        String.valueOf(electricShock / 20),
                        30, h - offsetH, 0xFFFFFF, true);
            } else {
                guiGraphics.drawString(font,
                        String.valueOf(lightning),
                        30, h - offsetH, 0xFFFFFF, true);
            }
            offsetH += 10;
        }

        int acid = ((AttributeSynchedEntityData)player).getAccumulationTracker(3);
        int corrosion = ((AttributeSynchedEntityData)player).getEffectDuration(3);
        barW = getScaledBarWidth(player, acid, corrosion);
        frameW = getScaledFrameWidth(player, corrosion);
        if (barW > -1) {
            guiGraphics.blit(TEXTURE, 2, h - offsetH, 24, 24, 24, 8, 64, 64);
            guiGraphics.blit(TEXTURE, 3, h - offsetH + 1, 1, 25, barW, 6, 64, 64);
            if (frameW > -1) {
                guiGraphics.blit(TEXTURE, 2, h - offsetH, 0, 56, frameW, 8, 64, 64);
                guiGraphics.drawString(font,
                        String.valueOf(corrosion / 20),
                        30, h - offsetH, 0xFFFFFF, true);
            } else {
                guiGraphics.drawString(font,
                        String.valueOf(acid),
                        30, h - offsetH, 0xFFFFFF, true);
            }
            offsetH += 10;
        }

        int flood = ((AttributeSynchedEntityData)player).getAccumulationTracker(4);
        int submerged = ((AttributeSynchedEntityData)player).getEffectDuration(4);
        barW = getScaledBarWidth(player, flood, submerged);
        frameW = getScaledFrameWidth(player, submerged);
        if (barW > -1) {
            guiGraphics.blit(TEXTURE, 2, h - offsetH, 24, 32, 24, 8, 64, 64);
            guiGraphics.blit(TEXTURE, 3, h - offsetH + 1, 1, 33, barW, 6, 64, 64);
            if (frameW > -1) {
                guiGraphics.blit(TEXTURE, 2, h - offsetH, 0, 56, frameW, 8, 64, 64);
                guiGraphics.drawString(font,
                        String.valueOf(submerged / 20),
                        30, h - offsetH, 0xFFFFFF, true);
            } else {
                guiGraphics.drawString(font,
                        String.valueOf(flood),
                        30, h - offsetH, 0xFFFFFF, true);
            }
            offsetH += 10;
        }

        int echo = ((AttributeSynchedEntityData)player).getAccumulationTracker(5);
        int confusion = ((AttributeSynchedEntityData)player).getEffectDuration(5);
        barW = getScaledBarWidth(player, echo, confusion);
        frameW = getScaledFrameWidth(player, confusion);
        if (barW > -1) {
            guiGraphics.blit(TEXTURE, 2, h - offsetH, 24, 40, 24, 8, 64, 64);
            guiGraphics.blit(TEXTURE, 3, h - offsetH + 1, 1, 41, barW, 6, 64, 64);
            if (frameW > -1) {
                guiGraphics.blit(TEXTURE, 2, h - offsetH, 0, 56, frameW, 8, 64, 64);
                guiGraphics.drawString(font,
                        String.valueOf(confusion / 20),
                        30, h - offsetH, 0xFFFFFF, true);
            } else {
                guiGraphics.drawString(font,
                        String.valueOf(echo),
                        30, h - offsetH, 0xFFFFFF, true);
            }
            offsetH += 10;
        }

        int injury = ((AttributeSynchedEntityData)player).getAccumulationTracker(6);
        barW = getScaledBarWidth(player, injury, 0);
        if (barW > -1) {
            guiGraphics.blit(TEXTURE, 2, h - offsetH, 24, 48, 24, 8, 64, 64);
            guiGraphics.blit(TEXTURE, 3, h - offsetH + 1, 1, 49, barW, 6, 64, 64);
            guiGraphics.drawString(font,
                    String.valueOf(injury),
                    30, h - offsetH, 0xFFFFFF, true);
        }
    }

    private static int getScaledBarWidth(Player player, int accumulation, int duration) {
        if (duration > 0) {
            return 22;
        }
        if (accumulation > 0) {
            int maxAmpl = (int) (Math.sqrt(player.getMaxHealth()) * 5);
            return (accumulation * 22 / maxAmpl);
        }
        return -1;
    }
    private static int getScaledFrameWidth(Player player, int duration) {
        if (duration > 0) {
            return duration * 24 / 300;
        }
        return -1;
    }
}

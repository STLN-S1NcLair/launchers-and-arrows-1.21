package net.stln.launchersandarrows.client.hud;

import net.stln.launchersandarrows.LaunchersAndArrows;

public class HudInit {
    public static void registerHud() {
        LaunchersAndArrows.LOGGER.info("Registering Hud for " + LaunchersAndArrows.MOD_ID);
        BoltThrowerInfoRenderer.register();
        MechanicalBowInfoRenderer.register();
        AttributeEffectInfoRenderer.register();
    }
}

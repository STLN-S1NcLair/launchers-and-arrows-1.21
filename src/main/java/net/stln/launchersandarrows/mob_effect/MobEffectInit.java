package net.stln.launchersandarrows.mob_effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.particle.ParticleInit;

public class MobEffectInit {
    private static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, LaunchersAndArrows.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> FLAME_ACCUMULATION = STATUS_EFFECTS.register("flame_accumulation",
            id -> new FlameAccumulationEffect(MobEffectCategory.HARMFUL, 0xFFAB32));
    public static final DeferredHolder<MobEffect, MobEffect> BURNING = STATUS_EFFECTS.register("burning",
            id -> new BurningEffect(MobEffectCategory.HARMFUL, 0xFFAB32));


    public static void registerMobEffects(IEventBus eventBus) {
        LaunchersAndArrows.LOGGER.info("Registering Mob Effect for " + LaunchersAndArrows.MOD_ID);
        STATUS_EFFECTS.register(eventBus);
    }
}

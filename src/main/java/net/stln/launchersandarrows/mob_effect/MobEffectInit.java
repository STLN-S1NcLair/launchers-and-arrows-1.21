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

    public static final DeferredHolder<MobEffect, MobEffect> SHOCK_EXPLOSION = STATUS_EFFECTS.register("shock_explosion",
            id -> new ShockExplosionEffect(MobEffectCategory.HARMFUL, 0xCDC7FF));

    public static final DeferredHolder<MobEffect, MobEffect> BURNING = STATUS_EFFECTS.register("burning",
            id -> new BurningEffect(MobEffectCategory.HARMFUL, 0xFFAB32));
    public static final DeferredHolder<MobEffect, MobEffect> FREEZE = STATUS_EFFECTS.register("freeze",
            id -> new FreezeEffect(MobEffectCategory.HARMFUL, 0x89FEFF));
    public static final DeferredHolder<MobEffect, MobEffect> ELECTRIC_SHOCK = STATUS_EFFECTS.register("electric_shock",
            id -> new ElectricShockEffect(MobEffectCategory.HARMFUL, 0x4C5CFF));
    public static final DeferredHolder<MobEffect, MobEffect> CORROSION = STATUS_EFFECTS.register("corrosion",
            id -> new CorrosionEffect(MobEffectCategory.HARMFUL, 0xA3FF4C));
    public static final DeferredHolder<MobEffect, MobEffect> SUBMERGED = STATUS_EFFECTS.register("submerged",
            id -> new SubmergedEffect(MobEffectCategory.HARMFUL, 0x74C6FF));
    public static final DeferredHolder<MobEffect, MobEffect> CONFUSION = STATUS_EFFECTS.register("confusion",
            id -> new ConfusionEffect(MobEffectCategory.HARMFUL, 0x00A6B0));

    public static final DeferredHolder<MobEffect, MobEffect> SERIOUS_INJURY = STATUS_EFFECTS.register("serious_injury",
            id -> new SeriousInjuryEffect(MobEffectCategory.HARMFUL, 0x800000));

    public static void registerMobEffects(IEventBus eventBus) {
        LaunchersAndArrows.LOGGER.info("Registering Mob Effect for " + LaunchersAndArrows.MOD_ID);
        STATUS_EFFECTS.register(eventBus);
    }
}

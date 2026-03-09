package net.stln.launchersandarrows.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;

import java.util.function.Supplier;

public class ParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, LaunchersAndArrows.MOD_ID);

    public static final Supplier<SimpleParticleType> FLAME_EFFECT = PARTICLE_TYPES.register("flame_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> FROST_EFFECT = PARTICLE_TYPES.register("frost_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> LIGHTNING_EFFECT = PARTICLE_TYPES.register("lightning_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> ACID_EFFECT = PARTICLE_TYPES.register("acid_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> FLOOD_EFFECT = PARTICLE_TYPES.register("flood_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> ECHO_EFFECT = PARTICLE_TYPES.register("echo_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> WAVE_EFFECT = PARTICLE_TYPES.register("wave_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> HOMING_EFFECT = PARTICLE_TYPES.register("homing_effect", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> GLITCH_EFFECT = PARTICLE_TYPES.register("glitch_effect", () -> new SimpleParticleType(true));

    @OnlyIn(Dist.CLIENT)
    public static void registerParticleClient(IEventBus eventBus) {
        LaunchersAndArrows.LOGGER.info("Registering Client Particle for " + LaunchersAndArrows.MOD_ID);
        PARTICLE_TYPES.register(eventBus);
    }

    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(FLAME_EFFECT.get(), FlameEffectParticle.Provider::new);
        event.registerSpriteSet(FROST_EFFECT.get(), FrostEffectParticle.Provider::new);
        event.registerSpriteSet(LIGHTNING_EFFECT.get(), LightningEffectParticle.Provider::new);
        event.registerSpriteSet(ACID_EFFECT.get(), AcidEffectParticle.Provider::new);
        event.registerSpriteSet(FLOOD_EFFECT.get(), FloodEffectParticle.Provider::new);
        event.registerSpriteSet(ECHO_EFFECT.get(), EchoEffectParticle.Provider::new);
        event.registerSpriteSet(WAVE_EFFECT.get(), WaveEffectParticle.Provider::new);
        event.registerSpriteSet(HOMING_EFFECT.get(), HomingEffectParticle.Provider::new);
        event.registerSpriteSet(GLITCH_EFFECT.get(), GlitchEffectParticle.Provider::new);
    }
}

package net.stln.launchersandarrows.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.stln.launchersandarrows.LaunchersAndArrows;

public class ParticleInit {

    public static final SimpleParticleType FLAME_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType FROST_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType LIGHTNING_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType ACID_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType FLOOD_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType ECHO_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType WAVE_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType HOMING_EFFECT = FabricParticleTypes.simple(true);
    public static final SimpleParticleType GLITCH_EFFECT = FabricParticleTypes.simple(true);

    public static void registerParticleTypes() {
        LaunchersAndArrows.LOGGER.info("Registering Particle Types for " + LaunchersAndArrows.MOD_ID);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "flame_effect"), FLAME_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "frost_effect"), FROST_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "lightning_effect"), LIGHTNING_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "acid_effect"), ACID_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "flood_effect"), FLOOD_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "echo_effect"), ECHO_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "wave_effect"), WAVE_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "homing_effect"), HOMING_EFFECT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LaunchersAndArrows.MOD_ID, "glitch_effect"), GLITCH_EFFECT);
    }

    public static void registerParticleClient() {
        LaunchersAndArrows.LOGGER.info("Registering Client Particle for " + LaunchersAndArrows.MOD_ID);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.FLAME_EFFECT, FlameEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.FROST_EFFECT, FrostEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.LIGHTNING_EFFECT, LightningEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.ACID_EFFECT, AcidEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.FLOOD_EFFECT, FloodEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.ECHO_EFFECT, EchoEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.WAVE_EFFECT, WaveEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.HOMING_EFFECT, HomingEffectParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleInit.GLITCH_EFFECT, GlitchEffectParticle.Factory::new);
    }
}

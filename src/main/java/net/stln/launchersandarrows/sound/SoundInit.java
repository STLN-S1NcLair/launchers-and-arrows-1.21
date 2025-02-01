package net.stln.launchersandarrows.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.stln.launchersandarrows.LaunchersAndArrows;

public class SoundInit {
    public static final Identifier BOW_RELEASE_ID = Identifier.of("launchers_and_arrows:bow_release");
    public static SoundEvent BOW_RELEASE = SoundEvent.of(BOW_RELEASE_ID);
    public static final Identifier BOLT_THROWER_ID = Identifier.of("launchers_and_arrows:bolt_thrower");
    public static SoundEvent BOLT_THROWER = SoundEvent.of(BOLT_THROWER_ID);
    public static final Identifier CROSSLAUNCHER_ID = Identifier.of("launchers_and_arrows:crosslauncher");
    public static SoundEvent CROSSLAUNCHER = SoundEvent.of(CROSSLAUNCHER_ID);

    public static final Identifier FLAME_EFFECT_ID = Identifier.of("launchers_and_arrows:flame_effect");
    public static SoundEvent FLAME_EFFECT = SoundEvent.of(FLAME_EFFECT_ID);
    public static final Identifier FROST_EFFECT_ID = Identifier.of("launchers_and_arrows:frost_effect");
    public static SoundEvent FROST_EFFECT = SoundEvent.of(FROST_EFFECT_ID);
    public static final Identifier LIGHTNING_EFFECT_ID = Identifier.of("launchers_and_arrows:lightning_effect");
    public static SoundEvent LIGHTNING_EFFECT = SoundEvent.of(LIGHTNING_EFFECT_ID);
    public static final Identifier ACID_EFFECT_ID = Identifier.of("launchers_and_arrows:acid_effect");
    public static SoundEvent ACID_EFFECT = SoundEvent.of(ACID_EFFECT_ID);
    public static final Identifier FLOOD_EFFECT_ID = Identifier.of("launchers_and_arrows:flood_effect");
    public static SoundEvent FLOOD_EFFECT = SoundEvent.of(FLOOD_EFFECT_ID);
    public static final Identifier ECHO_EFFECT_ID = Identifier.of("launchers_and_arrows:echo_effect");
    public static SoundEvent ECHO_EFFECT = SoundEvent.of(ECHO_EFFECT_ID);
    public static final Identifier EXPLODE_ID = Identifier.of("launchers_and_arrows:explode");
    public static SoundEvent EXPLODE = SoundEvent.of(EXPLODE_ID);
    public static RegistryEntry.Reference<SoundEvent> EXPLODE_ENTRY = Registry.registerReference(Registries.SOUND_EVENT, EXPLODE_ID, EXPLODE);
    public static final Identifier WAVE_ID = Identifier.of("launchers_and_arrows:wave");
    public static SoundEvent WAVE = SoundEvent.of(WAVE_ID);
    public static final Identifier RELOAD_ID = Identifier.of("launchers_and_arrows:reload");
    public static SoundEvent RELOAD = SoundEvent.of(RELOAD_ID);
    public static final Identifier GLITCH_ID = Identifier.of("launchers_and_arrows:glitch");
    public static SoundEvent GLITCH = SoundEvent.of(GLITCH_ID);

    public static void registerSoundEvents() {
        LaunchersAndArrows.LOGGER.info("Registering Sounds for " + LaunchersAndArrows.MOD_ID);
        Registry.register(Registries.SOUND_EVENT, BOW_RELEASE_ID, BOW_RELEASE);
        Registry.register(Registries.SOUND_EVENT, BOLT_THROWER_ID, BOLT_THROWER);
        Registry.register(Registries.SOUND_EVENT, CROSSLAUNCHER_ID, CROSSLAUNCHER);
        Registry.register(Registries.SOUND_EVENT, FLAME_EFFECT_ID, FLAME_EFFECT);
        Registry.register(Registries.SOUND_EVENT, FROST_EFFECT_ID, FROST_EFFECT);
        Registry.register(Registries.SOUND_EVENT, LIGHTNING_EFFECT_ID, LIGHTNING_EFFECT);
        Registry.register(Registries.SOUND_EVENT, ACID_EFFECT_ID, ACID_EFFECT);
        Registry.register(Registries.SOUND_EVENT, FLOOD_EFFECT_ID, FLOOD_EFFECT);
        Registry.register(Registries.SOUND_EVENT, ECHO_EFFECT_ID, ECHO_EFFECT);
        Registry.register(Registries.SOUND_EVENT, EXPLODE_ID, EXPLODE);
        Registry.register(Registries.SOUND_EVENT, WAVE_ID, WAVE);
        Registry.register(Registries.SOUND_EVENT, RELOAD_ID, RELOAD);
        Registry.register(Registries.SOUND_EVENT, GLITCH_ID, GLITCH);
    }
}

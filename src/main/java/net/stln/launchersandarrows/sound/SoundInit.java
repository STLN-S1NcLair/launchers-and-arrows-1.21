package net.stln.launchersandarrows.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;

import java.util.function.Supplier;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, LaunchersAndArrows.MOD_ID);

    public static final Supplier<SoundEvent> BOW_RELEASE = registerSoundEvent("bow_release");
    public static final Supplier<SoundEvent> BOLT_THROWER = registerSoundEvent("bolt_thrower");
    public static final Supplier<SoundEvent> CROSSLAUNCHER = registerSoundEvent("crosslauncher");

    public static final Supplier<SoundEvent> FLAME_EFFECT = registerSoundEvent("flame_effect");
    public static final Supplier<SoundEvent> FROST_EFFECT = registerSoundEvent("frost_effect");
    public static final Supplier<SoundEvent> LIGHTNING_EFFECT = registerSoundEvent("lightning_effect");
    public static final Supplier<SoundEvent> ACID_EFFECT = registerSoundEvent("acid_effect");
    public static final Supplier<SoundEvent> FLOOD_EFFECT = registerSoundEvent("flood_effect");
    public static final Supplier<SoundEvent> ECHO_EFFECT = registerSoundEvent("echo_effect");

    public static final Supplier<SoundEvent> EXPLODE = registerSoundEvent("explode");
    public static final Supplier<SoundEvent> WAVE = registerSoundEvent("wave");
    public static final Supplier<SoundEvent> RELOAD = registerSoundEvent("reload");
    public static final Supplier<SoundEvent> GLITCH = registerSoundEvent("glitch");
    public static final Supplier<SoundEvent> RICOCHET = registerSoundEvent("ricochet");

    public static final Supplier<SoundEvent> MECHANICAL_BOW_CHARGE = registerSoundEvent("mechanical_bow_charge");
    public static final Supplier<SoundEvent> MECHANICAL_BOW_LOAD = registerSoundEvent("mechanical_bow_load");
    public static final Supplier<SoundEvent> MECHANICAL_BOW_RELEASE = registerSoundEvent("mechanical_bow_release");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
    }

    public static void registerSoundEvents(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

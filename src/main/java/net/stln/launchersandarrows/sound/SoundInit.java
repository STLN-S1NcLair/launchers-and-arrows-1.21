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

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
    }

    public static void registerSoundEvents(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

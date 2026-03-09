package net.stln.launchersandarrows.creative_tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.ItemInit;

public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LaunchersAndArrows.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LAUNCHERS_AND_ARROWS_TAB = CREATIVE_MOD_TABS.register("launchers_and_arrows_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.launchers_and_arrows.launchers_and_arrows"))
            .icon(() -> ItemInit.VOLATILE_FUEL.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // Weapons
                output.accept(ItemInit.LONG_BOW);
                output.accept(ItemInit.RAPID_BOW);
                output.accept(ItemInit.MODULAR_BOW);
                output.accept(ItemInit.MULTISHOT_BOW);
                output.accept(ItemInit.MECHANICAL_BOW);
                output.accept(ItemInit.RAINSHOT_BOW);
                output.accept(ItemInit.BOLT_THROWER);
                output.accept(ItemInit.QUICK_BOLT_THROWER);
                output.accept(ItemInit.CROSSLAUNCHER);
                output.accept(ItemInit.HOOK_LAUNCHER);
                output.accept(ItemInit.SLINGSHOT);

                // Arrows
                output.accept(ItemInit.FLAME_ARROW);
                output.accept(ItemInit.FREEZING_ARROW);
                output.accept(ItemInit.LIGHTNING_ARROW);
                output.accept(ItemInit.CORROSIVE_ARROW);
                output.accept(ItemInit.FLOOD_ARROW);
                output.accept(ItemInit.REVERBERATING_ARROW);
                output.accept(ItemInit.WAVE_ARROW);
                output.accept(ItemInit.PIERCING_ARROW);
                output.accept(ItemInit.HOMING_ARROW);
                output.accept(ItemInit.GLITCH_ARROW);
                output.accept(ItemInit.TAILWIND_ARROW);
                output.accept(ItemInit.LINEAR_ARROW);
                output.accept(ItemInit.BURST_ARROW);

                // Bolts
                output.accept(ItemInit.BOXED_BOLTS);
                output.accept(ItemInit.BOXED_FLAME_BOLTS);
                output.accept(ItemInit.BOXED_FREEZING_BOLTS);
                output.accept(ItemInit.BOXED_LIGHTNING_BOLTS);
                output.accept(ItemInit.BOXED_CORROSIVE_BOLTS);
                output.accept(ItemInit.BOXED_FLOOD_BOLTS);
                output.accept(ItemInit.BOXED_REVERBERATING_BOLTS);
                output.accept(ItemInit.BOXED_EXPLOSIVE_BOLTS);

                // Grappling Hook
                output.accept(ItemInit.GRAPPLING_HOOK);

                // Strings
                // ├ Attribute Modifiers
                output.accept(ItemInit.IGNITION_STRING);
                output.accept(ItemInit.FROSTBITE_STRING);
                output.accept(ItemInit.CHARGING_STRING);
                output.accept(ItemInit.DETERIORATION_STRING);
                output.accept(ItemInit.PERMEATION_STRING);
                output.accept(ItemInit.VIBRATING_STRING);

                // ├ Modifiers
                output.accept(ItemInit.RANGE_STRING);
                output.accept(ItemInit.STURDY_STRING);
                output.accept(ItemInit.LIGHTWEIGHT_STRING);
                output.accept(ItemInit.SLIMY_STRING);
                output.accept(ItemInit.PRECISION_STRING);
                output.accept(ItemInit.OVERLOADED_STRING);

                // Pulleys
                // ├ Attribute Modifiers
                output.accept(ItemInit.IGNITION_STRING);
                output.accept(ItemInit.FROSTBITE_STRING);
                output.accept(ItemInit.CHARGING_STRING);
                output.accept(ItemInit.DETERIORATION_STRING);
                output.accept(ItemInit.PERMEATION_STRING);
                output.accept(ItemInit.VIBRATING_STRING);

                // ├ Modifiers
                output.accept(ItemInit.COMPOUND_PULLEY);
                output.accept(ItemInit.REINFORCED_PULLEY);
                output.accept(ItemInit.LUBRICATION_PULLEY);
                output.accept(ItemInit.POWERED_PULLEY);

                // Self Erosion Plate
                output.accept(ItemInit.SELF_EROSION_PLATE);

                // Materials
                output.accept(ItemInit.VOLATILE_FUEL);
                output.accept(ItemInit.COOLANT);
                output.accept(ItemInit.REDSTONE_CAPACITOR);
                output.accept(ItemInit.ACID);
                output.accept(ItemInit.VISCOUS_WATER);
                output.accept(ItemInit.ECHO_COIL);

                output.accept(ItemInit.METAL_ARROWHEAD);
                output.accept(ItemInit.CUSTOMMADE_TICKET);
            }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MOD_TABS.register(eventBus);
    }
}

package net.stln.launchersandarrows.item_group;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.ItemInit;

public final class ItemGroupInit {
    public static final ItemGroup LAUNCHERS_AND_ARROWS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemInit.LONG_BOW))
            .displayName(Text.translatable("itemgroup.launchers_and_arrows.launchers_and_arrows"))
            .entries((displayContext, entries) -> {
                entries.add(ItemInit.LONG_BOW);
                entries.add(ItemInit.RAPID_BOW);
                entries.add(ItemInit.MODULAR_BOW);
                entries.add(ItemInit.MULTISHOT_BOW);
                entries.add(ItemInit.RAINSHOT_BOW);
                entries.add(ItemInit.BOLT_THROWER);
                entries.add(ItemInit.QUICK_BOLT_THROWER);
                entries.add(ItemInit.CROSSLAUNCHER);
                entries.add(ItemInit.HOOK_LAUNCHER);
                entries.add(ItemInit.SLINGSHOT);
                entries.add(ItemInit.FLAME_ARROW);
                entries.add(ItemInit.FREEZING_ARROW);
                entries.add(ItemInit.LIGHTNING_ARROW);
                entries.add(ItemInit.CORROSIVE_ARROW);
                entries.add(ItemInit.FLOOD_ARROW);
                entries.add(ItemInit.REVERBERATING_ARROW);
                entries.add(ItemInit.WAVE_ARROW);
                entries.add(ItemInit.PIERCING_ARROW);
                entries.add(ItemInit.HOMING_ARROW);
                entries.add(ItemInit.GLITCH_ARROW);
                entries.add(ItemInit.TAILWIND_ARROW);
                entries.add(ItemInit.LINEAR_ARROW);
                entries.add(ItemInit.BURST_ARROW);
                entries.add(ItemInit.BOXED_BOLTS);
                entries.add(ItemInit.BOXED_FLAME_BOLTS);
                entries.add(ItemInit.BOXED_FREEZING_BOLTS);
                entries.add(ItemInit.BOXED_LIGHTNING_BOLTS);
                entries.add(ItemInit.BOXED_CORROSIVE_BOLTS);
                entries.add(ItemInit.BOXED_FLOOD_BOLTS);
                entries.add(ItemInit.BOXED_REVERBERATING_BOLTS);
                entries.add(ItemInit.BOXED_EXPLOSIVE_BOLTS);
                entries.add(ItemInit.GRAPPLING_HOOK);
                entries.add(ItemInit.IGNITION_STRING);
                entries.add(ItemInit.FROSTBITE_STRING);
                entries.add(ItemInit.CHARGING_STRING);
                entries.add(ItemInit.DETERIORATION_STRING);
                entries.add(ItemInit.PERMEATION_STRING);
                entries.add(ItemInit.VIBRATING_STRING);
                entries.add(ItemInit.RANGE_STRING);
                entries.add(ItemInit.STURDY_STRING);
                entries.add(ItemInit.LIGHTWEIGHT_STRING);
                entries.add(ItemInit.SLIMY_STRING);
                entries.add(ItemInit.PRECISION_STRING);
                entries.add(ItemInit.OVERLOADED_STRING);
                entries.add(ItemInit.IGNITION_PULLEY);
                entries.add(ItemInit.COOLING_PULLEY);
                entries.add(ItemInit.POWER_GENERATION_PULLEY);
                entries.add(ItemInit.CORROSION_RESISTANT_PULLEY);
                entries.add(ItemInit.HYDROPHILIC_PULLEY);
                entries.add(ItemInit.CONDUCTION_PULLEY);
                entries.add(ItemInit.COMPOUND_PULLEY);
                entries.add(ItemInit.REINFORCED_PULLEY);
                entries.add(ItemInit.LUBRICATION_PULLEY);
                entries.add(ItemInit.POWERED_PULLEY);
                entries.add(ItemInit.VOLATILE_FUEL);
                entries.add(ItemInit.COOLANT);
                entries.add(ItemInit.REDSTONE_CAPACITOR);
                entries.add(ItemInit.ACID);
                entries.add(ItemInit.VISCOUS_WATER);
                entries.add(ItemInit.ECHO_COIL);
                entries.add(ItemInit.METAL_ARROWHEAD);
                entries.add(ItemInit.CUSTOMMADE_TICKET);
            })
            .build();

    public static void registerItemGroup() {
        LaunchersAndArrows.LOGGER.info("Registering Item Group for " + LaunchersAndArrows.MOD_ID);
        Registry.register(Registries.ITEM_GROUP, Identifier.of(LaunchersAndArrows.MOD_ID, "launchers_and_arrows"), LAUNCHERS_AND_ARROWS);
    }
}

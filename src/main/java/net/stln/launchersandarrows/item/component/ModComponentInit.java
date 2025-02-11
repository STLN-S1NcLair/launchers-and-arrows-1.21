package net.stln.launchersandarrows.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.stln.launchersandarrows.LaunchersAndArrows;

public class ModComponentInit {

    public static final ComponentType<ModifierComponent> MODIFIER_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "modifier"),
            ComponentType.<ModifierComponent>builder().codec(ModifierComponent.CODEC).packetCodec(ModifierComponent.PACKET_CODEC).build()
    );

    public static final ComponentType<Boolean> SELF_REPAIR_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "self_repair"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static final ComponentType<String> ARROW_SELECTOR_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "arrow_selecter"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static final ComponentType<Integer> CHARGE_COUNT_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "charge_count"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<ChargeComponent> CHARGE_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "charge"),
            ComponentType.<ChargeComponent>builder().codec(ChargeComponent.CODEC).build()
    );

    public static final ComponentType<Integer> BOLT_COUNT_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "bolt_count"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> CHARGED_BOLT_COUNT_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "charged_bolt_count"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Boolean> CHARGING_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "charging"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );
}

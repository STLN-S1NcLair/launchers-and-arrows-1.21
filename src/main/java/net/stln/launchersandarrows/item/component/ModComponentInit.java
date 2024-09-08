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

    public static final ComponentType<String> ARROW_SELECTOR_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(LaunchersAndArrows.MOD_ID, "arrow_selecter"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );
}

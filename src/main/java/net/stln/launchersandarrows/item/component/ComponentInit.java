package net.stln.launchersandarrows.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;

import java.util.function.Supplier;

public class ComponentInit {

    public static final DeferredRegister.DataComponents COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, LaunchersAndArrows.MOD_ID);

    public static final Supplier<DataComponentType<ModifierComponent>> MODIFIER_COMPONENT = register("modifier", ModifierComponent.CODEC, ModifierComponent.STREAM_CODEC);
    public static final Supplier<DataComponentType<Boolean>> SELF_REPAIR_COMPONENT = register("self_repair", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final Supplier<DataComponentType<String>> ARROW_SELECTOR_COMPONENT = register("arrow_selector", Codec.STRING, ByteBufCodecs.STRING_UTF8);
    public static final Supplier<DataComponentType<Integer>> CHARGE_COUNT_COMPONENT = register("charge_count", Codec.INT, ByteBufCodecs.INT);
    public static final Supplier<DataComponentType<ChargeComponent>> CHARGE_COMPONENT = register("charge", ChargeComponent.CODEC, ChargeComponent.STREAM_CODEC);
    public static final Supplier<DataComponentType<Integer>> BOLT_COUNT_COMPONENT = register("bolt_count", Codec.INT, ByteBufCodecs.INT);
    public static final Supplier<DataComponentType<Integer>> CHARGED_BOLT_COUNT_COMPONENT = register("charged_bolt_count", Codec.INT, ByteBufCodecs.INT);
    public static final Supplier<DataComponentType<Boolean>> CHARGING_COMPONENT = register("charging_component", Codec.BOOL, ByteBufCodecs.BOOL);

    public static void registerComponents(IEventBus eventBus) {
        LaunchersAndArrows.LOGGER.info("Registering Data Components for" + LaunchersAndArrows.MOD_ID);
        COMPONENT_TYPES.register(eventBus);
    }

    private static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return COMPONENT_TYPES.registerComponentType(name, builder -> builder.persistent(codec).networkSynchronized(streamCodec).cacheEncoding());
    }
}

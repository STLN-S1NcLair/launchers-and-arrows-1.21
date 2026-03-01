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
            .title(Component.translatable("itemgroup.launchers_and_arrows.launchers_and_arrows"))
            .icon(() -> ItemInit.VOLATILE_FUEL.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemInit.VOLATILE_FUEL);
                output.accept(ItemInit.COOLANT);
                output.accept(ItemInit.REDSTONE_CAPACITOR);
                output.accept(ItemInit.ACID);
                output.accept(ItemInit.VISCOUS_WATER);
                output.accept(ItemInit.ECHO_COIL);
            }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MOD_TABS.register(eventBus);
    }
}

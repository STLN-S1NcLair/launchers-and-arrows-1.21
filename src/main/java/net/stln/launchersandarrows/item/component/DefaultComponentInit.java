package net.stln.launchersandarrows.item.component;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.ItemInit;

@EventBusSubscriber(modid = LaunchersAndArrows.MOD_ID)
public class DefaultComponentInit {
    @SubscribeEvent
    public static void modifyDefault(ModifyDefaultComponentsEvent event){
        setComponentsForModifiableBow(event, ItemInit.LONG_BOW);
        setComponentsForModifiableBow(event, ItemInit.RAPID_BOW);
        setComponentsForModifiableBow(event, ItemInit.MODULAR_BOW);
        setComponentsForModifiableBow(event, ItemInit.MULTISHOT_BOW);

        event.modify(ItemInit.RAINSHOT_BOW, builder -> builder
                .set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.DEFAULT)
                .set(ComponentInit.SELF_REPAIR_COMPONENT.get(), false)
                .set(ComponentInit.CHARGE_COUNT_COMPONENT.get(), 0)
                .build());

        event.modify(ItemInit.SLINGSHOT, builder -> builder.set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.DEFAULT).build());
    }

    private static void setComponentsForModifiableBow(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event, DeferredItem<?> item) {
        event.modify(item, builder -> builder
                .set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.DEFAULT)
                .set(ComponentInit.SELF_REPAIR_COMPONENT.get(), false)
                .build());
    }
}

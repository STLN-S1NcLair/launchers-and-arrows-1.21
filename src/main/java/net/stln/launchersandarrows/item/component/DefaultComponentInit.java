package net.stln.launchersandarrows.item.component;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ChargedProjectiles;
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

        event.modify(ItemInit.MECHANICAL_BOW, builder -> builder
                .set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.EMPTY)
                .set(ComponentInit.SELF_REPAIR_COMPONENT.get(), false)
                .set(ComponentInit.CHARGE_COMPONENT.get(), ChargeComponent.EMPTY)
                .build()
        );

        event.modify(ItemInit.RAINSHOT_BOW, builder -> builder
                .set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.EMPTY)
                .set(ComponentInit.SELF_REPAIR_COMPONENT.get(), false)
                .set(ComponentInit.CHARGE_COUNT_COMPONENT.get(), 0)
                .build());

        setComponentsForBoltThrower(event, ItemInit.BOLT_THROWER);
        setComponentsForBoltThrower(event, ItemInit.QUICK_BOLT_THROWER);

        event.modify(ItemInit.SLINGSHOT, builder -> builder.set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.EMPTY).build());
    }

    private static void setComponentsForModifiableBow(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event, DeferredItem<?> item) {
        event.modify(item, builder -> builder
                .set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.EMPTY)
                .set(ComponentInit.SELF_REPAIR_COMPONENT.get(), false)
                .build());
    }

    private static void setComponentsForBoltThrower(net.neoforged.neoforge.event.ModifyDefaultComponentsEvent event, DeferredItem<?> item) {
        event.modify(item, builder -> builder
                .set(ComponentInit.MODIFIER_COMPONENT.get(), ModifierComponent.EMPTY)
                .set(ComponentInit.SELF_REPAIR_COMPONENT.get(), false)
                .set(ComponentInit.BOLT_COUNT_COMPONENT.get(), 0)
                .set(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT.get(), 0)
                .set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY)
                .set(ComponentInit.CHARGING_COMPONENT.get(), false)
                .build());
    }
}

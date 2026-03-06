package net.stln.launchersandarrows.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;

public class CustomModelPredicateProvider {
    public static void registerModModels() {
        LaunchersAndArrows.LOGGER.info("Registering Item Model for " + LaunchersAndArrows.MOD_ID);
        registerModBow(ItemInit.LONG_BOW.get());
        registerModBow(ItemInit.RAPID_BOW.get());
        registerModBow(ItemInit.MODULAR_BOW.get());
        registerModBow(ItemInit.MULTISHOT_BOW.get());
        //registerModBow(ItemInit.MECHANICAL_BOW.get());
        //registerMechanicalBow(ItemInit.MECHANICAL_BOW.get());
        //registerModBow(ItemInit.RAINSHOT_BOW.get());
        //registerBoltThrower(ItemInit.BOLT_THROWER.get());
        //registerBoltThrower(ItemInit.QUICK_BOLT_THROWER.get());
        //registerCrossLauncher(ItemInit.CROSSLAUNCHER.get());
        //registerCrossLauncher(ItemInit.HOOK_LAUNCHER.get());
        //registerCrossLauncher(ItemInit.SLINGSHOT.get());
    }

    private static void registerModBow(Item bow){
        ItemProperties.register(bow, ResourceLocation.withDefaultNamespace("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            }
            return entity.getUseItem() != stack ? 0.0F : ((ModifiableBowItem) bow).getModifiedPullProgress(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks(), stack);
        });
        ItemProperties.register(bow, ResourceLocation.withDefaultNamespace("pulling"), (stack, level, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
    }
}

package net.stln.launchersandarrows.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;
import net.stln.launchersandarrows.item.component.ComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.item.launcher.CrossLauncherItem;

public class CustomModelPredicateProvider {
    public static void registerModModels() {
        LaunchersAndArrows.LOGGER.info("Registering Item Model for " + LaunchersAndArrows.MOD_ID);
        registerModBow(ItemInit.LONG_BOW.get());
        registerModBow(ItemInit.RAPID_BOW.get());
        registerModBow(ItemInit.MODULAR_BOW.get());
        registerModBow(ItemInit.MULTISHOT_BOW.get());
        registerModBow(ItemInit.MECHANICAL_BOW.get());
        registerMechanicalBow(ItemInit.MECHANICAL_BOW.get());
        registerModBow(ItemInit.RAINSHOT_BOW.get());
        registerBoltThrower(ItemInit.BOLT_THROWER.get());
        registerBoltThrower(ItemInit.QUICK_BOLT_THROWER.get());
        registerCrossLauncher(ItemInit.CROSSLAUNCHER.get());
        registerCrossLauncher(ItemInit.HOOK_LAUNCHER.get());
        registerCrossLauncher(ItemInit.SLINGSHOT.get());
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

    private static void registerMechanicalBow(Item bow){
        ItemProperties.register(bow, ResourceLocation.withDefaultNamespace("charging"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            }
            return entity.isShiftKeyDown() ? 1.0F : 0.0F;
        });
    }

    private static void registerBoltThrower(Item boltThrower){
        ItemProperties.register(boltThrower, ResourceLocation.withDefaultNamespace("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            }
            return entity.getUseItem() != stack
                    ? (float) stack.get(ComponentInit.CHARGED_BOLT_COUNT_COMPONENT) / ((BoltThrowerItem) boltThrower).getMaxChargeCount()
                    : ((BoltThrowerItem) boltThrower).getModifiedPullProgress(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks(), stack);
        });
        ItemProperties.register(boltThrower, ResourceLocation.withDefaultNamespace("pulling"), (stack, level, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
    }

    private static void registerCrossLauncher(Item crossLauncher){
        ItemProperties.register(crossLauncher, ResourceLocation.withDefaultNamespace("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            }
            return CrossLauncherItem.isCharged(stack) ? 0.0F: (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / (float)CrossLauncherItem.getChargeDuration(stack, entity);
        });

        ItemProperties.register(crossLauncher, ResourceLocation.withDefaultNamespace("pulling"), (stack, level, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );

        ItemProperties.register(crossLauncher, ResourceLocation.withDefaultNamespace("charged"), (stack, level, entity, seed) ->
                CrossLauncherItem.isCharged(stack) ? 1.0F : 0.0F
        );

        ItemProperties.register(crossLauncher, ResourceLocation.withDefaultNamespace("firework"), (stack, level, entity, seed) -> {
            ChargedProjectiles chargedProjectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
            return chargedProjectiles != null && chargedProjectiles.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }
}

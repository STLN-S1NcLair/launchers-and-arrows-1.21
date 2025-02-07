package net.stln.launchersandarrows.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.item.bow.ModfiableBowItem;
import net.stln.launchersandarrows.item.component.ModComponentInit;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;
import net.stln.launchersandarrows.item.launcher.CrossLauncherItem;

@Environment(EnvType.CLIENT)
public class CustomModelPredicateProvider {
    public static void registerModModels() {
        LaunchersAndArrows.LOGGER.info("Registering Item Model for " + LaunchersAndArrows.MOD_ID);
        registerModBow(ItemInit.LONG_BOW);
        registerModBow(ItemInit.RAPID_BOW);
        registerModBow(ItemInit.MODULAR_BOW);
        registerModBow(ItemInit.MULTISHOT_BOW);
        registerModBow(ItemInit.RAINSHOT_BOW);
        registerBoltThrower(ItemInit.BOLT_THROWER);
        registerBoltThrower(ItemInit.QUICK_BOLT_THROWER);
        registerCrosslauncher(ItemInit.CROSSLAUNCHER);
        registerCrosslauncher(ItemInit.HOOK_LAUNCHER);
        registerCrosslauncher(ItemInit.SLINGSHOT);

    }

    private static void registerModBow(Item bow) {
        ModelPredicateProviderRegistry.register(bow, Identifier.ofVanilla("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : ((ModfiableBowItem) bow).getModifiedPullProgress(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft(), stack);
            }
        });
        ModelPredicateProviderRegistry.register(bow, Identifier.ofVanilla("pulling"), (stack, world, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
        );
    }

    private static void registerBoltThrower(Item thrower) {
        ModelPredicateProviderRegistry.register(thrower, Identifier.ofVanilla("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? (float) stack.get(ModComponentInit.CHARGED_BOLT_COUNT_COMPONENT) / ((BoltThrowerItem) thrower).getMaxChargeCount() : ((BoltThrowerItem) thrower).getModifiedPullProgress(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft(), stack);
            }
        });
        ModelPredicateProviderRegistry.register(thrower, Identifier.ofVanilla("pulling"), (stack, world, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
        );
    }

    public static void registerCrosslauncher(Item crosslauncher) {
        ModelPredicateProviderRegistry.register(
                crosslauncher,
                Identifier.ofVanilla("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        return CrossLauncherItem.isCharged(stack)
                                ? 0.0F
                                : (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / (float)CrossLauncherItem.getPullTime(stack, entity);
                    }
                }
        );
        ModelPredicateProviderRegistry.register(
                crosslauncher,
                Identifier.ofVanilla("pulling"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !CrossLauncherItem.isCharged(stack) ? 1.0F : 0.0F
        );
        ModelPredicateProviderRegistry.register(crosslauncher, Identifier.ofVanilla("charged"), (stack, world, entity, seed) -> CrossLauncherItem.isCharged(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(crosslauncher, Identifier.ofVanilla("firework"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }
}

package net.stln.launchersandarrows.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.projectile.ItemProjectile;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, LaunchersAndArrows.MOD_ID);

    public static final Supplier<EntityType<ItemProjectile>> ITEM_PROJECTILE = registerMobEntity("item_projectile",
            ItemProjectile::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));

    private static <T extends Entity> Supplier<EntityType<T>> registerMobEntity(String path, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> operator) {
        return ENTITY_TYPES.register(path, id -> operator.apply(EntityType.Builder.of(factory, category)).build(id.getPath()));
    }

    public static void registerModEntities(IEventBus eventBus) {
        LaunchersAndArrows.LOGGER.info("Registering Entity for " + LaunchersAndArrows.MOD_ID);
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerModEntitiesRenderer() {
        LaunchersAndArrows.LOGGER.info("Registering Entity Renderer for " + LaunchersAndArrows.MOD_ID);
        EntityRenderers.register(EntityInit.ITEM_PROJECTILE.get(), ThrownItemRenderer::new);
    }
}

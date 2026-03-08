package net.stln.launchersandarrows.entity.renderer;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.projectile.Bolt;

public class BoltRenderer extends ArrowRenderer<Bolt> {

    public BoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Bolt bolt) {
        return ResourceLocation.fromNamespaceAndPath(LaunchersAndArrows.MOD_ID, "textures/entity/bolt.png");
    }
}

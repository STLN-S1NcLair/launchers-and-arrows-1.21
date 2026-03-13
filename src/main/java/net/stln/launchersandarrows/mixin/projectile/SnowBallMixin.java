package net.stln.launchersandarrows.mixin.projectile;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.stln.launchersandarrows.mob_effect.util.MobEffectUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public class SnowBallMixin {
    @Inject(method = "onHitEntity", at = @At("TAIL"))
    protected void onHitEntity(EntityHitResult result, CallbackInfo info) {
        if(!result.getEntity().level().isClientSide()){
            if(result.getEntity() instanceof LivingEntity livingEntity){
                MobEffectUtil.applyAttributeEffect(livingEntity, Items.SNOWBALL.getDefaultInstance());
            }
        }
    }
}

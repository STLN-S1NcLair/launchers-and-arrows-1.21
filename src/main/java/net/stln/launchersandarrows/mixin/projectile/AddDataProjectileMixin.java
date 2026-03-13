package net.stln.launchersandarrows.mixin.projectile;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.entity.RicochetProjectile;
import net.stln.launchersandarrows.entity.renderer.RicochetEffectProjectile;
import net.stln.launchersandarrows.sound.SoundInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AddDataProjectileMixin extends Entity implements BypassDamageCooldownProjectile, RicochetProjectile, AttributedProjectile {



    public AddDataProjectileMixin(EntityType<?> entityType, Level level){
        super(entityType, level);
    }

    @Unique
    AbstractArrow abstractArrow = (AbstractArrow) (Object) this;

    @Unique
    private static final EntityDataAccessor<Boolean> BYPASS_DAMAGE_COOLDOWN = SynchedEntityData.defineId(AddDataProjectileMixin.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<Integer> RICOCHET_COUNT = SynchedEntityData.defineId(AddDataProjectileMixin.class, EntityDataSerializers.INT);

    @Unique
    private static final EntityDataAccessor<CompoundTag> ATTRIBUTE_EFFECT = SynchedEntityData.defineId(AddDataProjectileMixin.class, EntityDataSerializers.COMPOUND_TAG);

    // f: initDataTracker
    @Shadow
    protected abstract void defineSynchedData(SynchedEntityData.Builder builder);

    @Override
    public void setBypass(boolean flag) {
        this.getEntityData().set(BYPASS_DAMAGE_COOLDOWN, Boolean.valueOf(flag));
    }

    @Override
    public boolean getBypass() {
        if(this.entityData.get(BYPASS_DAMAGE_COOLDOWN) != null){
            return this.getEntityData().get(BYPASS_DAMAGE_COOLDOWN);
        }
        return false;
    }

    @Override
    public void setRicochet(int i) {
        this.getEntityData().set(RICOCHET_COUNT, i);
    }

    @Override
    public int getRicochet(){
        if(this.getEntityData().get(RICOCHET_COUNT) != null){
            return this.entityData.get(RICOCHET_COUNT);
        }
        return 0;
    }

    @Override
    public void setAttribute(int id, int amount) {
        CompoundTag compound = this.getEntityData().get(ATTRIBUTE_EFFECT);
        compound.putInt(String.valueOf(id), amount);
        this.getEntityData().set(ATTRIBUTE_EFFECT, compound);
    }

    @Override
    public int getAttribute(int id) {
        CompoundTag compound = this.getEntityData().get(ATTRIBUTE_EFFECT);
        if (compound.contains(String.valueOf(id))) {
            return compound.getInt(String.valueOf(id));
        }
        return 0;
    }

    @Override
    public Integer[] getAttributes() {
        Integer[] array = new Integer[7];
        for (int i = 0; i < 7; i++) {
            array[i] = getAttribute(i);
        }
        return array;
    }

    @Override
    public Integer[] getRatioAttributes() {
        Integer[] array = new Integer[6];
        for (int i = 0; i < 6; i++) {
            array[i] = getAttribute(-i - 1);
        }
        return array;
    }

    // f: initDataTracker
    @Inject(method = "defineSynchedData", at = {@At("TAIL")})
    private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(BYPASS_DAMAGE_COOLDOWN, Boolean.FALSE);
        builder.define(RICOCHET_COUNT, 0);
        builder.define(ATTRIBUTE_EFFECT, new CompoundTag());
    }

    // 無敵時間の無効化
    // f: onEntityHit
    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        Entity target = hitResult.getEntity();
        if (getBypass() && target instanceof LivingEntity living) {
            living.invulnerableTime = 0;
        }
    }

    // リコシェ
    @Inject(method = "onHitBlock", at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;")}, cancellable = true)
    private void ricochet(BlockHitResult blockHitResult, CallbackInfo info){
        if(getRicochet() > 0){
            Direction dir = blockHitResult.getDirection();
            double x = 1.2;
            double y = 1.2;
            double z = 1.2;
            switch (dir){
                case Direction.EAST, Direction.WEST -> x *= -1;
                case Direction.UP, Direction.DOWN -> y *= -1;
                case Direction.NORTH, Direction.SOUTH -> z *= -1;
            }
            abstractArrow.setDeltaMovement(abstractArrow.getDeltaMovement().multiply(x, y, z));
            Vec3 velocity = abstractArrow.getDeltaMovement();
            if(velocity.length() < 1){
                abstractArrow.setDeltaMovement(velocity.normalize().scale(0.8));
            }
            setRicochet(getRicochet() - 1);
            Vec3 vec3 = blockHitResult.getLocation().subtract(abstractArrow.getX(), abstractArrow.getY(), abstractArrow.getZ());
            vec3.scale(1.05);
            abstractArrow.setPos(abstractArrow.getX() - vec3.x, abstractArrow.getY() - vec3.y, abstractArrow.getZ() - vec3.z);

            if(abstractArrow instanceof Arrow arrow){
                ((RicochetEffectProjectile)arrow).onRicochet(getRicochet());
            }

            Level level = abstractArrow.level();
            level.playSound(null,
                    abstractArrow.blockPosition(),
                    level.getBlockState(blockHitResult.getBlockPos()).getSoundType().getPlaceSound(),
                    SoundSource.PLAYERS,
                    0.5F, 1.0F
            );
            level.playSound(null,
                    abstractArrow.blockPosition(),
                    SoundInit.RICOCHET.get(),
                    SoundSource.PLAYERS,
                    0.25F,
                    1.0F
            );

            info.cancel();
        }
    }

    // f: writeCustonDataToNbt
    @Inject(method = "addAdditionalSaveData", at = {@At("TAIL")})
    private void addAdditionalSaveData(CompoundTag compound, CallbackInfo info){
        compound.putBoolean("BypassDamageCooldown", getBypass());

        int[] attributeArray = new int[7];
        for(int i = 0; i < 7; i++){
            attributeArray[i] = getAttribute(i);
        }
        compound.putIntArray("AttributeEffect", attributeArray);

        int[] attributeRatioArray = new int[7];
        for (int i = 0; i < 7; i++){
            attributeRatioArray[i] = getAttribute(-i - 1);
        }
        compound.putIntArray("AttributeRatioEffect", attributeRatioArray);
    }

    // f: readCustomDataFromNbt
    @Inject(method = "readAdditionalSaveData", at = {@At("TAIL")})
    private void readAdditionalSaveData(CompoundTag compound, CallbackInfo info){
        if(compound.contains("BypassDamageCooldown")){
            setBypass(compound.getBoolean("BypassDamageCooldown"));
        }
        if (compound.contains("AttributeEffect")) {
            int[] attributeArray = compound.getIntArray("AttributeEffect");
            for (int i = 0; i < 7; i++) {
                setAttribute(i, attributeArray[i]);
            }
            int[] attributeRatioArray = compound.getIntArray("AttributeRatioEffect");
            for (int i = 0; i < 7; i++) {
                setAttribute(-i - 1, attributeRatioArray[i]);
            }
        }
    }
}

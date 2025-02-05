package net.stln.launchersandarrows.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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

@Mixin(PersistentProjectileEntity.class)
public abstract class AddDataProjectileMixin extends Entity implements BypassDamageCooldownProjectile, RicochetProjectile, AttributedProjectile {



    @Unique
    PersistentProjectileEntity entity = (PersistentProjectileEntity) (Object) this;

    public AddDataProjectileMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract void initDataTracker(DataTracker.Builder builder);

    @Override
    public void setBypass(boolean flag) {
        this.dataTracker.set(BYPASS_DAMAGE_COOLDOWN, Boolean.valueOf(flag));
    }

    @Override
    public boolean getBypass() {
        if (this.dataTracker.get(BYPASS_DAMAGE_COOLDOWN) != null) {
            return this.dataTracker.get(BYPASS_DAMAGE_COOLDOWN);
        }
        return false;
    }

    @Override
    public void setRicochet(int i) {
        this.dataTracker.set(RICOCHET_COUNT, i);
    }

    @Override
    public int getRicochet() {
        if (this.dataTracker.get(RICOCHET_COUNT) != null) {
            return this.dataTracker.get(RICOCHET_COUNT);
        }
        return 0;
    }

    @Override
    public void setAttribute(int id, int amount) {
        NbtCompound nbtCompound = this.getDataTracker().get(ATTRIBUTE_EFFECT);
        nbtCompound.putInt(String.valueOf(id), amount);
        this.dataTracker.set(ATTRIBUTE_EFFECT, nbtCompound);
    }

    @Override
    public int getAttribute(int id) {
        NbtCompound nbtCompound = this.dataTracker.get(ATTRIBUTE_EFFECT);
        if (nbtCompound.contains(String.valueOf(id))) {
            return nbtCompound.getInt(String.valueOf(id));
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

    @Unique
    private static final TrackedData<Boolean> BYPASS_DAMAGE_COOLDOWN = DataTracker.registerData(AddDataProjectileMixin.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static final TrackedData<Integer> RICOCHET_COUNT = DataTracker.registerData(AddDataProjectileMixin.class, TrackedDataHandlerRegistry.INTEGER);

    @Unique
    private static final TrackedData<NbtCompound> ATTRIBUTE_EFFECT = DataTracker.registerData(AddDataProjectileMixin.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    @Inject(method = "initDataTracker", at = {@At("TAIL")})
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(BYPASS_DAMAGE_COOLDOWN, Boolean.FALSE);
        builder.add(RICOCHET_COUNT, 0);
        builder.add(ATTRIBUTE_EFFECT, new NbtCompound());
    }

    @Inject(method = "onEntityHit", at = {@At("HEAD")})
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity target = entityHitResult.getEntity();
        if (getBypass() && target instanceof LivingEntity) {
            target.timeUntilRegen = 0;
        }
    }

    @Inject(method = "onBlockHit", at = {@At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;")}, cancellable = true)
    private void ricochet(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (getRicochet() > 0) {
            Direction direction = blockHitResult.getSide();
            double x = 1.2;
            double y = 1.2;
            double z = 1.2;
            switch (direction) {
                case Direction.EAST, Direction.WEST -> x *= -1;
                case Direction.UP, Direction.DOWN -> y *= -1;
                case Direction.NORTH, Direction.SOUTH -> z *= -1;
            }
            entity.setVelocity(entity.getVelocity().multiply(x, y, z));
            Vec3d velocity = entity.getVelocity();
            if (velocity.length() < 1) {
                entity.setVelocity(velocity.normalize().multiply(0.8));
            }
            setRicochet(getRicochet() - 1);
            Vec3d vec3d = blockHitResult.getPos().subtract(entity.getX(), entity.getY(), entity.getZ());
            vec3d.multiply(1.05);
            entity.setPos(entity.getX() - vec3d.x, entity.getY() - vec3d.y, entity.getZ() - vec3d.z);
            if (entity instanceof ArrowEntity arrowEntity) {
                ((RicochetEffectProjectile)arrowEntity).onRicochet(getRicochet());
            }
            entity.getWorld().playSound(null, entity.getBlockPos(), entity.getWorld().getBlockState(blockHitResult.getBlockPos()).getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, 0.5F, 1.0F);
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundInit.RICOCHET, SoundCategory.PLAYERS, 0.25F, 1.0F);

            ci.cancel();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = {@At("TAIL")})
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("BypassDamageCooldown", getBypass());
        int[] attributeArray = new int[7];
        for (int i = 0; i < 7; i++) {
            attributeArray[i] = getAttribute(i);
        }
        nbt.putIntArray("AttributeEffect", attributeArray);
        int[] attributeRatioArray = new int[7];
        for (int i = 0; i < 7; i++) {
            attributeRatioArray[i] = getAttribute(-i - 1);
        }
        nbt.putIntArray("AttributeRatioEffect", attributeRatioArray);
    }

    @Inject(method = "readCustomDataFromNbt", at = {@At("TAIL")})
    private void readCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("BypassDamageCooldown")) {
            setBypass(nbt.getBoolean("BypassDamageCooldown"));
        }
        if (nbt.contains("AttributeEffect")) {
            int[] attributeArray = nbt.getIntArray("AttributeEffect");
            for (int i = 0; i < 7; i++) {
                setAttribute(i, attributeArray[i]);
            }
            int[] attributeRatioArray = nbt.getIntArray("AttributeRatioEffect");
            for (int i = 0; i < 7; i++) {
                setAttribute(-i - 1, attributeRatioArray[i]);
            }
        }
    }
}

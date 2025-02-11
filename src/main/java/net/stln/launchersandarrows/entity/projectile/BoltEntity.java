package net.stln.launchersandarrows.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.stln.launchersandarrows.entity.AttributedProjectile;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.entity.EntityInit;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.status_effect.util.StatusEffectUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.entity.projectile.AbstractWindChargeEntity.EXPLOSION_BEHAVIOR;

public class BoltEntity extends PersistentProjectileEntity {
    
    private int inGroundTime = 0;

    private static ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(
            false, true, Optional.of(0.5F), Optional.empty());

    private static final TrackedData<ItemStack> ITEM_STACK =
            DataTracker.registerData(BoltEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    
    ItemStack itemStack = ItemStack.EMPTY;
    
    public BoltEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.BOLT_PROJECTILE, owner, world, stack, shotFrom);
        this.itemStack = stack;
        ((BypassDamageCooldownProjectile)this).setBypass(true);
    }

    public BoltEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.BOLT_PROJECTILE, x, y, z, world, stack, shotFrom);
        this.itemStack = stack;
        ((BypassDamageCooldownProjectile)this).setBypass(true);
    }

    public BoltEntity(EntityType<? extends BoltEntity> entityType, World world) {
        super(entityType, world);
        ((BypassDamageCooldownProjectile)this).setBypass(true);
    }

    private ParticleEffect getparticleEffect() {
        if (itemStack.isOf(ItemInit.BOXED_FLAME_BOLTS)) return ParticleInit.FLAME_EFFECT;
        else if (itemStack.isOf(ItemInit.BOXED_FREEZING_BOLTS)) return ParticleInit.FROST_EFFECT;
        else if (itemStack.isOf(ItemInit.BOXED_LIGHTNING_BOLTS)) return ParticleInit.LIGHTNING_EFFECT;
        else if (itemStack.isOf(ItemInit.BOXED_CORROSIVE_BOLTS)) return ParticleInit.ACID_EFFECT;
        else if (itemStack.isOf(ItemInit.BOXED_FLOOD_BOLTS)) return ParticleInit.FLOOD_EFFECT;
        else if (itemStack.isOf(ItemInit.BOXED_REVERBERATING_BOLTS)) return ParticleInit.ECHO_EFFECT;
        else if (itemStack.isOf(ItemInit.BOXED_EXPLOSIVE_BOLTS)) return ParticleTypes.SMOKE;
        return null;
    }
    

    protected void setStack(ItemStack stack) {
        super.setStack(stack);
    }

    public void tick() {
        super.tick();
        this.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        if (this.getWorld().isClient) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.spawnParticles(1);
                }
            } else {
                this.spawnParticles(2);
            }
            Vec3d pos = this.getPos();
            if (inGroundTime > 18) {
            for (int i = 0; i < 10; i++) {
                double rx = Math.sin(this.getRandom().nextFloat() * 2 * Math.PI) / 2;
                double ry = Math.sin(this.getRandom().nextFloat() * 2 * Math.PI) / 2;
                double rz = Math.sin(this.getRandom().nextFloat() * 2 * Math.PI) / 2;
                this.getWorld().addParticle(ParticleTypes.SMOKE, rx + pos.x, ry + pos.y, rz + pos.z, rx / 10, ry / 10, rz / 10);
            }
            }
        }
        if (!this.getWorld().isClient()) {
            this.getDataTracker().set(ITEM_STACK, this.getItemStack());
        }
        itemStack = this.getDataTracker().get(ITEM_STACK);
        if (itemStack.isOf(ItemInit.BOXED_EXPLOSIVE_BOLTS)) {
            NbtCompound nbt = new NbtCompound();
            this.writeCustomDataToNbt(nbt);
            if (nbt.getBoolean("inGround")) {
                inGroundTime++;
            } else {
                inGroundTime = 0;
            }
            if (inGroundTime > 20) {
                generateExplosion();
            }
        }
    }

    private void generateExplosion() {
        Vec3d pos = this.getPos();

        this.getWorld().createExplosion(this, Explosion.createDamageSource(this.getWorld(), this),
                EXPLOSION_BEHAVIOR, pos.getX(), pos.getY(), pos.getZ(),
                1.2F, false, World.ExplosionSourceType.MOB,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundInit.EXPLODE_ENTRY);
        this.kill();
    }

    private void spawnParticles(int amount) {
        if (getparticleEffect() != null) {
            if (amount > 0) {
                NbtCompound nbt = new NbtCompound();
                this.writeCustomDataToNbt(nbt);
                for (int j = 0; j < amount; j++) {
                    if (!nbt.getBoolean("inGround") || this.getRandom().nextFloat() >= 0.65F) {
                        this.getWorld()
                                .addParticle(
                                        getparticleEffect(), this.getParticleX(0.25),
                                        this.getY() - 0.125 + (this.getRandom().nextFloat() / 4),
                                        this.getParticleZ(0.25),
                                        0.0, 0.0, 0.0
                                );
                    }
                }
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (itemStack.isOf(ItemInit.BOXED_EXPLOSIVE_BOLTS)) {
            generateExplosion();
        }
    }

    protected void onHit(LivingEntity target) {
        if (target instanceof LivingEntity livingEntity) {
            StatusEffectUtil.applyAttributeEffect(livingEntity, this.itemStack);
            StatusEffectUtil.applyAttributeModifier(livingEntity, ((AttributedProjectile) this).getAttributes());
            StatusEffectUtil.applyAttributeRatioModifier(livingEntity, this.itemStack, ((AttributedProjectile) this).getRatioAttributes());
        }
        super.onHit(target);
    }


    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ItemInit.BOXED_BOLTS);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ITEM_STACK, new ItemStack(ItemInit.BOXED_BOLTS));
    }
}

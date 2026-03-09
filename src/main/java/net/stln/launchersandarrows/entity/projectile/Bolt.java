package net.stln.launchersandarrows.entity.projectile;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.entity.EntityInit;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.particle.ParticleInit;
import net.stln.launchersandarrows.sound.SoundInit;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Bolt extends AbstractArrow {
    private int inGroundTime = 0;

    private static ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
            false, true, Optional.of(0.5F), Optional.empty());

    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(Bolt.class, EntityDataSerializers.ITEM_STACK);

    ItemStack itemStack = ItemStack.EMPTY;

    public Bolt(EntityType<? extends Bolt> entityType, Level level) {
        super(entityType, level);
    }

    public Bolt(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityInit.BOLT.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
        this.itemStack = pickupItemStack;
        ((BypassDamageCooldownProjectile)this).setBypass(true);
    }

    public Bolt(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon){
        super(EntityInit.BOLT.get(), owner, level, pickupItemStack, firedFromWeapon);
        this.itemStack = pickupItemStack;
        ((BypassDamageCooldownProjectile)this).setBypass(true);
    }

    // f: getParticleEffect
    private ParticleOptions getParticleOptions(){
        if (itemStack.is(ItemInit.BOXED_FLAME_BOLTS)) return ParticleInit.FLAME_EFFECT.get();
        else if (itemStack.is(ItemInit.BOXED_FREEZING_BOLTS)) return ParticleInit.FROST_EFFECT.get();
        else if (itemStack.is(ItemInit.BOXED_LIGHTNING_BOLTS)) return ParticleInit.LIGHTNING_EFFECT.get();
        else if (itemStack.is(ItemInit.BOXED_CORROSIVE_BOLTS)) return ParticleInit.ACID_EFFECT.get();
        else if (itemStack.is(ItemInit.BOXED_FLOOD_BOLTS)) return ParticleInit.FLOOD_EFFECT.get();
        else if (itemStack.is(ItemInit.BOXED_REVERBERATING_BOLTS)) return ParticleInit.ECHO_EFFECT.get();
        else if (itemStack.is(ItemInit.BOXED_EXPLOSIVE_BOLTS)) return ParticleTypes.SMOKE;
        return null;
    }

    // f: setStack
    protected void setPickupItemStack(ItemStack pickupItemStack){
        super.setPickupItemStack(pickupItemStack);
    }

    // f: initDataTracker
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ITEM_STACK, new ItemStack(ItemInit.BOXED_BOLTS.get()));
    }

    public void tick(){
        super.tick();
        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
            Vec3 pos = this.position();
            if(inGroundTime > 18){
                for(int i = 0; i < 10; i++){
                    double rx = Math.sin(this.getRandom().nextFloat() * 2 * Math.PI) / 2;
                    double ry = Math.sin(this.getRandom().nextFloat() * 2 * Math.PI) / 2;
                    double rz = Math.sin(this.getRandom().nextFloat() * 2 * Math.PI) / 2;
                    this.level().addParticle(ParticleTypes.SMOKE, rx + pos.x, ry + pos.y, rz + pos.z, rx / 10, ry /10, rz / 10);
                }
            }
        }
        if(!this.level().isClientSide()){
            this.getEntityData().set(ITEM_STACK, this.getPickupItem());
        }
        itemStack = this.getEntityData().get(ITEM_STACK);
        if(itemStack.is(ItemInit.BOXED_EXPLOSIVE_BOLTS)){
            CompoundTag compoundTag = new CompoundTag();
            this.addAdditionalSaveData(compoundTag);
            if(compoundTag.getBoolean("inGround")){
                inGroundTime++;
            }
            else {
                inGroundTime = 0;
            }
            if(inGroundTime > 20){
                generateExplosion();
            }
        }
    }

    private void generateExplosion(){
        Vec3 pos = this.position();

        this.level().explode(this, Explosion.getDefaultDamageSource(this.level(),
                this), EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(),
                1.2F, false, Level.ExplosionInteraction.MOB,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER,
                Holder.direct(SoundInit.EXPLODE.get())
        );
        this.kill();
    }

    // f: spawnParticles
    private void makeParticle(int particleAmount){
        if(getParticleOptions() != null){
            if(particleAmount > 0){
                CompoundTag compoundTag = new CompoundTag();
                this.addAdditionalSaveData(compoundTag);
                for(int j = 0; j < particleAmount; j++){
                    if(!compoundTag.getBoolean("inGround") || this.getRandom().nextFloat() >= 0.65F){
                        this.level().addParticle(
                                getParticleOptions(),
                                this.getRandomX(0.25F),
                                this.getY() - 0.125 + (this.getRandom().nextFloat() / 4F),
                                this.getRandomZ(0.25F),
                                0.0F, 0.0F, 0.0F
                        );
                    }
                }
            }
        }
    }

    // f: onEntityHit
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if(itemStack.is(ItemInit.BOXED_EXPLOSIVE_BOLTS)){
            generateExplosion();
        }
    }

    // f: onHit
    protected void doPostHurtEffects(LivingEntity living){
        // StatusEffectUtilに関する処理
        super.doPostHurtEffects(living);
    }

    // f: getDefaultItemStack
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemInit.BOXED_BOLTS.get());
    }

    @Override
    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        // do nothing
    }
}

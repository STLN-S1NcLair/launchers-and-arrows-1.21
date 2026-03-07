package net.stln.launchersandarrows.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.*;
import net.minecraft.core.particles.*;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.EntityInit;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.status_effect.util.StatusEffectUtil;

import java.util.List;

public class ItemProjectile extends ThrowableItemProjectile {

    private static final EntityDataAccessor<Integer> HIT_EFFECT_TICK =
            SynchedEntityData.defineId(ItemProjectile.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(ItemProjectile.class, EntityDataSerializers.BOOLEAN);


    public ItemProjectile(EntityType<ItemProjectile> type, Level level) {
        super(type, level);
    }

    public ItemProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level, ItemStack stack) {
        super(type, level);
        this.setItem(stack);
    }

    public ItemProjectile(Level level, LivingEntity owner, ItemStack stack) {
        super(EntityInit.ITEM_PROJECTILE.get(), owner, level);
        this.setItem(stack);
    }

    public ItemProjectile(Level level, LivingEntity owner, double x, double y, double z, ItemStack stack) {
        super(EntityInit.ITEM_PROJECTILE.get(), x, y, z, level);
        this.setOwner(owner);
        this.setItem(stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    private ParticleOptions getParticleParameters() {
        ItemStack stack = this.getItem();
        return stack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, stack);
    }

    public void handleStatus(byte status) {
        if (this.entityData.get(HIT)) {
            status = 0;
        }
        if (status == 3) {
            ParticleOptions particle = this.getParticleParameters();

            for (int i = 0; i < 8; i++) {
                level().addParticle(particle, getX(), getY(), getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    public void tick() {
        if (this.getItem().is(ItemInit.GRAPPLING_HOOK)) {
            Entity owner = this.getOwner();
            if (owner == null || owner.isShiftKeyDown()) {
                this.kill();
            } else {
                Vec3 hookPos = this.position();
                Vec3 ownerPos = owner.getEyePosition().add(0, -0.5, 0);
                Vec3 subtract = hookPos.subtract(ownerPos);
                double length = subtract.length();
                for (int i = 0; i < length * 3; i++) {
                    ownerPos = ownerPos.add(subtract.scale(1 / (length * 3)));
                    this.level().addParticle(ParticleTypes.CRIT, ownerPos.x, ownerPos.y, ownerPos.z, 0, 0, 0);
                }
            }
        }

        if (this.entityData.get(HIT)) {
            this.entityData.set(HIT_EFFECT_TICK, this.entityData.get(HIT_EFFECT_TICK) + 1);
            if (this.getItem().is(Items.ENDER_EYE)) {
                this.setDeltaMovement(Vec3.ZERO);
                List<Entity> list = level().getEntities(this, new AABB(getX()-5,getY()-5,getZ()-5,getX()+5,getY()+5,getZ()+5));

                for (Entity e : list) {
                    double dx = 0.1/(getX()-e.getX()<0?Math.min(getX()-e.getX(),-1):Math.max(getX()-e.getX(),1));
                    double dy = 0.1/(getY()-e.getY()<0?Math.min(getY()-e.getY(),-1):Math.max(getY()-e.getY(),1));
                    double dz = 0.1/(getZ()-e.getZ()<0?Math.min(getZ()-e.getZ(),-1):Math.max(getZ()-e.getZ(),1));

                    e.push(dx,dy,dz);
                }
                if(this.level().isClientSide()){
                    for (int i = 0; i < 5; i++) {
                        this.level().addParticle(ParticleTypes.PORTAL,
                                this.getX() + this.getRandom().nextFloat() - 0.5F,
                                this.getY() + this.getRandom().nextFloat() - 1.0F,
                                this.getZ() + this.getRandom().nextFloat() - 0.5F,
                                this.getRandom().nextFloat() / 10,
                                this.getRandom().nextFloat() / 10,
                                this.getRandom().nextFloat() / 10);
                    }
                }
            } else if(this.getItem().is(ItemInit.GRAPPLING_HOOK)){
                this.setDeltaMovement(0,0,0);
                Entity owner = this.getOwner();
                if(owner == null || owner.isShiftKeyDown()){
                    this.kill();
                } else{
                    Vec3 hookPos = this.position();
                    Vec3 ownerPos = owner.getEyePosition().add(0, -0.5, 0);
                    Vec3 subtract = hookPos.subtract(ownerPos);
                    double length = subtract.length();
                    int lefttick = this.getLifeTimeAfterHit() - this.entityData.get(HIT_EFFECT_TICK);
                    length = Math.clamp(length, 0, 50);
                    double strength = 0.125 / length;
                    double control = 0.075;
                    double vx = subtract.x * strength + owner.getDirection().getStepX() * control;
                    double vy = subtract.y * strength + owner.getDirection().getStepY() * control + Math.max(owner.getGravity() * lefttick * 1.5 / getLifeTimeAfterHit(), owner.getGravity());
                    double vz = subtract.z * strength + owner.getDirection().getStepZ() * control;
                    owner.push(vx, vy, vz);
                    owner.fallDistance = 0;
                    LaunchersAndArrows.LOGGER.info(String.valueOf(length));
                    this.level().playSound(null, BlockPos.containing(owner.position()), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS,
                            1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.33F);
                    if (length < 2) {
                        this.kill();
                        this.level().playSound(null, this.blockPosition(), getHitSound(), SoundSource.PLAYERS,
                                1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.33F);
                    }
                }
            }
            if (this.entityData.get(HIT_EFFECT_TICK) >= getLifeTimeAfterHit()) {
                this.kill();

                if (level().isClientSide) {
                    ParticleOptions particle = getParticleParameters();

                    for (int i = 0; i < 8; i++) {
                        level().addParticle(particle,getX(),getY(),getZ(),0,0,0);
                    }
                }

                level().playSound(null, blockPosition(), getHitSound(), SoundSource.PLAYERS, 1F, 1F/(random.nextFloat()*0.5F+1.8F)+0.33F);
            }
        }
        super.tick();
    }

    private int getLifeTimeAfterHit() {
        if (getItem().is(Items.ENDER_EYE)) return 100;
        if (getItem().is(ItemInit.GRAPPLING_HOOK.get())) return 100;
        return 0;
    }

    // f: onEntityHit
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        float damage = 0;
        DamageSource source = damageSources().thrown(this,getOwner());

        if(getItem().is(Items.SLIME_BALL)) {
            if(entity instanceof LivingEntity livingEntity){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
            }
        }
        else if(getItem().is(Items.TORCH)){
            if(entity instanceof LivingEntity livingEntity){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 40, 0));
            }
            entity.setRemainingFireTicks(40);
        }
        else if(getItem().is(Items.GLOW_INK_SAC)){
            if(entity instanceof LivingEntity livingEntity){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 400, 0));
            }
        }
        else if(getItem().is(Items.INK_SAC)){
            if(entity instanceof LivingEntity livingEntity){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            }
        }
        else if (getItem().is(Items.AMETHYST_SHARD)) {
            damage = 1;
            source = damageSources().mobProjectile(this,(LivingEntity)getOwner());
            if (entity instanceof LivingEntity living) living.invulnerableTime = 0;
        }
        else if(getItem().is(Items.POINTED_DRIPSTONE)){
            damage = 3;
            source = damageSources().fallingStalactite(this.getOwner());
        }
        else if(getItem().is(Items.ECHO_SHARD)){
            if(entity instanceof LivingEntity livingEntity){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
            }
        }
        else if(getItem().is(Items.HEART_OF_THE_SEA)){
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        BlockPos pos = entity.blockPosition();
                        if (entity.level().getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).is(Blocks.AIR)) {
                            entity.level().setBlock(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k),
                                    Fluids.FLOWING_WATER.defaultFluidState().createLegacyBlock(), 3);
                        }
                    }
                }
            }
        }
        else if(getItem().is(Items.HEAVY_CORE)){
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.knockback(4, -this.getDeltaMovement().x, -this.getDeltaMovement().z);
                damage = 10;
                source = damageSources().mobProjectile(this,(LivingEntity)getOwner());
            }
            this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(Items.HEAVY_CORE)));
        }
        else if(getItem().getItem() instanceof BlockItem blockItem){
            Block block = blockItem.getBlock();
            damage = (float) Math.ceil(Math.sqrt(Math.max(block.defaultDestroyTime(), 0)) * 2);
        }
        level().playSound(null,entity.blockPosition(), getHitSound(), SoundSource.PLAYERS, 1F, 1F/(entity.getRandom().nextFloat()*0.5F+1.8F)+0.53F);
        entity.hurt(source,damage);
        if (entity instanceof LivingEntity living){
            // StatusEffectUtil.applyAttributeEffect(living,this.getItem());
        }
    }

    // f: onBlockHit
    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.getItem().is(Items.TORCH)) {
            BlockPos pos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            if (this.level().getBlockState(pos).is(Blocks.AIR)) {
                this.level().setBlock(pos, Blocks.TORCH.defaultBlockState(), 3);
            }
            else {
                int x = 0;
                int y = 0;
                int z = 0;
                switch (direction) {
                    case UP -> y = 1;
                    case NORTH -> z = -1;
                    case SOUTH -> z = 1;
                    case EAST -> x = 1;
                    case WEST -> x = -1;
                }
                pos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                if (this.level().getBlockState(pos).is(Blocks.AIR)) {
                    switch(direction) {
                        case UP -> this.level().setBlock(pos, Blocks.TORCH.defaultBlockState(), 3);
                        case NORTH -> this.level().setBlock(pos, Blocks.WALL_TORCH.defaultBlockState(), 3);
                        case SOUTH -> this.level().setBlock(pos, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, direction), 3);
                        case EAST -> this.level().setBlock(pos, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, direction), 3);
                        case WEST -> this.level().setBlock(pos, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, direction), 3);
                    }
                }
                else {
                    this.level().addFreshEntity(new ItemEntity(this.level(), pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.TORCH)));
                }
            }
        }
        else if (getItem().is(ItemInit.GRAPPLING_HOOK.get())){
            entityData.set(HIT, true);
        }
        else if(getItem().is(Items.HEART_OF_THE_SEA)){
            BlockPos pos = blockHitResult.getBlockPos();
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        BlockPos target = new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);

                        if (level().getBlockState(target).is(Blocks.AIR)) {
                            level().setBlock(target, Fluids.FLOWING_WATER.defaultFluidState().createLegacyBlock(), 3);
                        }
                    }
                }
            }
        }
        else if(this.getItem().getItem() instanceof BlockItem blockItem){
            Block block = blockItem.getBlock();
            BlockPos pos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            if (this.level().getBlockState(pos).is(Blocks.AIR)) {
                this.level().setBlock(pos, Blocks.HEAVY_CORE.defaultBlockState(), 3);
            }
            else {
                int x = 0;
                int y = 0;
                int z = 0;
                switch (direction) {
                    case UP -> y = 1;
                    case DOWN -> y = -1;
                    case NORTH -> z = -1;
                    case SOUTH -> z = 1;
                    case EAST -> x = 1;
                    case WEST -> x = -1;
                }
                pos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                if (this.level().getBlockState(pos).is(Blocks.AIR)) {
                    this.level().setBlock(pos, block.defaultBlockState(), 3);
                } else {
                    this.level().addFreshEntity(new ItemEntity(this.level(), pos.getX(), pos.getY(), pos.getZ(), new ItemStack(blockItem)));
                }
            }
        }

        level().playSound(null,blockHitResult.getBlockPos(), getHitSound(), SoundSource.PLAYERS,
                1F, 1F/(random.nextFloat()*0.5F+1.8F)+0.53F);
    }

    // f: onCollision
    @Override
    protected void onHit(HitResult hitResult) {

        if (getItem().is(Items.ENDER_EYE)){
            entityData.set(HIT, true);
            this.level().playSound(null, this.blockPosition(), SoundEvents.PORTAL_AMBIENT, SoundSource.PLAYERS,
                    1.0F, 1.0F  / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F);
        }
        else if (getItem().is(ItemInit.GRAPPLING_HOOK.get())){
            entityData.set(HIT, true);
        }
        else {
            this.kill();
        }
        super.onHit(hitResult);
    }

    // f: onBlockCollision
    @Override
    protected void onInsideBlock(BlockState state) {
        super.onInsideBlock(state);

        if (!level().isClientSide) {
            level().broadcastEntityEvent(this, (byte)3);
        }
    }

    private SoundEvent getHitSound() {
        if (getItem().is(Items.SLIME_BALL) || getItem().is(Items.MAGMA_CREAM))
            return SoundEvents.SLIME_JUMP;

        if (getItem().is(Items.TORCH))
            return SoundEvents.WOOD_BREAK;

        if (getItem().is(Items.GLOW_INK_SAC) || getItem().is(Items.INK_SAC))
            return SoundEvents.SQUID_SQUIRT;

        if (getItem().is(Items.AMETHYST_SHARD))
            return SoundEvents.AMETHYST_BLOCK_BREAK;

        if (getItem().is(Items.ENDER_EYE))
            return SoundEvents.ENDER_EYE_DEATH;

        if (getItem().is(Items.ECHO_SHARD))
            return SoundEvents.SCULK_SHRIEKER_SHRIEK;

        if (getItem().is(Items.HEART_OF_THE_SEA))
            return SoundEvents.CONDUIT_ACTIVATE;

        if (getItem().is(Items.HEAVY_CORE))
            return SoundEvents.HEAVY_CORE_BREAK;

        if (getItem().is(Items.POINTED_DRIPSTONE))
            return SoundEvents.DRIPSTONE_BLOCK_BREAK;

        if (getItem().is(ItemInit.GRAPPLING_HOOK.get()))
            return SoundEvents.IRON_TRAPDOOR_OPEN;

        if (getItem().getItem() instanceof BlockItem block)
            return block.getBlock().defaultBlockState().getSoundType().getPlaceSound();

        return SoundEvents.STONE_BREAK;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(HIT_EFFECT_TICK, 0);
        builder.define(HIT, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("HitEffectTick",entityData.get(HIT_EFFECT_TICK));
        tag.putBoolean("Hit",entityData.get(HIT));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(HIT_EFFECT_TICK,tag.getInt("HitEffectTick"));
        entityData.set(HIT,tag.getBoolean("Hit"));
    }
}
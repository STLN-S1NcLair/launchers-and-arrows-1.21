package net.stln.launchersandarrows.entity.projectile;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.stln.launchersandarrows.LaunchersAndArrows;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.entity.EntityInit;
import net.stln.launchersandarrows.item.ItemInit;
import net.stln.launchersandarrows.item.util.AttributeEffectsDictionary;
import net.stln.launchersandarrows.status_effect.StatusEffectInit;
import net.stln.launchersandarrows.status_effect.util.StatusEffectUtil;
import net.stln.launchersandarrows.util.AttributeEnum;

import java.util.List;

public class ItemProjectile extends ThrownItemEntity {

    private static final TrackedData<Integer> HIT_EFFECT_TICK = DataTracker.registerData(ItemProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> HIT = DataTracker.registerData(ItemProjectile.class, TrackedDataHandlerRegistry.BOOLEAN);

    public ItemProjectile(EntityType<ItemProjectile> entityType, World world) {
        super(entityType, world);
    }

    public ItemProjectile(EntityType<? extends ThrownItemEntity> entityType, World world, ItemStack stack) {
        super(entityType, world);
        this.setItem(stack);
    }

    public ItemProjectile(World world, LivingEntity owner, ItemStack stack) {
        super(EntityInit.ITEM_PROJECTILE, owner, world);
        this.setItem(stack);
    }

    public ItemProjectile(World world, LivingEntity owner, double x, double y, double z, ItemStack stack) {
        super(EntityInit.ITEM_PROJECTILE, x, y, z, world);
        this.setOwner(owner);
        this.setItem(stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }


    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getStack();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    public void handleStatus(byte status) {
        if (this.getDataTracker().get(HIT)) {
            status = 0;
        }
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    public void tick() {
        if (this.getStack().isOf(ItemInit.GRAPPLING_HOOK)) {
            Entity owner = this.getOwner();
            if (owner == null || owner.isSneaking()) {
                this.kill();
            } else {
                Vec3d hookPos = this.getPos();
                Vec3d ownerPos = owner.getEyePos().add(0, -0.5, 0);
                Vec3d subtract = hookPos.subtract(ownerPos);
                double length = subtract.length();
                for (int i = 0; i < length * 3; i++) {
                    ownerPos = ownerPos.add(subtract.multiply(1 / (length * 3)));
                    this.getWorld().addParticle(ParticleTypes.CRIT, ownerPos.x, ownerPos.y, ownerPos.z, 0, 0, 0);
                }
            }
        }
        if (this.getDataTracker().get(HIT)) {
            this.getDataTracker().set(HIT_EFFECT_TICK, this.getDataTracker().get(HIT_EFFECT_TICK) + 1);
            if (this.getStack().isOf(Items.ENDER_EYE)) {
                this.setVelocity(0, 0, 0);
                List<Entity> entityList = this.getWorld().getOtherEntities(this, new Box(this.getX() - 5, this.getY() - 5, this.getZ() - 5, this.getX() + 5, this.getY() + 5, this.getZ() + 5));
                for (Entity entity : entityList) {
                    double dx = 0.1 / (this.getX() - entity.getX() < 0 ? Math.min((this.getX() - entity.getX()), -1) : Math.max((this.getX() - entity.getX()), 1));
                    double dy = 0.1 / (this.getY() - entity.getY() < 0 ? Math.min((this.getY() - entity.getY()), -1) : Math.max((this.getY() - entity.getY()), 1));
                    double dz = 0.1 / (this.getZ() - entity.getZ() < 0 ? Math.min((this.getZ() - entity.getZ()), -1) : Math.max((this.getZ() - entity.getZ()), 1));
                    entity.addVelocity(dx, dy, dz);
                }
                if (this.getWorld().isClient()) {
                    for (int i = 0; i < 5; i++) {
                        this.getWorld().addParticle(ParticleTypes.PORTAL,
                                this.getX() + this.getRandom().nextFloat() - 0.5F,
                                this.getY() + this.getRandom().nextFloat() - 1.0F,
                                this.getZ() + this.getRandom().nextFloat() - 0.5F,
                                this.getRandom().nextFloat() / 10,
                                this.getRandom().nextFloat() / 10,
                                this.getRandom().nextFloat() / 10);
                    }
                }
            } else if (this.getStack().isOf(ItemInit.GRAPPLING_HOOK)) {
                this.setVelocity(0, 0, 0);
                Entity owner = this.getOwner();
                if (owner == null || owner.isSneaking()) {
                    this.kill();
                } else {
                    Vec3d hookPos = this.getPos();
                    Vec3d ownerPos = owner.getEyePos().add(0, -0.5, 0);
                    Vec3d subtract = hookPos.subtract(ownerPos);
                    double length = subtract.length();
                    int lefttick = this.getLifeTimeAfterHit() - this.getDataTracker().get(HIT_EFFECT_TICK);
                    length = Math.clamp(length, 0, 50);
                    double strength = 0.125 / length;
                    double control = 0.075;
                    double vx = subtract.x * strength + owner.getFacing().getOffsetX() * control;
                    double vy = subtract.y * strength + owner.getFacing().getOffsetY() * control + Math.max(owner.getFinalGravity() * lefttick * 1.5 / getLifeTimeAfterHit(), owner.getFinalGravity());
                    double vz = subtract.z * strength + owner.getFacing().getOffsetZ() * control;
                    owner.addVelocity(vx, vy, vz);
                    owner.fallDistance = 0;
                    LaunchersAndArrows.LOGGER.info(String.valueOf(length));
                    this.getWorld().playSound(null, BlockPos.ofFloored(owner.getPos()), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.PLAYERS,
                            1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.33F);
                    if (length < 2) {
                        this.kill();
                        this.getWorld().playSound(null, this.getBlockPos(), getHitSound(), SoundCategory.PLAYERS,
                                1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.33F);
                    }
                }
            }
            if (this.getDataTracker().get(HIT_EFFECT_TICK) >= getLifeTimeAfterHit()) {
                this.kill();
                if (getWorld().isClient) {
                    ParticleEffect particleEffect = this.getParticleParameters();
                    for (int i = 0; i < 8; ++i) {
                        this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
                    }
                }
                this.getWorld().playSound(null, this.getBlockPos(), getHitSound(), SoundCategory.PLAYERS,
                        1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.33F);
            }
        }
        super.tick();
    }

    private int getLifeTimeAfterHit() {
        if (this.getStack().isOf(Items.ENDER_EYE)) return 100;
        if (this.getStack().isOf(ItemInit.GRAPPLING_HOOK)) return 100;
        return 0;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        float damage = 0;
        DamageSource damageSource = this.getDamageSources().thrown(this, this.getOwner());
        if (this.getStack().isOf(Items.SLIME_BALL)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 2));
            }
        } else if (this.getStack().isOf(Items.TORCH)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 40, 0));
            }
            entity.setOnFireForTicks(40);
        } else if (this.getStack().isOf(Items.GLOW_INK_SAC)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 400, 0));
            }
        } else if (this.getStack().isOf(Items.INK_SAC)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
            }
        } else if (this.getStack().isOf(Items.AMETHYST_SHARD)) {
            damage = 1;
            damageSource = this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner());
            entity.timeUntilRegen = 0;
        } else if (this.getStack().isOf(Items.POINTED_DRIPSTONE)) {
            damage = 3;
            damageSource = this.getDamageSources().fallingStalactite(this.getOwner());
        } else if (this.getStack().isOf(Items.ECHO_SHARD)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0));
            }
        } else if (this.getStack().isOf(Items.HEART_OF_THE_SEA)) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        BlockPos pos = entity.getBlockPos();
                        if (entity.getWorld().getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).isOf(Blocks.AIR)) {
                            entity.getWorld().setBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k),
                                    Fluids.FLOWING_WATER.getDefaultState().getBlockState());
                        }
                    }
                }
            }
        } else if (this.getStack().isOf(Items.HEAVY_CORE)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.takeKnockback(4, -this.getVelocity().x, -this.getVelocity().z);
                damage = 10;
                damageSource = this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner());
            }
            this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(Items.HEAVY_CORE)));
        } else if (this.getStack().getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
                damage = (float) Math.ceil(Math.sqrt(Math.max(block.getHardness(), 0)) * 2);
                damageSource = this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner());
        }
        this.getWorld().playSound(null, entity.getBlockPos(), getHitSound(), SoundCategory.PLAYERS,
                1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F);
        entity.damage(damageSource, damage);
        if (entity instanceof LivingEntity livingEntity) {
            StatusEffectUtil.applyAttributeEffect(livingEntity, this.getStack());
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
            if (this.getStack().isOf(Items.TORCH)) {
                BlockPos pos = blockHitResult.getBlockPos();
                Direction direction = blockHitResult.getSide();
                if (this.getWorld().getBlockState(pos).isOf(Blocks.AIR)) {
                    this.getWorld().setBlockState(pos, Blocks.TORCH.getDefaultState());
                } else {
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
                    if (this.getWorld().getBlockState(pos).isOf(Blocks.AIR)) {
                        switch (direction) {
                            case UP -> this.getWorld().setBlockState(pos, Blocks.TORCH.getDefaultState());
                            case NORTH -> this.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState());
                            case SOUTH ->
                                    this.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.CLOCKWISE_180));
                            case EAST ->
                                    this.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.CLOCKWISE_90));
                            case WEST ->
                                    this.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.COUNTERCLOCKWISE_90));
                        }
                    } else {
                        this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.TORCH)));
                    }
                }
            } else if (this.getStack().isOf(ItemInit.GRAPPLING_HOOK)) {
                this.getDataTracker().set(HIT, true);

            } else if (this.getStack().isOf(Items.HEART_OF_THE_SEA)) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++) {
                            BlockPos pos = blockHitResult.getBlockPos();
                            if (this.getWorld().getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).isOf(Blocks.AIR)) {
                                this.getWorld().setBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k),
                                        Fluids.FLOWING_WATER.getDefaultState().getBlockState());
                            }
                        }
                    }
                }
            } else if (this.getStack().getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockPos pos = blockHitResult.getBlockPos();
                Direction direction = blockHitResult.getSide();
                if (this.getWorld().getBlockState(pos).isOf(Blocks.AIR)) {
                    this.getWorld().setBlockState(pos, Blocks.HEAVY_CORE.getDefaultState());
                } else {
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
                    if (this.getWorld().getBlockState(pos).isOf(Blocks.AIR)) {
                        this.getWorld().setBlockState(pos, block.getDefaultState());
                    } else {
                        this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), pos.getX(), pos.getY(), pos.getZ(), new ItemStack(blockItem)));
                    }
                }
            }
            this.getWorld().playSound(null, blockHitResult.getBlockPos(), getHitSound(), SoundCategory.PLAYERS,
                    1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F);

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (this.getStack().isOf(Items.ENDER_EYE)) {
            this.getDataTracker().set(HIT, true);
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.PLAYERS,
                    1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F);
        } else if (this.getStack().isOf(ItemInit.GRAPPLING_HOOK)) {
            this.getDataTracker().set(HIT, true);
        } else {
            this.kill();
        }
        super.onCollision(hitResult);
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
        }
    }

    private SoundEvent getHitSound() {
        if (this.getStack().isOf(Items.SLIME_BALL) || this.getStack().isOf(Items.MAGMA_CREAM)) {
            return SoundEvents.ENTITY_SLIME_JUMP;
        }
        if (this.getStack().isOf(Items.TORCH)) {
            return SoundEvents.BLOCK_WOOD_BREAK;
        }
        if (this.getStack().isOf(Items.GLOW_INK_SAC) || this.getStack().isOf(Items.INK_SAC)) {
            return SoundEvents.ENTITY_SQUID_SQUIRT;
        }
        if (this.getStack().isOf(Items.AMETHYST_SHARD)) {
            return SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK;
        }
        if (this.getStack().isOf(Items.ENDER_EYE)) {
            return SoundEvents.ENTITY_ENDER_EYE_DEATH;
        }
        if (this.getStack().isOf(Items.LIGHTNING_ROD)) {
            return SoundEvents.ITEM_TRIDENT_THUNDER.value();
        }
        if (this.getStack().isOf(Items.ECHO_SHARD)) {
            return SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK;
        }
        if (this.getStack().isOf(Items.HEART_OF_THE_SEA)) {
            return SoundEvents.BLOCK_CONDUIT_ACTIVATE;
        }
        if (this.getStack().isOf(Items.HEAVY_CORE)) {
            return SoundEvents.BLOCK_HEAVY_CORE_BREAK;
        }
        if (this.getStack().isOf(Items.POINTED_DRIPSTONE)) {
            return SoundEvents.BLOCK_DRIPSTONE_BLOCK_BREAK;
        }
        if (this.getStack().isOf(Items.POINTED_DRIPSTONE)) {
            return SoundEvents.BLOCK_DRIPSTONE_BLOCK_BREAK;
        }
        if (this.getStack().isOf(ItemInit.GRAPPLING_HOOK)) {
            return SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN;
        }
        if (this.getStack().getItem() instanceof BlockItem blockItem) {
            return blockItem.getBlock().getDefaultState().getSoundGroup().getPlaceSound();
        }
        return SoundEvents.BLOCK_STONE_BREAK;

    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HIT_EFFECT_TICK, 0);
        builder.add(HIT, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("HitEffectTick", this.getDataTracker().get(HIT_EFFECT_TICK));
        nbt.putBoolean("Hit", this.getDataTracker().get(HIT));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.getDataTracker().set(HIT_EFFECT_TICK, nbt.getInt("HitEffectTick"));
        this.getDataTracker().set(HIT, nbt.getBoolean("Hit"));
    }
}

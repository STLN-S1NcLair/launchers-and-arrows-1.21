package net.stln.launchersandarrows.entity.projectile;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.stln.launchersandarrows.entity.BypassDamageCooldownProjectile;
import net.stln.launchersandarrows.entity.EntityInit;

public class ItemProjectile extends ThrownItemEntity {

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


    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getStack();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

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
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 400, 0));
            }
        } else if (this.getStack().isOf(Items.AMETHYST_SHARD)) {
            damage = 1;
            damageSource = this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner());
            entity.timeUntilRegen = 0;
        }
        this.getWorld().playSound(null, entity.getBlockPos(), getHitSound(), SoundCategory.PLAYERS,
                1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F);
        entity.damage(damageSource, damage);
        this.kill();
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
                        case SOUTH -> this.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.CLOCKWISE_180));
                        case EAST -> this.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.CLOCKWISE_90));
                        case WEST -> this.getWorld().setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.COUNTERCLOCKWISE_90));
                    }
                } else {
                    this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.TORCH)));
                }
            }
        }
        this.getWorld().playSound(null, blockHitResult.getBlockPos(), getHitSound(), SoundCategory.PLAYERS,
                1.0F, 1.0F / (this.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F);
        this.kill();
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
        }
    }

    private SoundEvent getHitSound() {
        if (this.getStack().isOf(Items.SLIME_BALL)) {
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
        return null;
    }
}

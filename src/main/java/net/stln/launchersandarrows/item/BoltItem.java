package net.stln.launchersandarrows.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.entity.projectile.Bolt;

import javax.annotation.Nullable;

public class BoltItem extends ArrowItem {
    public BoltItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        Bolt bolt = new Bolt(level, shooter, ammo.copyWithCount(1), weapon);
        bolt.pickup = AbstractArrow.Pickup.DISALLOWED;
        return bolt;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        Bolt bolt = new Bolt(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        bolt.pickup = AbstractArrow.Pickup.DISALLOWED;
        return bolt;
    }
}

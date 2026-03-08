package net.stln.launchersandarrows.item.launcher;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.stln.launchersandarrows.item.ItemTagKeys;

import java.util.Optional;
import java.util.function.Predicate;

public class QuickBoltThrowerItem extends BoltThrowerItem{

    public static final Predicate<ItemStack> BOLT_THROWER_HELD_PROJECTILES = (stack) -> stack.is(ItemTagKeys.BOXED_BOLTS);

    private static final CrossbowItem.ChargingSounds DEFAULT_CHARGING_SOUNDS = new CrossbowItem.ChargingSounds(
            Optional.of(SoundEvents.CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.CROSSBOW_LOADING_END)
    );

    public QuickBoltThrowerItem(Properties properties) {
        super(properties);
        pulltime = 60;
        maxCount = 40;
        maxChargeCount = 10;
        chargeDelay = 2;
        shootDelay = 1;
        slotsize = 4;
    }

    @Override
    protected float getBoltVelocity(){
        return 7.5F;
    }

    @Override
    protected float getFirePitch(){
        return 1.5F;
    }
}

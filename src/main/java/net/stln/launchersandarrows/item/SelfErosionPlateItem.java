package net.stln.launchersandarrows.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.stln.launchersandarrows.item.bow.ModfiableBowItem;
import net.stln.launchersandarrows.item.component.ModComponentInit;

public class SelfErosionPlateItem extends Item {
    public SelfErosionPlateItem(Settings settings) {
        super(settings);
    }

    protected Class<?> targetItemClass;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack mainhandStack = user.getMainHandStack();
        ItemStack offhandStack = user.getOffHandStack();
        if (hand == Hand.MAIN_HAND && getCorrectTarget(offhandStack.getItem()) != null && user.isSneaking()) {
            offhandStack.set(ModComponentInit.SELF_REPAIR_COMPONENT, true);
            mainhandStack.setCount(mainhandStack.getCount() - 1);

            float h = 1.0F / (user.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
            user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_DISPENSER_FAIL, user.getSoundCategory(), 1.0F, h);
            user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_IRON_DOOR_OPEN, user.getSoundCategory(), 1.0F, h);
            user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, user.getSoundCategory(), 1.0F, h);
            return TypedActionResult.consume(user.getMainHandStack());
        }
        return super.use(world, user, hand);
    }

    protected Item getCorrectTarget(Item item) {
        if (item instanceof ModfiableBowItem) {
            return item;
        }
        return null;
    }
}

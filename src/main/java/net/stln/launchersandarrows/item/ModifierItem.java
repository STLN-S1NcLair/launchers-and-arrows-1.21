package net.stln.launchersandarrows.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;

public abstract class ModifierItem extends Item {
    public ModifierItem(Properties properties) {
        super(properties);
    }

    protected Class<?> targetItemClass;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        if(usedHand == InteractionHand.MAIN_HAND && getCorrectTarget(offHandStack.getItem()) != null & player.isShiftKeyDown()){
            ModifiableBowItem bow = (ModifiableBowItem)offHandStack.getItem();
            for(int i = 0; i < bow.getSlotsize(); i++){
                if(bow.getModifier(i, offHandStack) == null){
                    bow.setModifier(i, offHandStack, mainHandStack.copy());
                    mainHandStack.setCount(mainHandStack.getCount() -1);

                    float h = 1.0F / (player.getRandom().nextFloat() * 0.5F + 1.8F) + 0.53F;
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.DISPENSER_FAIL, player.getSoundSource(), 1.0F, h);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_DOOR_OPEN, player.getSoundSource(), 1.0F, h);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_DIAMOND, player.getSoundSource(), 1.0F, h);

                    return InteractionResultHolder.consume(player.getMainHandItem());
                }
            }
        }
        return super.use(level, player, usedHand);
    }

    protected abstract Item getCorrectTarget(Item item);
}

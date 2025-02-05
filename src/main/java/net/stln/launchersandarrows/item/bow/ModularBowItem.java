package net.stln.launchersandarrows.item.bow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.stln.launchersandarrows.item.FovModifierItem;
import net.stln.launchersandarrows.item.util.ModifierDictionary;
import net.stln.launchersandarrows.sound.SoundInit;
import net.stln.launchersandarrows.util.ModifierEnum;

import java.util.List;

public class ModularBowItem extends ModfiableBowItem implements FovModifierItem {

    float fov = 1.0f;

    public ModularBowItem(Settings settings) {
        super(settings);
        pulltime = 30;
        slotsize = 5;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound((Entity) user, user.getBlockPos(), SoundEvents.ITEM_CROSSBOW_LOADING_END.value(), SoundCategory.PLAYERS, 1f, 1.5f);
        world.playSound((Entity) user, user.getBlockPos(), SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE.value(), SoundCategory.PLAYERS, 1f, 1.0f);
        return super.use(world, user, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack = this.getProjectileTypeWithSelector(playerEntity, stack);
            if (!itemStack.isEmpty()) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                float f = getModifiedPullProgress(i, stack);
                if (!((double)f < 0.5)) {
                    List<ItemStack> list = load(stack, itemStack, playerEntity);
                    if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
                        this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, f * 2.0F, 1.0F, f == 1.0F, null);
                    }

                    world.playSound(
                            null,
                            playerEntity.getX(),
                            playerEntity.getY(),
                            playerEntity.getZ(),
                            SoundInit.BOW_RELEASE,
                            SoundCategory.PLAYERS,
                            1.5F,
                            1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public float getFov() {
        return this.fov;
    }

    @Override
    public void resetFov() {
        this.fov = 1.0F;
    }
}

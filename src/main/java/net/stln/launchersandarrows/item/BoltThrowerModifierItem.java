package net.stln.launchersandarrows.item;

import net.minecraft.world.item.Item;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;

public class BoltThrowerModifierItem extends ModifierItem{
    public BoltThrowerModifierItem(Properties properties){
        super(properties);
    }

    @Override
    protected Item getCorrectTarget(Item item) {
        if (item instanceof BoltThrowerItem) {
            return item;
        }
        return null;
    }
}

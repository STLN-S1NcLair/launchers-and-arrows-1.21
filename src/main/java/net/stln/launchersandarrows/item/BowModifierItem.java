package net.stln.launchersandarrows.item;

import net.minecraft.world.item.Item;
import net.stln.launchersandarrows.item.bow.ModifiableBowItem;
import net.stln.launchersandarrows.item.launcher.BoltThrowerItem;

public class BowModifierItem extends ModifierItem{
    public BowModifierItem(Properties properties){
        super(properties);
    }

    @Override
    protected Item getCorrectTarget(Item item){
        if(item instanceof ModifiableBowItem && !(item instanceof BoltThrowerItem)){
            return item;
        }
        return null;
    }
}

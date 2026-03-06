package net.stln.launchersandarrows.item;

import net.minecraft.world.item.Item;
import net.stln.launchersandarrows.item.util.AttributeModifierDictionary;

public class StringItem extends Item {
    public StringItem(Properties properties) {
        super(properties);
    }

    public int getAttributeMModifier(int id){
        return AttributeModifierDictionary.getAttributeEffect(this, id);
    }
}

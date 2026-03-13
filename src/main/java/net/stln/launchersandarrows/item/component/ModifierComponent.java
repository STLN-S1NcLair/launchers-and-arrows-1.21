package net.stln.launchersandarrows.item.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;

public final class ModifierComponent {
    public static final ModifierComponent EMPTY = new ModifierComponent(List.of());
    public static final Codec<ModifierComponent> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierComponent> STREAM_CODEC;
    private final List<ItemStack> modifiers;

    private ModifierComponent(List<ItemStack> modifiers) {
        this.modifiers = modifiers;
    }

    public static ModifierComponent of(ItemStack Modifier) {
        return new ModifierComponent(List.of(Modifier.copy()));
    }

    public static ModifierComponent of(List<ItemStack> Modifiers) {
        return new ModifierComponent(List.copyOf(Lists.transform(Modifiers, ItemStack::copy)));
    }

    public boolean contains(Item item) {
        Iterator var2 = this.modifiers.iterator();

        ItemStack itemStack;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            itemStack = (ItemStack)var2.next();
        } while(!itemStack.is(item));

        return true;
    }

    public List<ItemStack> getModifiers() {
        return Lists.transform(this.modifiers, ItemStack::copy);
    }

    public boolean isEmpty() {
        return this.modifiers.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            boolean var10000;
            if (o instanceof ModifierComponent) {
                ModifierComponent modifierComponent = (ModifierComponent)o;
                if (ItemStack.listMatches(this.modifiers, modifierComponent.modifiers)) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    public int hashCode() {
        return ItemStack.hashStackList(this.modifiers);
    }

    public String toString() {
        return "ChargedModifiers[items=" + String.valueOf(this.modifiers) + "]";
    }

    static {
        CODEC = ItemStack.CODEC.listOf().xmap(ModifierComponent::new, c -> c.modifiers);
        STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ModifierComponent::new, c -> c.modifiers);
    }
}

package net.stln.launchersandarrows.item.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public final class ChargeComponent {
    public static final ChargeComponent DEFAULT = new ChargeComponent(List.of(0.0, 0.0, 0.0));
    public static final Codec<ChargeComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, ChargeComponent> PACKET_CODEC;
    private final List<Double> charges;

    private ChargeComponent(List<Double> charges) {
        this.charges = charges;
    }

    public static ChargeComponent of(Double charge) {
        return new ChargeComponent(List.of(charge));
    }

    public static ChargeComponent of(List<Double> charges) {
        return new ChargeComponent(List.copyOf(charges));
    }

    public List<Double> getCharges() {
        return this.charges;
    }

    public boolean isEmpty() {
        return this.charges.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            boolean var10000;
            if (o instanceof ChargeComponent chargeComponent) {
                if (this.charges == chargeComponent.charges) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    public int hashCode() {
        return this.charges.hashCode();
    }

    public String toString() {
        return "Charges" + this.charges;
    }

    static {
        CODEC = Codec.list(Codec.DOUBLE)
                .xmap(
                        list -> list.stream()
                                .filter(value -> value != null && !Double.isNaN(value))
                                .collect(Collectors.toList()),
                        list -> list
                ).xmap(ChargeComponent::new, ChargeComponent::getCharges);
        PACKET_CODEC = PacketCodec.of(
                (component, buf) -> {
                    buf.writeVarInt(component.charges.size());
                    for (double charge : component.charges) {
                        buf.writeDouble(charge);
                    }
                },
                buf -> {
                    int size = buf.readVarInt();
                    List<Double> charges = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        charges.add(buf.readDouble());
                    }
                    return new ChargeComponent(charges);
                }
        );
    }
}

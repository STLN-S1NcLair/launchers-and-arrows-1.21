package net.stln.launchersandarrows.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChargeComponent {
    public static final ChargeComponent EMPTY = new ChargeComponent(List.of(0.0, 0.0, 0.0));
    public static final Codec<ChargeComponent> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ChargeComponent> STREAM_CODEC;
    private final List<Double> charges;

    private ChargeComponent(List<Double> charges){
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
        CODEC = Codec.list(Codec.DOUBLE).xmap(
                list -> list.stream().filter(value -> value != null && !Double.isNaN(value)).collect(Collectors.toList()),
                list -> list).xmap(ChargeComponent::new, ChargeComponent::getCharges);
        STREAM_CODEC = StreamCodec.of(
                (buf, component) -> {
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

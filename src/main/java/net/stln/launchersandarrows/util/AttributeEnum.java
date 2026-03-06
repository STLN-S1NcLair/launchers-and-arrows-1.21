package net.stln.launchersandarrows.util;

public enum AttributeEnum {
    FLAME(0),
    FROST(1),
    LIGHTNING(2),
    ACID(3),
    FLOOD(4),
    ECHO(5),
    INJURY(6),
    FLAME_RATIO(-1),
    FROST_RATIO(-2),
    LIGHTNING_RATIO(-3),
    ACID_RATIO(-4),
    FLOOD_RATIO(-5),
    ECHO_RATIO(-6),
    ;

    private final int id;

    private AttributeEnum(int i) {
        this.id = i;
    }

    public int get() {
        return this.id;
    }
}

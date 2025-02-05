package net.stln.launchersandarrows.util;

public enum ModifierEnum {
    RANGE(0),
    STURDY(1),
    LIGHTWEIGHT(2),
    CAPACITY(3),
    RICOCHET(4)
    ;

    private final int id;

    private ModifierEnum(int i) {
        this.id = i;
    }

    public int get() {
        return this.id;
    }
}

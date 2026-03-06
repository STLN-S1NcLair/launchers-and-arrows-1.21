package net.stln.launchersandarrows.entity;

public interface AttributedProjectile {
    default int getAttribute(int id) {
        return 0;
    }
    default Integer[] getAttributes() {
        return null;
    }
    default Integer[] getRatioAttributes() {
        return null;
    }

    default void setAttribute(int id, int amount) {

    }
}

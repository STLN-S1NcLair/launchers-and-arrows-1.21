package net.stln.launchersandarrows.entity;

public interface BypassDamageCooldownProjectile {
    default boolean getBypass() {
        return false;
    }

    default void setBypass(boolean flag) {

    }
}

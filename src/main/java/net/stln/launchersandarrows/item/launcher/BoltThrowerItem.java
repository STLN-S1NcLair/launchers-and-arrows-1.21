package net.stln.launchersandarrows.item.launcher;

import net.stln.launchersandarrows.item.bow.ModifiableBowItem;


public class BoltThrowerItem extends ModifiableBowItem {
    protected int maxCount = 60;
    protected int maxChargeCount = 20;
    protected int chargeDelay = 2;
    protected int shootDelay = 2;

    protected int shootCooldown = 0;

    protected boolean played0 = false;
    protected boolean played1 = false;
    protected boolean played2 = false;

    //

    public BoltThrowerItem(Properties properties) {
        super(properties);
    }
}

package sample.entity;

import java.io.Serializable;

public class WheelPointSave implements Serializable {
    private final String name;
    private final double multiplier;

    public WheelPointSave(WheelPoint wheelPoint) {
        this.name = wheelPoint.getName();
        this.multiplier = wheelPoint.getMultiplier();
    }

    public WheelPoint load(int id) {
        return new WheelPoint(id, name, multiplier);
    }

}

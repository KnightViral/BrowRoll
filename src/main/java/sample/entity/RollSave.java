package sample.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RollSave implements Serializable {

    private final String name;
    private final List<WheelPointSave> list;
    private final Date date;

    public RollSave(String name, List<WheelPointSave> list ) {
        this.name = name;
        this.list = list;
        this.date = new Date();
    }

    public String getName() {
        return name;
    }

    public List<WheelPointSave> getList() {
        return list;
    }

    public Date getDate() {
        return date;
    }
}

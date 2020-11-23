package sample.entity;

public class WheelPoint {
    private int id;
    private String name;
    private int multiplier;

    public WheelPoint(int id, String name, int multiplier){
        this.id = id;
        this.name = name;
        this.multiplier = multiplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public void addMultiplier(){
        multiplier = multiplier + 1;
    }

    public void decreaseMultiplier(){
        multiplier = multiplier - 1;
    }
}

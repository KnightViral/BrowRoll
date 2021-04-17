package sample.entity;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

public class WheelPoint {
    private int id;
    private String name;
    private double multiplier;
    private PieChart.Data wheelData;

    private static final int WHEEL_DATA_TEXT_LENGTH = 15;

    public WheelPoint(int id, String name, double multiplier){
        this.id = id;
        this.name = name;
        this.multiplier = multiplier;
        generateWheelData();
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
        wheelData.setName(shortenWheelName(name));
        updateWheelTooltip();
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
        wheelData.setPieValue(multiplier);
        updateWheelTooltip();
    }

    public PieChart.Data getWheelData() {
        return wheelData;
    }

    public void setWheelData(PieChart.Data wheelData) {
        this.wheelData = wheelData;
    }

    private String shortenWheelName(String name){
        if (name.length() > WHEEL_DATA_TEXT_LENGTH - 1)
            return name.substring(0, WHEEL_DATA_TEXT_LENGTH) + "...";
        else
            return name;
    }

    public void updateWheelTooltip(){
        setWheelTooltip(name + " " + multiplier);
    }

    public void setWheelTooltip(String tooltipText){
        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(wheelData.getNode(), tooltip);
    }

    public void generateWheelData(){
        this.wheelData = new PieChart.Data(shortenWheelName(name), multiplier);
    }

    public WheelPointSave save() {
        return new WheelPointSave(this);
    }
}

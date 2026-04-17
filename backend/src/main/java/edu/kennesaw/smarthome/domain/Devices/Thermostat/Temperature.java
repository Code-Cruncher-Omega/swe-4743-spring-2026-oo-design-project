package edu.kennesaw.smarthome.domain.Devices.Thermostat;

public class Temperature {
    private int value;
    private String unit; // "F" for Fahrenheit, "C" for Celsius

    public Temperature(int value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public void incrementValue() {
        this.value++;
    }

    public void decrementValue() {
        this.value--;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

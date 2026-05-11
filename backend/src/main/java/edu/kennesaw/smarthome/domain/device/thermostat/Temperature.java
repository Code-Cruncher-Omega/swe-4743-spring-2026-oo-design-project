package edu.kennesaw.smarthome.domain.device.thermostat;

// Temperature acts mainly as a values class. It does not perform much logic.
public class Temperature {
    private String unit;    // Farenheit, Celsius, Kelvin.
    private int value;

    public Temperature(String unit, int value) {
        this.unit = unit;
        this.value = value;
    }

    public Temperature(int value) {
        this.unit = "Farenheit";
        this.value = value;
    }

    protected void decrease(int amount) {
        this.value -= amount;
    }

    protected void decrease() {
        this.value--;
    }

    protected void increase(int amount) {
        this.value += amount;
    }

    protected void increase() {
        this.value++;
    }

    protected void setUnit(String unit) {
        this.unit = unit;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}

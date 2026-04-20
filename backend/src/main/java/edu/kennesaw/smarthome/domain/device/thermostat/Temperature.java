package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

@Component("temperature")
public class Temperature {
    private String unit;    // Farenheit, Celsius, Kelvin.
    private int value;

    public Temperature(String unit, int value) {
        this.unit = unit;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public void decrease(int amount) {
        this.value -= amount;
    }

    public void decrease() {
        this.value--;
    }

    public void increase(int amount) {
        this.value += amount;
    }

    public void increase() {
        this.value++;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

package edu.kennesaw.smarthome.domain.Devices.Thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Device;

@Component
public class Thermostat extends Device {
    // Thermostat-specific variables
    private String mode; // Values: "Heat", "Cool", "Auto" only
    private Temperature desiredTemperature; // Values: 60 - 80 Fahrenheit only
    private ThermostatState state;
    public enum ThermostatAction {TURN_ON, TURN_OFF, SET_IDLE, SET_HEATING, SET_COOLING};  // All state transition actions

    // Constructor
    public Thermostat(String name, String location, String initialMode, Temperature initialDesiredTemp, Temperature initialAmbientTemp) {
        super(name, location, Type.THERMOSTAT);
        this.mode = initialMode;
        this.desiredTemperature = initialDesiredTemp;
    }

    // Methods //

    // Pass action onto the state object to handle and return the result
    private ActionResult execute(ThermostatAction action) {
        return state.execute(action, this);
    }

    // Allow state objects to transition the context's state object to another
    protected void setState(ThermostatState newState) {
        this.state = newState;
    }

    // Simplify state changing methods for external callers by directly exposing the actions as methods
    public ActionResult changeMode(String newMode) {
        switch (newMode.toUpperCase()) {
            case "HEAT":
                mode = "Heat";
                return new ActionResult(true, "SET_MODE_HEAT", "Thermostat mode set to Heat successfully.");
            case "COOL":
                mode = "Cool";
                return new ActionResult(true, "SET_MODE_COOL", "Thermostat mode set to Cool successfully.");
            case "AUTO":
                mode = "Auto";
                return new ActionResult(true, "SET_MODE_AUTO", "Thermostat mode set to Auto successfully.");
            default:
                return new ActionResult(false, "SET_MODE_" + newMode.toUpperCase(), "Invalid mode. Valid modes are HEAT, COOL, and AUTO.");
        }
    }

    public ActionResult setDesiredTemperature(int newTemp) {
        if (newTemp < 60 || newTemp > 80) {
            return new ActionResult(false, "SET_DESIRED_TEMPERATURE", "Invalid temperature. Desired temperature must be between 60 and 80 Fahrenheit.");
        }
        this.desiredTemperature.setValue(newTemp);
        return new ActionResult(true, "SET_DESIRED_TEMPERATURE", String.format("Desired temperature set to %d°F successfully.", newTemp));
    }

    public ActionResult incrementAmbientTemperature(Temperature ambientTemperature) {
        ambientTemperature.incrementValue();
        return new ActionResult(true, "INCREMENT_AMBIENT_TEMPERATURE", "Ambient temperature incremented successfully.");
    }

    public ActionResult decrementAmbientTemperature(Temperature ambientTemperature) {
        ambientTemperature.decrementValue();
        return new ActionResult(true, "DECREMENT_AMBIENT_TEMPERATURE", "Ambient temperature decremented successfully.");
    }
    
    public ActionResult turnOn() {
        return execute(ThermostatAction.TURN_ON);
    }

    public ActionResult turnOff() {
        return execute(ThermostatAction.TURN_OFF);
    }

    public String getStateName() {
        return state.getName();
    }

    public String getMode() {
        return mode;
    }

    public Temperature getDesiredTemperature() {
        return desiredTemperature;
    }
}
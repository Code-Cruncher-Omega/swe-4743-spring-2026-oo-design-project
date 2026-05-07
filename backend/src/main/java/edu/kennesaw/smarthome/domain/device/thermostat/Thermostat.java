package edu.kennesaw.smarthome.domain.device.thermostat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.service.creator.ThermostatCreator;
import edu.kennesaw.smarthome.service.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.service.dto.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.abstraction.UpdateableDevice;

public class Thermostat extends Device<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> 
                        implements UpdateableDevice {
    
    // Concrete ThermostatMode class objects change AMBIENT_TEMPERATURE in their own unique way.
    private final Map<String, ThermostatMode> MODES;

    private ThermostatMode currentMode; // HEAT, COOL, AUTO.
    // Only the values of desired and ambient temperatures ever change, not the address (aka location) of the temperatures.
    private final Temperature DESIRED_TEMPERATURE; // Between 60 and 80 degrees Farenheit.
    private final Temperature AMBIENT_TEMPERATURE;  // The temperature of the environment the thermostat is in.

    public Thermostat(  UUID id,
                        String name, 
                        String location, 
                        ThermostatState savedState,
                        Map<String, ThermostatState> states,

                        ThermostatMode savedMode, 
                        Temperature savedDesiredTemperature,
                        Temperature savedAmbientTemperature, 

                        Map<String, ThermostatMode> modes) {
        super(id, name, location, savedState, states);

        this.MODES = modes;

        this.currentMode = savedMode;

        this.DESIRED_TEMPERATURE = savedDesiredTemperature;
        this.AMBIENT_TEMPERATURE = savedAmbientTemperature;
    }

    public Thermostat(  String name, 
                        String location, 
                        ThermostatState initialState,
                        Map<String, ThermostatState> states,

                        ThermostatMode initialMode, 
                        Temperature initialDesiredTemperature,
                        Temperature ambientTemperature, 

                        Map<String, ThermostatMode> modes) {
        super(name, location, initialState, states);

        this.MODES = modes;

        this.currentMode = initialMode;

        this.DESIRED_TEMPERATURE = initialDesiredTemperature;
        this.AMBIENT_TEMPERATURE = ambientTemperature;
    }

    @Override
    // Delegate the action execution to the current state of the thermostat, allowing for state-specific behavior.
    protected DeviceResult execute(ThermostatAction action) {
        return state.execute(this, action);
    }

    protected ThermostatState getOffState() {
        return STATES.get(ThermostatStateType.OFF.toString());
    }

    protected ThermostatState getIdleState() {
        return STATES.get(ThermostatStateType.IDLE.toString());
    }

    protected ThermostatState getHeatingState() {
        return STATES.get(ThermostatStateType.HEATING.toString());
    }

    protected ThermostatState getCoolingState() {
        return STATES.get(ThermostatStateType.COOLING.toString());
    }

    protected ThermostatMode getCurrentMode() {
        return currentMode;
    }

    @Override
    protected void setState(ThermostatState newState) {
        state = newState;
    }

    @Override
    public DeviceResult performAction(DeviceActionRequest action) {
        switch(action.action()) {
            case "TOGGLE_POWER":    // ThermostatAction.TOGGLE_POWER
                return togglePower();
            case "SET_AMBIENCE":
                return setAmbientTemperature((int) action.parameters()[0]);
            case "SET_DESIRED":
                return setDesiredTemperature((int) action.parameters()[0]);
            case "SET_MODE_HEAT":
                return setModeHeat();
            case "SET_MODE_COOL":
                return setModeCool();
            case "SET_MODE_AUTO":
                return setModeAuto();
            default:
                return new DeviceResult(false, action.action().toString(), "Cannot perform " + action.action().toString() + " with " + getName());
        }
    }

    @Override
    public DeviceType getType() {
        return DeviceType.THERMOSTAT;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("mode", currentMode.getModeType().toString());
        attributes.put("desired", DESIRED_TEMPERATURE.getValue() + "");
        attributes.put("ambient", AMBIENT_TEMPERATURE.getValue() + "");
        return attributes;
    }

    public Temperature getDesiredTemperature() {
        return DESIRED_TEMPERATURE;
    }

    public Temperature getAmbientTemperature() {
        return AMBIENT_TEMPERATURE;
    }

    @Override
    public DeviceResult reset() {
        state = STATES.get(ThermostatCreator.initialState()); // Reset to the initial state.
        currentMode = MODES.get(ThermostatCreator.initialMode()); // Reset mode to the initial mode.
        DESIRED_TEMPERATURE.setValue(ThermostatCreator.initialDesiredTemperature().getValue()); // Reset desired temperature to the initial value.
        return new DeviceResult(true, "RESET_THERMOSTAT", getName() + " reset to initial state.");
    }

    @Override
    public DeviceResult update(int tickRate) {
        DeviceResult result = state.execute(this, ThermostatAction.UPDATE_STATE);
        // Checking for STILL prevents warming or cooling in the same update where thermostat changes state.
        if(result.success() && result.action().contains("STILL")) {
            return state.execute(this, ThermostatAction.UPDATE_AMBIENCE, tickRate);
        }
        return result;
    }

    public DeviceResult togglePower() {
        return state.execute(this, ThermostatAction.TOGGLE_POWER);
    }

    public DeviceResult setModeHeat() {
        currentMode = MODES.get(ThermostatModeType.HEAT.toString());
        return new DeviceResult(true, "SET_MODE_HEAT", getName() + " mode set to heat.");
    }

    public DeviceResult setModeCool() {
        currentMode = MODES.get(ThermostatModeType.COOL.toString());
        return new DeviceResult(true, "SET_MODE_COOL", getName() + " mode set to cool.");
    }

    public DeviceResult setModeAuto() {
        currentMode = MODES.get(ThermostatModeType.AUTO.toString());
        return new DeviceResult(true, "SET_MODE_AUTO", getName() + " mode set to auto.");
    }
    
    public DeviceResult setDesiredTemperature(int newTemperature) {
        if (newTemperature < 60 || newTemperature > 80) {
            return new DeviceResult(false, "SET_DESIRED_TEMPERATURE", "Desired temperature must be between 60 and 80 degrees Farenheit.");
        }
        DESIRED_TEMPERATURE.setValue(newTemperature);
        return new DeviceResult(true, "SET_DESIRED_TEMPERATURE", getName() + " desired temperature set to " + newTemperature + " degrees Farenheit.");
    }

    public DeviceResult setAmbientTemperature(int newTemperature) {
        AMBIENT_TEMPERATURE.setValue(newTemperature);
        return new DeviceResult(true, "SET_AMBIENT_TEMPERATURE", getName() + "'s ambient temperature set to " + newTemperature + " degrees Farenheit.");
    }
}

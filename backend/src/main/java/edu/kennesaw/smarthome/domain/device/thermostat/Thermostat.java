package edu.kennesaw.smarthome.domain.device.thermostat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.service.creator.ThermostatCreator;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.abstraction.UpdateableDevice;
import edu.kennesaw.smarthome.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.dto.DeviceResult;

public class Thermostat extends Device<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> 
                        implements UpdateableDevice {
    
    // Concrete ThermostatMode class objects change AMBIENT_TEMPERATURE in their own unique way.
    private final Map<ThermostatModeType, ThermostatMode> MODES;

    private ThermostatMode currentMode; // HEAT, COOL, AUTO.
    // Only the values of desired and ambient temperatures ever change, not the address (aka location) of the temperatures.
    private final Temperature DESIRED_TEMPERATURE; // Between 60 and 80 degrees Farenheit.
    private final Temperature AMBIENT_TEMPERATURE;  // The temperature of the environment the thermostat is in.

    public Thermostat(  UUID id,
                        String name, 
                        String location, 
                        ThermostatState savedState,
                        Map<ThermostatStateType, ThermostatState> states,

                        ThermostatMode savedMode, 
                        Temperature savedDesiredTemperature,
                        Temperature savedAmbientTemperature, 

                        Map<ThermostatModeType, ThermostatMode> modes) {
        super(id, name, location, savedState, states);

        this.MODES = modes;

        this.currentMode = savedMode;

        this.DESIRED_TEMPERATURE = savedDesiredTemperature;
        this.AMBIENT_TEMPERATURE = savedAmbientTemperature;
    }

    public Thermostat(  String name, 
                        String location, 
                        ThermostatState initialState,
                        Map<ThermostatStateType, ThermostatState> states,

                        ThermostatMode initialMode, 
                        Temperature initialDesiredTemperature,
                        Temperature ambientTemperature, 

                        Map<ThermostatModeType, ThermostatMode> modes) {
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
        return STATES.get(ThermostatStateType.OFF);
    }

    protected ThermostatState getIdleState() {
        return STATES.get(ThermostatStateType.IDLE);
    }

    protected ThermostatState getHeatingState() {
        return STATES.get(ThermostatStateType.HEATING);
    }

    protected ThermostatState getCoolingState() {
        return STATES.get(ThermostatStateType.COOLING);
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
        ThermostatAction thermostatAction = ThermostatAction.from(action.action());
        switch (thermostatAction) {
            case SET_DESIRED:
                int newDesiredTemperature = (int) action.parameters()[0];
                return setDesiredTemperature(newDesiredTemperature);
            case SET_AMBIENCE:
                int newAmbientTemperature = (int) action.parameters()[0];
                return setAmbientTemperature(newAmbientTemperature);
            case SET_MODE_HEAT:
                return setModeHeat();
            case SET_MODE_COOL:
                return setModeCool();
            case SET_MODE_AUTO:
                return setModeAuto();
            default:
                return execute(thermostatAction);
        }
    }

    @Override
    public DeviceType getType() {
        return DeviceType.THERMOSTAT;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("mode", currentMode.getModeType());
        attributes.put("desired", DESIRED_TEMPERATURE.getValue());
        attributes.put("ambient", AMBIENT_TEMPERATURE.getValue());
        return attributes;
    }

    public Temperature getDesiredTemperature() {
        return DESIRED_TEMPERATURE;
    }

    public Temperature getAmbientTemperature() {
        return AMBIENT_TEMPERATURE;
    }

    @Override
    public void reset() {
        state = STATES.get(ThermostatCreator.initialState()); // Reset to the initial state.
        currentMode = MODES.get(ThermostatCreator.initialMode()); // Reset mode to the initial mode.
        DESIRED_TEMPERATURE.setValue(ThermostatCreator.initialDesiredTemperature().getValue()); // Reset desired temperature to the initial value.
    }

    @Override
    public DeviceResult update(int tickRate) {
        DeviceResult result = state.execute(this, ThermostatAction.UPDATE_STATE);
        // Checking for STILL prevents warming or cooling in the same update where thermostat changes state.
        if(result.success() && result.action().equals(ThermostatAction.STILL_IN_SAME_STATE)) {
            return state.execute(this, ThermostatAction.UPDATE_AMBIENCE, tickRate);
        }
        return result;
    }

    public DeviceResult togglePower() {
        return state.execute(this, ThermostatAction.TOGGLE_POWER);
    }

    public DeviceResult setModeHeat() {
        currentMode = MODES.get(ThermostatModeType.HEAT);
        return new DeviceResult(true, ThermostatAction.SET_MODE_HEAT, getName() + " mode set to heat.");
    }

    public DeviceResult setModeCool() {
        currentMode = MODES.get(ThermostatModeType.COOL);
        return new DeviceResult(true, ThermostatAction.SET_MODE_COOL, getName() + " mode set to cool.");
    }

    public DeviceResult setModeAuto() {
        currentMode = MODES.get(ThermostatModeType.AUTO);
        return new DeviceResult(true, ThermostatAction.SET_MODE_AUTO, getName() + " mode set to auto.");
    }
    
    public DeviceResult setDesiredTemperature(int newTemperature) {
        if (newTemperature < 60 || newTemperature > 80) {
            return new DeviceResult(false, ThermostatAction.SET_DESIRED, "Desired temperature must be between 60 and 80 degrees Farenheit.");
        }
        DESIRED_TEMPERATURE.setValue(newTemperature);
        return new DeviceResult(true, ThermostatAction.SET_DESIRED, getName() + " desired temperature set to " + newTemperature + " degrees Farenheit.");
    }

    public DeviceResult setAmbientTemperature(int newTemperature) {
        AMBIENT_TEMPERATURE.setValue(newTemperature);
        return new DeviceResult(true, ThermostatAction.SET_AMBIENCE, getName() + "'s ambient temperature set to " + newTemperature + " degrees Farenheit.");
    }
}

package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

@Component("thermostat")
public class Thermostat extends Device<Thermostat, ThermostatState, ThermostatAction> {
    
    private final ThermostatState INITIAL_STATE;
    private final ThermostatMode INITIAL_MODE;
    // DESIRED_TEMPERATURE is resetted by its values, hence storing its initial values.
    private final String INITIAL_DESIRED_TEMPERATURE_UNIT;
    private final int INITIAL_DESIRED_TEMPERATURE_VALUE;
    // AMBIENT_TEMPERATURE does not get reset, so no initial values need to be stored for the object.

    private final ThermostatState OFF_STATE;
    private final ThermostatState IDLE_STATE;
    private final ThermostatState HEATING_STATE;
    private final ThermostatState COOLING_STATE;

    // ThermostatMode change AMBIENT_TEMPERATURE in their own unique way.
    private final ThermostatMode HEAT_MODE;
    private final ThermostatMode COOL_MODE;
    private final ThermostatMode AUTO_MODE;

    private ThermostatMode currentMode; // HEAT, COOL, AUTO.
    private final Temperature DESIRED_TEMPERATURE; // Between 60 and 80 degrees Farenheit.
    private final Temperature AMBIENT_TEMPERATURE;  // The environment a thermostat is in does not change, so it's final.

    public Thermostat(  String name, 
                        String location, 
                        ThermostatState initialState, 
                        ThermostatMode initialMode, 
                        Temperature initialDesiredTemperature,
                        Temperature ambientTemperature, 
                        ThermostatState offState, 
                        ThermostatState idleState, 
                        ThermostatState heatingState, 
                        ThermostatState coolingState,
                        ThermostatMode heatMode,
                        ThermostatMode coolMode,
                        ThermostatMode autoMode) {
        super(name, location, initialState);

        this.INITIAL_STATE = initialState;
        this.INITIAL_MODE = initialMode;
        this.INITIAL_DESIRED_TEMPERATURE_UNIT = initialDesiredTemperature.getUnit();
        this.INITIAL_DESIRED_TEMPERATURE_VALUE = initialDesiredTemperature.getValue();

        this.OFF_STATE = offState;
        this.IDLE_STATE = idleState;
        this.HEATING_STATE = heatingState;
        this.COOLING_STATE = coolingState;

        this.HEAT_MODE = heatMode;
        this.COOL_MODE = coolMode;
        this.AUTO_MODE = autoMode;

        this.currentMode = initialMode;
        this.DESIRED_TEMPERATURE = initialDesiredTemperature;
        this.AMBIENT_TEMPERATURE = ambientTemperature;
    }

    @Override
    protected ActionResult execute(ThermostatAction action) {
        return state.execute(this, action);
    }

    protected ThermostatState getOffState() {
        return OFF_STATE;
    }

    protected ThermostatState getIdleState() {
        return IDLE_STATE;
    }

    protected ThermostatState getHeatingState() {
        return HEATING_STATE;
    }

    protected ThermostatState getCoolingState() {
        return COOLING_STATE;
    }

    protected ThermostatMode getCurrentMode() {
        return currentMode;
    }

    protected Temperature getDesiredTemperature() {
        return DESIRED_TEMPERATURE;
    }

    protected Temperature getAmbientTemperature() {
        return AMBIENT_TEMPERATURE;
    }

    @Override
    protected void setState(ThermostatState newState) {
        state = newState;
    }

    @Override
    public DeviceType getType() {
        return DeviceType.THERMOSTAT;
    }

    @Override
    public ActionResult reset() {
        state = INITIAL_STATE; // Reset to the initial state.
        currentMode = INITIAL_MODE; // Reset mode to the initial mode.
        DESIRED_TEMPERATURE.setUnit(INITIAL_DESIRED_TEMPERATURE_UNIT);   // Reset desired temperature unit to the initial unit.
        DESIRED_TEMPERATURE.setValue(INITIAL_DESIRED_TEMPERATURE_VALUE); // Reset desired temperature to the initial value.
        return new ActionResult(true, "RESET_THERMOSTAT", "Thermostat reset to initial state, mode, and desired temperature.");
    }

    public ActionResult updateAmbientTemperature() {
        return state.execute(this, ThermostatAction.UPDATE_AMBIENCE);
    }

    public ActionResult togglePower() {
        return state.execute(this, ThermostatAction.TOGGLE_POWER);
    }

    public ActionResult setModeHeat() {
        currentMode = HEAT_MODE;
        return new ActionResult(true, "SET_MODE_HEAT", "Thermostat mode set to HEAT.");
    }

    public ActionResult setModeCool() {
        currentMode = COOL_MODE;
        return new ActionResult(true, "SET_MODE_COOL", "Thermostat mode set to COOL.");
    }

    public ActionResult setModeAuto() {
        currentMode = AUTO_MODE;
        return new ActionResult(true, "SET_MODE_AUTO", "Thermostat mode set to AUTO.");
    }
    
    public ActionResult setDesiredTemperature(int newTemperature) {
        if (newTemperature < 60 || newTemperature > 80) {
            return new ActionResult(false, "SET_DESIRED_TEMPERATURE", "Desired temperature must be between 60 and 80 degrees Farenheit.");
        }
        DESIRED_TEMPERATURE.setValue(newTemperature);
        return new ActionResult(true, "SET_DESIRED_TEMPERATURE", "Desired temperature set to " + newTemperature + " degrees Farenheit.");
    }
}

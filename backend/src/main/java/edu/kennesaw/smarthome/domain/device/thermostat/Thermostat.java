package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class Thermostat extends Device<Thermostat, ThermostatState, ThermostatAction> {
    
    private final ThermostatState INITIAL_STATE;
    private final ThermostatMode INITIAL_MODE;
    private final String INITIAL_DESIRED_TEMPERATURE_UNIT;
    private final int INITIAL_DESIRED_TEMPERATURE_VALUE;
    // No INITIAL_AMBIENT_TEMPERATURE since it will be passed in the constructor.
    private final ThermostatState OFF_STATE;
    private final ThermostatState IDLE_STATE;
    private final ThermostatState HEATING_STATE;
    private final ThermostatState COOLING_STATE;

    private ThermostatMode currentMode; // HEAT, COOL, AUTO.
    private Temperature desiredTemperature; // Between 60 and 80 degrees Farenheit.
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
                        ThermostatState coolingState) {
        super(name, location, initialState);
        this.INITIAL_STATE = initialState;
        this.INITIAL_MODE = initialMode;
        this.INITIAL_DESIRED_TEMPERATURE_UNIT = initialDesiredTemperature.getUnit();
        this.INITIAL_DESIRED_TEMPERATURE_VALUE = initialDesiredTemperature.getValue();
        this.OFF_STATE = offState;
        this.IDLE_STATE = idleState;
        this.HEATING_STATE = heatingState;
        this.COOLING_STATE = coolingState;
        this.currentMode = initialMode;
        this.desiredTemperature = initialDesiredTemperature;
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
        return desiredTemperature;
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
        desiredTemperature.setUnit(INITIAL_DESIRED_TEMPERATURE_UNIT);   // Reset desired temperature unit to the initial unit.
        desiredTemperature.setValue(INITIAL_DESIRED_TEMPERATURE_VALUE); // Reset desired temperature to the initial value.
        return new ActionResult(true, "RESET_THERMOSTAT", "Thermostat reset to initial state, mode, and desired temperature.");
    }

    public ActionResult turnOn() {
        return state.execute(this, ThermostatAction.SET_IDLE);
    }

    public ActionResult turnOff() {
        return state.execute(this, ThermostatAction.TURN_OFF);
    }

    public ActionResult setModeHeat() {
        currentMode = ThermostatMode.HEAT;
        return new ActionResult(true, "SET_MODE_HEAT", "Thermostat mode set to HEAT.");
    }

    public ActionResult setModeCool() {
        currentMode = ThermostatMode.COOL;
        return new ActionResult(true, "SET_MODE_COOL", "Thermostat mode set to COOL.");
    }

    public ActionResult setModeAuto() {
        currentMode = ThermostatMode.AUTO;
        return new ActionResult(true, "SET_MODE_AUTO", "Thermostat mode set to AUTO.");
    }

    public ActionResult setDesiredTemperature(int newTemperature) {
        if (newTemperature < 60 || newTemperature > 80) {
            return new ActionResult(false, "SET_DESIRED_TEMPERATURE", "Desired temperature must be between 60 and 80 degrees Farenheit.");
        }
        desiredTemperature.setValue(newTemperature);
        return new ActionResult(true, "SET_DESIRED_TEMPERATURE", "Desired temperature set to " + newTemperature + " degrees Farenheit.");
    }

    // Mode = HEAT and ambient < desired -> increment ambient temp by 1 degree.
    // Mode = COOL and ambient > desired -> decrement ambient temp by 1 degree.
    // Mode = AUTO and ambient != desired -> increment or decrement ambient temp by 1 degree.
    // Ambient temperature == desired temperature -> do not change ambient temperature.
    public ActionResult updateAmbientTemperature() {
        return state.updateAmbientTemperature(this);
    }
}

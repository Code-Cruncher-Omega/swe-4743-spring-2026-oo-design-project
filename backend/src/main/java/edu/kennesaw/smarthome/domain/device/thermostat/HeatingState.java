package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

public class HeatingState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        context.getAmbientTemperature().increase(); // Ambient temperature increases before entering another state (heating occurs during this state).

        switch (action) {
            case TOGGLE_POWER:
                context.getAmbientTemperature().decrease(); // State was interrupted, so the thermostat never finished heating.
                context.setState(context.getOffState());
                return new DeviceResult(true, "TOGGLE_THERMOSTAT_POWER", "Thermostat turned off.");
            case UPDATE_AMBIENCE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.updateAmbientTemperature(context);
                ThermostatStateType newStateType = transitionState.getStateType();
                if(newStateType.equals(getStateType())) {
                    return new DeviceResult(true, "THERMOSTAT_STILL_HEATING", "Thermostat continues heating.");
                } else {
                    return new DeviceResult(true, "SET_THERMOSTAT_" + newStateType.name().toUpperCase(), 
                    "Thermostat is now " + newStateType.name().toLowerCase() + " (ambient is " + context.getAmbientTemperature().getValue() + 
                    " Farenheit, target is " + context.getDesiredTemperature().getValue() + " Farenheit)");
                }
            default:
                return new DeviceResult(false, action.name(), "Action not valid for thermostat in idle state.");
        }
    }
    @Override
    public ThermostatStateType getStateType() {
        return ThermostatStateType.HEATING;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.ON;
    }
}

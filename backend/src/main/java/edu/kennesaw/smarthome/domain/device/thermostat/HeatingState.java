package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

@Component("thermostatHeatingState")
public class HeatingState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate) {
        if(action.equals(ThermostatAction.UPDATE_AMBIENCE)) {
            int previousTemp = context.getAmbientTemperature().getValue();
            int desiredTemp = context.getDesiredTemperature().getValue();
            if(desiredTemp - previousTemp < tickRate) {
                context.getAmbientTemperature().increase(desiredTemp - previousTemp);   // Avoids overheating past desired.
            }   else {
                context.getAmbientTemperature().increase(tickRate);
            }
            return new DeviceResult(true, "HEAT_UP_AMBIENCE", "Ambient temperature has heated up from "
                    + previousTemp + " to " + context.getAmbientTemperature().getValue() + " Farenheit.");
        }
        return new DeviceResult(false, action.name(), "Improper action for heating ambience."); // Invalid action for this method
    }
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, "TOGGLE_THERMOSTAT_POWER", "Thermostat turned off.");
            case UPDATE_STATE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.determineNextState(context);
                ThermostatStateType newStateType = transitionState.getStateType();
                if(newStateType.equals(getStateType())) {
                    return new DeviceResult(true, "THERMOSTAT_STILL_HEATING", "Thermostat continues heating.");
                } else {
                    context.setState(transitionState);
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

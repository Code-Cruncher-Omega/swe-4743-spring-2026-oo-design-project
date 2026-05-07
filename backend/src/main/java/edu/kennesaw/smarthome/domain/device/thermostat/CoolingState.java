package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

@Component("thermostatCoolingState")
public class CoolingState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate) {
        if(action.equals(ThermostatAction.UPDATE_AMBIENCE)) {
            int previousTemp = context.getAmbientTemperature().getValue();
            int desiredTemp = context.getDesiredTemperature().getValue();
            if(previousTemp - desiredTemp < tickRate) {
                context.getAmbientTemperature().decrease(previousTemp - desiredTemp);   // Avoids overcooling past desired.
            }   else {
                context.getAmbientTemperature().decrease(tickRate);
            }
            return new DeviceResult(true, "COOL_DOWN_AMBIENCE", "Ambient temperature has cooled down from "
                    + previousTemp + " to " + context.getAmbientTemperature().getValue() + " Farenheit.");
        }
        return new DeviceResult(false, action.toString(), "Improper action for cooling ambience."); // Invalid action for this method
    }
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, "TOGGLE_THERMOSTAT_POWER", context.getName() + " turned off.");
            case UPDATE_STATE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.determineNextState(context);
                ThermostatStateType newStateType = transitionState.getStateType();
                if(newStateType.equals(getStateType())) {
                    return new DeviceResult(true, "THERMOSTAT_STILL_COOLING", context.getName() + " continues cooling.");
                } else {
                    context.setState(transitionState);
                    return new DeviceResult(true, "SET_THERMOSTAT_" + newStateType.toString().toUpperCase(), 
                    context.getName() + " is now " + newStateType.toString().toLowerCase() + " (ambient is " + context.getAmbientTemperature().getValue() + 
                    " Farenheit, target is " + context.getDesiredTemperature().getValue() + " Farenheit)");
                }
            default:
                return new DeviceResult(false, action.toString(), "Action not valid for " + context.getName() + " in idle state.");
        }
    }
    @Override
    public ThermostatStateType getStateType() {
        return ThermostatStateType.COOLING;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.ON;
    }
}

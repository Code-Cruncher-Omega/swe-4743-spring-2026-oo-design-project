package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.dto.DeviceResult;

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
            return new DeviceResult(true, action, "Ambient temperature has cooled down from "
                    + previousTemp + " to " + context.getAmbientTemperature().getValue() + " Farenheit.");
        }
        throw new IllegalStateException("Action not valid for " + context.getName() + " in cooling state with tick rate."); // Invalid action for this method
    }
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, action, context.getName() + " turned off.");
            case UPDATE_STATE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.determineNextState(context);
                ThermostatStateType newStateType = transitionState.getStateType();
                if(newStateType.equals(getStateType())) {
                    return new DeviceResult(true, ThermostatAction.STILL_IN_SAME_STATE, context.getName() + " continues cooling.");
                } else {
                    context.setState(transitionState);
                    return new DeviceResult(true, action, context.getName() + " is now " + newStateType.toString().toLowerCase() 
                    + " (ambient is " + context.getAmbientTemperature().getValue() + " Farenheit, target is " + context.getDesiredTemperature()
                    .getValue() + " Farenheit)");
                }
            default:
                throw new IllegalStateException("Action not valid for " + context.getName() + " in heating state."); // Invalid action for this method
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

package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.dto.DeviceResult;

@Component("thermostatIdleState")
public class IdleState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate) {
       throw new IllegalStateException("Action not valid for " + context.getName() + " in idle state with tick rate."); // Invalid action for this method
    }
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        // No heating or cooling is ever done in this state.
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, action, context.getName() + " turned off.");
            case UPDATE_STATE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.determineNextState(context);
                ThermostatStateType newStateType = transitionState.getStateType();
                if(newStateType.equals(getStateType())) {
                    return new DeviceResult(true, ThermostatAction.STILL_IN_SAME_STATE, context.getName() + " continues idling.");
                } else {
                    context.setState(transitionState);
                    return new DeviceResult(true, action, context.getName() + " is now " + newStateType.toString().toLowerCase() + ".");
                }
            default:
                throw new IllegalStateException("Action not valid for " + context.getName() + " in idle state.");
        }
    }
    @Override
    public ThermostatStateType getStateType() {
        return ThermostatStateType.IDLE;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.OFF;
    }
}

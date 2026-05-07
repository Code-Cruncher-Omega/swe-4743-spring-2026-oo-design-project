package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

@Component("thermostatIdleState")
public class IdleState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate) {
        return new DeviceResult(false, action.toString(), "Cannot update ambient temperature while " + context.getName() + " is idle.");
    }
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        // No heating or cooling is ever done in this state.
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, "TOGGLE_THERMOSTAT_POWER", context.getName() + " turned off.");
            case UPDATE_STATE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.determineNextState(context);
                ThermostatStateType newStateType = transitionState.getStateType();
                if(newStateType.equals(getStateType())) {
                    return new DeviceResult(true, "THERMOSTAT_STILL_IDLING", context.getName() + " continues idling.");
                } else {
                    context.setState(transitionState);
                    return new DeviceResult(true, "SET_THERMOSTAT_" + newStateType.toString().toUpperCase(), context.getName() + " is now " + newStateType.toString().toLowerCase() + ".");
                }
            default:
                return new DeviceResult(false, action.toString(), "Action not valid for " + context.getName() + " in idle state.");
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

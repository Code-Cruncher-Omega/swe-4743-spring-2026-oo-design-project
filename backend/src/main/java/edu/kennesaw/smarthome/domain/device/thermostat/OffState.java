package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.dto.DeviceResult;

@Component("thermostatOffState")
public class OffState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate) {
        throw new IllegalStateException("Action not valid for " + context.getName() + " in off state with tick rate."); // Invalid action for this method
    }
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getIdleState());
                return new DeviceResult(true, action, context.getName() + " turned on and idling.");
            default:
                throw new IllegalStateException("Action not valid for " + context.getName() + " in off state.");
        }
    }
    @Override
    public ThermostatStateType getStateType() {
        return ThermostatStateType.OFF;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.OFF;
    }
}

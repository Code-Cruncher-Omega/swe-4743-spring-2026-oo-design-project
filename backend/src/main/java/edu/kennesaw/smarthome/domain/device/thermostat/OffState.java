package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

@Component
public class OffState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getIdleState());
                return new DeviceResult(true, "TOGGLE_THERMOSTAT_POWER", "Thermostat turned on and idling.");
            default:
                return new DeviceResult(false, action.name(), "Action not valid for thermostat in off state.");
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

package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

@Component("thermostatOffState")
public class OffState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate) {
        return new DeviceResult(false, action.toString(), "Cannot update ambient temperature while " + context.getName() + " is off.");
    }
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getIdleState());
                return new DeviceResult(true, "TOGGLE_THERMOSTAT_POWER", context.getName() + " turned on and idling.");
            default:
                return new DeviceResult(false, action.toString(), "Action not valid for " + context.getName() + " in off state.");
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

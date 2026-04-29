package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

public class OffState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate) {
        return new DeviceResult(false, action.name(), "Cannot update ambient temperature while thermostat is off. Please turn on the thermostat first.");
    }
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

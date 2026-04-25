package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

public class IdleState implements ThermostatState {
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action) {
        // No heating or cooling is ever done in this state.
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, "TOGGLE_THERMOSTAT_POWER", "Thermostat turned off.");
            case UPDATE_AMBIENCE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.updateAmbientTemperature(context);
                ThermostatStateType newStateType = transitionState.getStateType();
                if(newStateType.equals(getStateType())) {
                    return new DeviceResult(true, "THERMOSTAT_STILL_IDLING", "Thermostat continues idling.");
                } else {
                    return new DeviceResult(true, "SET_THERMOSTAT_" + newStateType.name().toUpperCase(), "Thermostat is now " + newStateType.name().toLowerCase() + ".");
                }
            default:
                return new DeviceResult(false, action.name(), "Action not valid for thermostat in idle state.");
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

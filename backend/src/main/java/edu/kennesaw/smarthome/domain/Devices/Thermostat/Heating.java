package edu.kennesaw.smarthome.domain.Devices.Thermostat;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;

public class Heating implements ThermostatState {
    @Override
    public String getName() {
        return "Heating";
    }

    @Override
    public ActionResult execute(Thermostat.ThermostatAction action, Thermostat deviceContext) {
        switch (action) {
            case TURN_OFF:
                deviceContext.setState(new Off());
                return new ActionResult(true, "TURN_OFF", "Thermostat turned off.");
            case SET_IDLE:
                deviceContext.setState(new Idle());
                return new ActionResult(true, "SET_IDLE", "Thermostat set to idle.");
            case SET_HEATING:
                return new ActionResult(false, "SET_HEATING", "Thermostat is already in heating mode.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for current state.");
        }
    }
    
}

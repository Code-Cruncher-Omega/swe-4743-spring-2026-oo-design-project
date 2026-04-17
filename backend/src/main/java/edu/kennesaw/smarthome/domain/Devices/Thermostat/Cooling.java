package edu.kennesaw.smarthome.domain.Devices.Thermostat;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Thermostat.Thermostat.ThermostatAction;

public class Cooling implements ThermostatState {
    @Override
    public String getName() {
        return "Cooling";
    }
    
    @Override
    public ActionResult execute(ThermostatAction action, Thermostat context) {
        switch (action) {
            case TURN_OFF:
                context.setState(new Off());
                return new ActionResult(true, "TURN_OFF", "Thermostat turned off successfully.");
            case TURN_ON:
                return new ActionResult(false, "TURN_ON", "Thermostat is already on.");
            case SET_IDLE:
                context.setState(new Idle());
                return new ActionResult(true, "SET_IDLE", "Thermostat set to Idle mode.");
            case SET_COOLING:
                return new ActionResult(false, "SET_COOLING", "Thermostat is already in Cooling mode.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Thermostat in Cooling state.");
        }
    }
    
}

package edu.kennesaw.smarthome.domain.Devices.Thermostat;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Thermostat.Thermostat.ThermostatAction;

public class Idle implements ThermostatState {
    @Override
    public String getName() {
        return "Idle";
    }
    
    @Override
    public ActionResult execute(ThermostatAction action, Thermostat deviceContext) {
        switch (action) {
            case TURN_OFF:
                deviceContext.setState(new Off());
                return new ActionResult(true, "TURN_OFF", "Thermostat turned off successfully.");
            case TURN_ON:
                return new ActionResult(false, "TURN_ON", "Thermostat is already on.");
            case SET_IDLE:
                return new ActionResult(false, "SET_IDLE", "Thermostat is already in Idle mode.");
            case SET_HEATING:
                deviceContext.setState(new Heating());
                return new ActionResult(true, "SET_HEATING", "Thermostat set to Heating mode.");
            case SET_COOLING:
                deviceContext.setState(new Cooling());
                return new ActionResult(true, "SET_COOLING", "Thermostat set to Cooling mode.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Thermostat in Idle state.");
        }
    }
    
}

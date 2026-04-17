package edu.kennesaw.smarthome.domain.Devices.Thermostat;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Thermostat.Thermostat.ThermostatAction;

public class Off implements ThermostatState {
    @Override
    public String getName() {
        return "Off";
    }

    @Override
    public ActionResult execute(ThermostatAction action, Thermostat deviceContext) {
        switch (action) {
            case TURN_ON:
                deviceContext.setState(new Idle());   // Idle is essentially the On state for the thermostat
                return new ActionResult(true, "TURN_ON", "Thermostat turned on. Now in Idle mode.");
            case TURN_OFF:
                return new ActionResult(false, "TURN_OFF", "Thermostat is already off.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Off state.");
        }
    }
    
}

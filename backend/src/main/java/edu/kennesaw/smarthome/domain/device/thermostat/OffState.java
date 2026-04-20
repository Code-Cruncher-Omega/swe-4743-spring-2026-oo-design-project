package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component("thermostatOffState")
public class OffState implements ThermostatState {
    @Override
    public ActionResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getIdleState());
                return new ActionResult(true, "TOGGLE_THERMOSTAT_POWER", "Thermostat turned on and idling.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for thermostat in off state.");
        }
    }

    @Override
    public String getStateName() {
        return "Off";
    }
}

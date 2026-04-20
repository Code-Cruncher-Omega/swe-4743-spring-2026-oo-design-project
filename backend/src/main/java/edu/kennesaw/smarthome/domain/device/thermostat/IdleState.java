package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component("thermostatIdleState")
public class IdleState implements ThermostatState {
    @Override
    public ActionResult execute(Thermostat context, ThermostatAction action) {
        // No heating or cooling is ever done in this state.
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new ActionResult(true, "TOGGLE_THERMOSTAT_POWER", "Thermostat turned off.");
            case UPDATE_AMBIENCE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.updateAmbientTemperature(context);
                String verb = transitionState.getStateName();
                return new ActionResult(true, "SET_THERMOSTAT_" + verb.toUpperCase(), "Thermostat is now " + verb.toLowerCase() + ".");
            default:
                return new ActionResult(false, action.name(), "Action not valid for thermostat in idle state.");
        }
    }

    @Override
    public String getStateName() {
        return "Idle";
    }
}

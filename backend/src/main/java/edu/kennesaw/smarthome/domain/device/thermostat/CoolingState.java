package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component("thermostatCoolingState")
public class CoolingState implements ThermostatState {
    @Override
    public ActionResult execute(Thermostat context, ThermostatAction action) {
        context.getAmbientTemperature().decrease(); // Ambient temperature increases before entering another state (cooling occurs during this state).

        switch (action) {
            case TOGGLE_POWER:
                context.getAmbientTemperature().increase(); // State was interrupted, so the thermostat never finished cooling.
                context.setState(context.getOffState());
                return new ActionResult(true, "TOGGLE_THERMOSTAT_POWER", "Thermostat turned off.");
            case UPDATE_AMBIENCE:
                ThermostatMode currentMode = context.getCurrentMode();
                ThermostatState transitionState = currentMode.updateAmbientTemperature(context);
                String verb = transitionState.getStateName();
                if(verb.equals(getStateName())) {
                    return new ActionResult(true, "THERMOSTAT_STILL_COOLING", "Thermostat continues cooling.");
                } else {
                    return new ActionResult(true, "SET_THERMOSTAT_" + verb.toUpperCase(), "Thermostat is now " + verb.toLowerCase() + ".");
                }
            default:
                return new ActionResult(false, action.name(), "Action not valid for thermostat in idle state.");
        }
    }

    @Override
    public String getStateName() {
        return "Cooling";
    }
}

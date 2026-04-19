package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.ActionResult;

public class OffState implements ThermostatState {
    @Override
    public ActionResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case SET_IDLE:  // SET_IDLE also acts like TURN_ON.
                context.setState(context.getIdleState());
                return new ActionResult(true, "TURN_THERMOSTAT_ON", "Thermostat turned on and is now idle.");
            case TURN_OFF:
                return new ActionResult(true, "TURN_THERMOSTAT_OFF", "Thermostat is already off.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for thermostat in OFF state.");
        }
    }

    @Override
    public ActionResult updateAmbientTemperature(Thermostat context) {
        // Success is true for filtering purposes, but the ambient temperature does not change in the Off state.
        return new ActionResult(true, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature unchanged in Off state.");
    }

    @Override
    public String getStateName() {
        return "Off";
    }
}

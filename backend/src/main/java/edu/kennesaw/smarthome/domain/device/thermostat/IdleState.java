package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.ActionResult;

public class IdleState implements ThermostatState {
    @Override
    public ActionResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TURN_OFF:
                context.setState(context.getOffState());
                return new ActionResult(true, "TURN_THERMOSTAT_OFF", "Thermostat turned off.");
            case SET_IDLE:  // Using SET_IDLE in this state assumes another that it was an attempt to turn it on.
                return new ActionResult(true, "TURN_THERMOSTAT_ON", "Thermostat is already on in idle mode.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for thermostat in Idle state.");
        }
    }

    @Override
    public ActionResult updateAmbientTemperature(Thermostat context) {
        Temperature ambientTemperature = context.getAmbientTemperature();
        Temperature desiredTemperature = context.getDesiredTemperature();

        if(ambientTemperature.getUnit() != desiredTemperature.getUnit()) {
            return new ActionResult(false, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature unit does not match thermostat's desired temperature unit.");
        }
        if(ambientTemperature.getValue() < desiredTemperature.getValue()) {
            ThermostatMode currentMode = context.getCurrentMode();
            if(currentMode == ThermostatMode.HEAT || currentMode == ThermostatMode.AUTO) {
                ThermostatState newState = context.getHeatingState();
                context.setState(newState);
                return newState.execute(context, ThermostatAction.HEATING_UP);
            } else {
                return new ActionResult(true, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature is below desired temperature, but thermostat is not set to heat or auto mode. Ambient temperature is unchanged.");
            }
        }
        else if(ambientTemperature.getValue() > desiredTemperature.getValue()) {
            ThermostatMode currentMode = context.getCurrentMode();
            if(currentMode == ThermostatMode.COOL || currentMode == ThermostatMode.AUTO) {
                ThermostatState newState = context.getCoolingState();
                context.setState(newState);
                return newState.execute(context, ThermostatAction.COOLING_DOWN);
            } else {
                return new ActionResult(true, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature is above desired temperature, but thermostat is not set to cool or auto mode. Ambient temperature is unchanged.");
            }
        }
        return new ActionResult(true, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature is at desired level. Ambient temperature is unchanged.");
    }

    @Override
    public String getStateName() {
        return "Idle";
    }
}

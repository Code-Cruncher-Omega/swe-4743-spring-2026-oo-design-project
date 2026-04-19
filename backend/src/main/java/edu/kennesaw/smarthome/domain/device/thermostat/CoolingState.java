package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.ActionResult;

public class CoolingState implements ThermostatState {
    @Override
    public ActionResult execute(Thermostat context, ThermostatAction action) {
        switch (action) {
            case TURN_OFF:
                context.setState(context.getOffState());
                return new ActionResult(true, "TURN_THERMOSTAT_OFF", "Thermostat turned off.");
            case SET_IDLE:
                context.setState(context.getIdleState());
                return new ActionResult(true, "SET_THERMOSTAT_IDLE", "Thermostat is now idling.");
            case COOLING_DOWN:
                context.getAmbientTemperature().decrease();
                return new ActionResult(true, "COOLING_DOWN_THERMOSTAT_AMBIENT_TEMPERATURE", "Cooled down the ambient temperature of thermostat by 1 degree Farenheit.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for thermostat in HEATING state.");
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
                context.setState(context.getHeatingState());
                return execute(context, ThermostatAction.HEATING_UP);
            } else {
                context.setState(context.getIdleState());
                return new ActionResult(true, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature is below desired temperature, but thermostat is not set to heat or auto mode. Ambient temperature is unchanged.");
            }
        }
        else if(ambientTemperature.getValue() > desiredTemperature.getValue()) {
            ThermostatMode currentMode = context.getCurrentMode();
            if(currentMode == ThermostatMode.COOL || currentMode == ThermostatMode.AUTO) {
                return execute(context, ThermostatAction.COOLING_DOWN);
            } else {
                context.setState(context.getIdleState());
                return new ActionResult(true, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature is above desired temperature, but thermostat is not set to cool or auto mode. Ambient temperature is unchanged.");
            }
        }
        context.setState(context.getIdleState());
        return new ActionResult(true, "UPDATE_THERMOSTAT_AMBIENT_TEMPERATURE", "Ambient temperature is at desired level. Ambient temperature is unchanged.");
    }

    @Override
    public String getStateName() {
        return "Heating";
    }
}

package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.Action;

public enum ThermostatAction implements Action {
    TURN_ON,
    TURN_OFF,
    SET_HEATING,
    SET_COOLING
}

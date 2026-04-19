package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.Action;

public enum ThermostatAction implements Action {
    TURN_OFF,
    SET_IDLE,   // SET_IDLE also acts like TURN_ON.
    HEATING_UP,
    COOLING_DOWN
}

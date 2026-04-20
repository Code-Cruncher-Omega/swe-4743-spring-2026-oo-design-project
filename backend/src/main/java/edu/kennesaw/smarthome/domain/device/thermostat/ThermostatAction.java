package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.Action;

public enum ThermostatAction implements Action {
    TOGGLE_POWER,
    UPDATE_AMBIENCE // Using a mode object.
}

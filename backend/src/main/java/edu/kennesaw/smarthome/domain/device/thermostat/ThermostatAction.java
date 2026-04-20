package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.DeviceAction;

public enum ThermostatAction implements DeviceAction {
    TOGGLE_POWER,
    UPDATE_AMBIENCE // Using a mode object.
}

package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.DeviceStateType;

public enum ThermostatStateType implements DeviceStateType {
    OFF,
    IDLE,
    HEATING,
    COOLING
}

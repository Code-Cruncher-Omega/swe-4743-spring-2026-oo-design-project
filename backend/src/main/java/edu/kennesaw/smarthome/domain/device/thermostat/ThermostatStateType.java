package edu.kennesaw.smarthome.domain.device.thermostat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceStateType;

public enum ThermostatStateType implements DeviceStateType {
    OFF,
    IDLE,
    HEATING,
    COOLING;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static ThermostatStateType from(String value) {
        return ThermostatStateType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

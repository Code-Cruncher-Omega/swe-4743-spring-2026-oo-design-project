package edu.kennesaw.smarthome.domain.device.abstraction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// Enum of available devices that can be made.
public enum DeviceType {
    LIGHT,
    FAN,
    THERMOSTAT,
    DOOR_LOCK;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static DeviceType from(String value) {
        return DeviceType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
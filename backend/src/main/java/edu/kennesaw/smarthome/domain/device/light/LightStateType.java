package edu.kennesaw.smarthome.domain.device.light;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceStateType;

public enum LightStateType implements DeviceStateType {
    ON,
    OFF;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static LightStateType from(String value) {
        return LightStateType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

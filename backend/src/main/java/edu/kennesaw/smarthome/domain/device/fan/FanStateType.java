package edu.kennesaw.smarthome.domain.device.fan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceStateType;

public enum FanStateType implements DeviceStateType {
    ON,
    OFF;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static FanStateType from(String value) {
        return FanStateType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

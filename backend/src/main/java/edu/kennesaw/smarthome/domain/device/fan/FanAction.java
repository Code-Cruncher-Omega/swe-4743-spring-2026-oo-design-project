package edu.kennesaw.smarthome.domain.device.fan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceAction;

public enum FanAction implements DeviceAction {
    TOGGLE_POWER,
    SET_SPEED_LOW,
    SET_SPEED_MEDIUM,
    SET_SPEED_HIGH;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static FanAction from(String value) {
        return FanAction.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

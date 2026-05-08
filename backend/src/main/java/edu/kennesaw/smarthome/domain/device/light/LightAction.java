package edu.kennesaw.smarthome.domain.device.light;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceAction;

public enum LightAction implements DeviceAction {
    TOGGLE_POWER,
    SET_BRIGHTNESS,
    SET_COLOR;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static LightAction from(String value) {
        return LightAction.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

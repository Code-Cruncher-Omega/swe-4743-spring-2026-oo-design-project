package edu.kennesaw.smarthome.domain.device.thermostat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceAction;

public enum ThermostatAction implements DeviceAction {
    TOGGLE_POWER,
    UPDATE_STATE,
    UPDATE_AMBIENCE,
    STILL_IN_SAME_STATE,
    
    // Used by users, not by the state machine.
    SET_DESIRED,
    SET_AMBIENCE,
    SET_MODE_HEAT,
    SET_MODE_COOL,
    SET_MODE_AUTO;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static ThermostatAction from(String value) {
        return ThermostatAction.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

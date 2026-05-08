package edu.kennesaw.smarthome.domain.device.doorlock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceAction;

public enum DoorLockAction implements DeviceAction {
    TOGGLE_LOCK;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static DoorLockAction from(String value) {
        return DoorLockAction.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

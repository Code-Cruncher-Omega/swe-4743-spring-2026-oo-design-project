package edu.kennesaw.smarthome.domain.device.doorlock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceStateType;

public enum DoorLockStateType implements DeviceStateType {
    LOCKED,
    UNLOCKED;
    
    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static DoorLockStateType from(String value) {
        return DoorLockStateType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

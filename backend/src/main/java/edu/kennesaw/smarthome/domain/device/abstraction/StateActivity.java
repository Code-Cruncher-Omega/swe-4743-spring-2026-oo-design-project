package edu.kennesaw.smarthome.domain.device.abstraction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// Enum contains the activeness of a device's state.
// Used for filtering purposes.
public enum StateActivity {
    ON,
    OFF;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static StateActivity from(String value) {
        return StateActivity.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

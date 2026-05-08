package edu.kennesaw.smarthome.domain.device.thermostat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
// Enum containing all available thermostat modes.
public enum ThermostatModeType {
    HEAT,
    COOL,
    AUTO;

    // Methods for parsing Strings to enum values (case-insensitive).
    @JsonCreator
    public static ThermostatModeType from(String value) {
        return ThermostatModeType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

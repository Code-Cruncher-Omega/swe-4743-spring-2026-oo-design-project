package edu.kennesaw.smarthome.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceStateType;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;

public record DeviceStatus(

    @Schema(
        description = "Unique identifier of the device", 
        example = "a3f1c2d4-..."
    )
    String id,

    @Schema(
        description = "Display name of the device", 
        example = "Living Room Light"
    )
    String name,

    @Schema(
        description = "Physical location of the device", 
        example = "Living Room"
    )
    String location,

    @Schema(
        description = "Current state of the device", 
        example = "ON"
    )
    DeviceStateType state,

    @Schema(
        description = "Type of device", 
        example = "LIGHT", 
        allowableValues = {"LIGHT", "FAN", "THERMOSTAT", "DOOR_LOCK"}
    )
    DeviceType deviceType,
    
    @Schema(description = "Device-specific attributes such as brightness, speed, or temperature")
    Map<String, Object> attributes
) {
    public static DeviceStatus of(Device<?, ?, ?, ?> device) {
        return new DeviceStatus(
            device.getId().toString(),
            device.getName(),
            device.getLocation(),
            device.getState().getStateType(),
            device.getType(),
            device.getAttributes()
        );
    }
}

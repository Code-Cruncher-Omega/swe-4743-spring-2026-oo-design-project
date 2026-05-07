package edu.kennesaw.smarthome.service.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;

public record DeviceStatus(
    @Schema(description = "Unique identifier of the device", example = "a3f1c2d4-...")
    String id,
    @Schema(description = "Display name of the device", example = "Living Room Light")
    String name,
    @Schema(description = "Physical location of the device", example = "Living Room")
    String location,
    @Schema(description = "Current state of the device", example = "ON")
    String state,
    @Schema(description = "Type of device", example = "LIGHT", allowableValues = {"LIGHT", "FAN", "THERMOSTAT", "DOOR_LOCK"})
    String type,
    @Schema(description = "Device-specific attributes such as brightness, speed, or temperature")
    Map<String, String> attributes
) {
    public static DeviceStatus of(Device<?, ?, ?, ?> device) {
        return new DeviceStatus(
            device.getId().toString(),
            device.getName(),
            device.getLocation(),
            device.getState().getStateType().toString(),
            device.getType().toString(),
            device.getAttributes()
        );
    }
}

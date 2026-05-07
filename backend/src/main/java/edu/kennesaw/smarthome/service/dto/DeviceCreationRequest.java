package edu.kennesaw.smarthome.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// Record used to pass creation requests to a DeviceFactory, which is then passed down to its
// respective device creator. Used by users to request the controller to make a device.
@Schema(description = "Request to create a new device")
public record DeviceCreationRequest(
    @Schema(description = "Display name of the device", example = "Living Room Light")
    String name,
    @Schema(description = "Physical location of the device", example = "Living Room")
    String location,
    @Schema(description = "Type of device to create", example = "LIGHT", allowableValues = {"LIGHT", "FAN", "THERMOSTAT", "DOOR_LOCK"})
    String deviceType
) {}
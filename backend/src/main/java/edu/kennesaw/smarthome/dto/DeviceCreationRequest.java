package edu.kennesaw.smarthome.dto;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Record used to pass creation requests to a DeviceFactory, which is then passed down to its
// respective device creator. Used by users to request the controller to make a device.
@Schema(description = "Request to create a new device")
public record DeviceCreationRequest(

    @NotBlank(message = "Device name is required")
    @Size(max = 100, message = "Device name cannot exceed 100 characters")
    @Schema(
        description = "Display name of the device", 
        example = "Living Room Light"
    )
    String name,

    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    @Schema(
        description = "Physical location of the device", 
        example = "Living Room"
    )
    String location,

    @NotNull(message = "Device type is required")
    @Schema(description = "Type of device to create", 
        example = "LIGHT", 
        allowableValues = {"LIGHT", "FAN", "THERMOSTAT", "DOOR_LOCK"}
    )
    DeviceType deviceType
) {}
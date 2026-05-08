package edu.kennesaw.smarthome.dto;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceAction;
import io.swagger.v3.oas.annotations.media.Schema;

// Used to pass messages from a device to a higher class (i.e. environment or simulation).
public record DeviceResult(

    @Schema(description = "Whether the action was successful")
    boolean success,
    
    @Schema(
        description = "Name of the action that was performed", 
        example = "TOGGLE_POWER"
    )
    DeviceAction action,

    @Schema(
        description = "Human-readable message describing the result", 
        example = "Light turned on successfully."
    )
    String message
) {}
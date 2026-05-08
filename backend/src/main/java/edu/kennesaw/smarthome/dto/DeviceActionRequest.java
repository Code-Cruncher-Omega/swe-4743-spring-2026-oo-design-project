package edu.kennesaw.smarthome.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Pass an action name, and any number of parameters to the device which will perform an action.
// Used for communication between controller and users.
@Schema(description = "Request to perform an action on a device")
public record DeviceActionRequest(

    @NotBlank(message = "Action is required")
    @Size(max = 50, message = "Action name too long")
    @Schema(
        description = "Name of the action to perform", 
        example = "TOGGLE_POWER"
    )
    String action,

    @NotNull(message = "Parameters array cannot be null")
    @Schema(
        description = "Optional parameters for the action, such as brightness level or RGB values", 
        example = "[75]"
    )
    Object... parameters
) {}
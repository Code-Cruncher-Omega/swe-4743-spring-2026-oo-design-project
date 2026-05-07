package edu.kennesaw.smarthome.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// Pass an action name, and any number of parameters to the device which will perform an action.
// Used for communication between controller and users.
@Schema(description = "Request to perform an action on a device")
public record DeviceActionRequest(
    @Schema(description = "Name of the action to perform", example = "TOGGLE_POWER")
    String action,
    @Schema(description = "Optional parameters for the action, such as brightness level or RGB values", example = "[75]")
    Object... parameters
) {}
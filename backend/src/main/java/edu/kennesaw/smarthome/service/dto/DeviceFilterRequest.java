package edu.kennesaw.smarthome.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// Record used to specify the parameters for each filtering method contained in EnvironmentDeviceQueryService.
// Filtering should work with null inputs in the record.
@Schema(description = "Filter criteria for querying devices across environments")
public record DeviceFilterRequest(
    @Schema(description = "Filter by physical location", example = "Living Room", nullable = true)
    String location,
    @Schema(description = "Filter by activity state", example = "ON", nullable = true)
    String activity,
    @Schema(description = "Filter by device type", example = "LIGHT", allowableValues = {"LIGHT", "FAN", "THERMOSTAT", "DOOR_LOCK"}, nullable = true)
    String type
) {}

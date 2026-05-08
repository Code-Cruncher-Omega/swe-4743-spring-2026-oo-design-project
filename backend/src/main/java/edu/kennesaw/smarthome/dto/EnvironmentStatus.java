package edu.kennesaw.smarthome.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import edu.kennesaw.smarthome.domain.Environment;

@Schema(description = "Status snapshot of an environment and all its devices")
public record EnvironmentStatus(

    @Schema(
        description = "Name of the environment", 
        example = "Living Room"
    )
    String name,

    @Schema(description = "Number of devices in this environment")
    int deviceCount,
    
    @Schema(description = "List of device statuses within this environment")
    List<DeviceStatus> devices
) {
    public static EnvironmentStatus of(Environment environment) {
        List<DeviceStatus> deviceResponses = environment.getDevices()
            .stream()
            .map(DeviceStatus::of)
            .toList();

        return new EnvironmentStatus (
            environment.getName(),
            deviceResponses.size(),
            deviceResponses
        );
    }
}

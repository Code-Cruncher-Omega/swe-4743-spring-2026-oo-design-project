package edu.kennesaw.smarthome.service.dto;

import java.util.List;

import edu.kennesaw.smarthome.domain.Environment;

public record EnvironmentStatus (
    String name,
    int deviceCount,
    List<DeviceStatus> devices
) {
    public static EnvironmentStatus from(Environment environment) {
        List<DeviceStatus> deviceResponses = environment.getDevices()
            .stream()
            .map(DeviceStatus::from)
            .toList();

        return new EnvironmentStatus (
            environment.getName(),
            deviceResponses.size(),
            deviceResponses
        );
    }
}

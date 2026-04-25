package edu.kennesaw.smarthome.service.dto;

import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;

public record DeviceStatus (
    UUID id,
    String name,
    String location,
    String state,
    String type,
    Map<String, String> attributes
) {
    public static DeviceStatus from(Device<?, ?, ?, ?> device) {
        return new DeviceStatus(
            device.getId(),
            device.getName(),
            device.getLocation(),
            device.getState().getStateType().toString(),
            device.getType().name(),
            device.getAttributes()
        );
    }
}

package edu.kennesaw.smarthome.service.dto;

import java.util.Map;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;

public record DeviceStatus (
    String id,
    String name,
    String location,
    String state,
    String type,
    Map<String, String> attributes
) {
    public static DeviceStatus from(Device<?, ?, ?, ?> device) {
        return new DeviceStatus(
            device.getId().toString(),
            device.getName(),
            device.getLocation(),
            device.getState().getStateType().toString(),
            device.getType().name(),
            device.getAttributes()
        );
    }
}

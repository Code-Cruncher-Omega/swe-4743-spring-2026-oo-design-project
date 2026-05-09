package edu.kennesaw.smarthome.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.service.creator.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.dto.DeviceSnapshot;

@Component
public class DeviceFactory {
    
    private final Map<DeviceType, DeviceCreator<?, ?, ?, ?>> CREATORS;

    // Spring provides a List containing an instance from each concrete DeviceCreator.
    public DeviceFactory(List<DeviceCreator<?, ?, ?, ?>> creatorsList) {
        this.CREATORS = creatorsList.stream()
                .collect(Collectors.toMap(DeviceCreator::getDeviceType, Function.identity()));
    }

    // Views the request given, chooses the correct creator if available, and creates the device.
    public Device<?, ?, ?, ?> create(DeviceCreationRequest request) {
        if(request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Device name is required");
        }
        if(request.location() == null || request.location().isBlank()) {
            throw new IllegalArgumentException("Device location is required");
        }
        DeviceCreator<?, ?, ?, ?> creator = CREATORS.get(request.deviceType());
        if(creator == null) {
            throw new IllegalArgumentException("Unsupported device type: " + request.deviceType());
        }
        return creator.createDevice(request);
    }

    // Recreates device based on the given details and attributes.
    public Device<?, ?, ?, ?> create(DeviceSnapshot snapshot) {
        if(snapshot.name() == null || snapshot.name().isBlank()) {
            throw new IllegalArgumentException("Device name is required");
        }
        if(snapshot.location() == null || snapshot.location().isBlank()) {
            throw new IllegalArgumentException("Device location is required");
        }
        DeviceCreator<?, ?, ?, ?> creator = CREATORS.get(snapshot.deviceType());
        if(creator == null) {
            throw new IllegalArgumentException("Unsupported device type: " + snapshot.deviceType());
        }
        return creator.createDevice(snapshot);
    }
}

package edu.kennesaw.smarthome.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.service.creator.DeviceCreator;
import edu.kennesaw.smarthome.service.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;

@Component
public class DeviceFactory {
    
    // Keys are lowercase.
    private final Map<String, DeviceCreator<?, ?, ?, ?>> CREATORS;

    // Spring provides a List containing an instance from each concrete DeviceCreator.
    public DeviceFactory(List<DeviceCreator<?, ?, ?, ?>> creatorsList) {
        this.CREATORS = creatorsList.stream()
                .collect(Collectors.toMap(device -> device.getDeviceType().toString().toLowerCase(), Function.identity()));
    }

    // Views the request given, chooses the correct creator if available, and creates the device.
    public Device<?, ?, ?, ?> create(DeviceCreationRequest request) {
        DeviceCreator<?, ?, ?, ?> creator = CREATORS.get(request.deviceType().toLowerCase());   // Takes deviceType from request and makes it lowercase.
        if(creator == null) {
            throw new IllegalArgumentException("Unsupported device type: " + request.deviceType());
        }
        return creator.createDevice(request);
    }
}

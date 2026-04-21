package edu.kennesaw.smarthome.domain.device;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class DeviceFactory {
    
    private final Map<DeviceType, DeviceCreator<?, ?, ?, ?>> CREATORS;

    public DeviceFactory(List<DeviceCreator<?, ?, ?, ?>> creatorsList) {
        this.CREATORS = creatorsList.stream()
                .collect(Collectors.toMap(
                    DeviceCreator::getDeviceType,
                    Function.identity()
                ));
    }

    public Device<?, ?, ?, ?> create(DeviceCreationRequest request) {
        DeviceCreator<?, ?, ?, ?> creator = CREATORS.get(request.deviceType());
        if (creator == null) {
            throw new IllegalArgumentException("Unsupported device type: " + request.deviceType().name());
        }
        return creator.createDevice(request);
    }
}

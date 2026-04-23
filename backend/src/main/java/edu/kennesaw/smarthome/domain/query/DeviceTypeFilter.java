package edu.kennesaw.smarthome.domain.query;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;

@Component
public class DeviceTypeFilter extends EnvironmentDeviceQueryDecoratorBase {
    
    private final DeviceType TYPE;

    public DeviceTypeFilter(EnvironmentDeviceQuery inner, Environment environment, DeviceType type) {
        super(inner, environment);
        this.TYPE = type;
    }

    @Override
    public Environment run() {
        Map<UUID, Device<?, ?, ?, ?>> filteredDevices = new HashMap<>();

        for(Device<?, ?, ?, ?> device : environment.getDevices().values()) {    // Check every device in the environment.
            if(device.getType().equals(TYPE)) {                                 // If the device type matches TYPE,
                filteredDevices.put(device.getId(), device);                    // add it to filteredDevices.
            }
        }

        return new Environment(environment.getName(), filteredDevices);
    }

    @Override
    public EnvironmentDeviceFilterType getFilterType() {
        return EnvironmentDeviceFilterType.DEVICE_TYPE;
    }
}
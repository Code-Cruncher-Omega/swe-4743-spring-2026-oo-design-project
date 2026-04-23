package edu.kennesaw.smarthome.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;

@Service
public class EnvironmentService {
    private final Map<String, Environment> ENVIRONMENTS;  // Stores all environments that will have their contents change. (interacts with its contents)

    // Spring provides a DeviceFactory and DeviceQueryService.
    public EnvironmentService(DeviceFactory deviceFactory, EnvironmentDeviceQueryService environmentDeviceQueryService) {
        this.ENVIRONMENTS = new HashMap<>();
    }

    public void updateAllEnvironments() {
        for(Environment environment : ENVIRONMENTS.values()) {
            environment.update();
        }
    }

    public void addDevice(Device<?, ?, ?, ?> device) {
        Environment environment = getOrCreateEnvironment(device.getLocation());
        environment.addDevice(device);
    }

    public void removeDevice(UUID id) {
        for(Environment environment : ENVIRONMENTS.values()) {
            Device<?, ?, ?, ?> device = environment.getDevice(id);
            if(device != null) {
                return;
            }
        }
    }

    public Environment getOrCreateEnvironment(String location) {
        return ENVIRONMENTS.computeIfAbsent(location, (Environment::new));
    }

    public Collection<Environment> getAllEnvironments() {
        return Collections.unmodifiableCollection(ENVIRONMENTS.values());
    }

    public void reset() {
        for(Environment environment : ENVIRONMENTS.values()) {
            environment.resetAllDevices();
        }
    }
}

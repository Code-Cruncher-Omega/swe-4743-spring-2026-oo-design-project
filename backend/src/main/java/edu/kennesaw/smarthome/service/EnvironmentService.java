package edu.kennesaw.smarthome.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;

@Service
public class EnvironmentService {
    private final Map<String, Environment> REAL_ENVIRONMENTS;  // Stores all environments that will have their contents change. (interacts with its contents)

    // Spring provides a DeviceFactory and DeviceQueryService.
    public EnvironmentService(DeviceFactory deviceFactory, EnvironmentDeviceQueryService environmentDeviceQueryService) {
        this.REAL_ENVIRONMENTS = new HashMap<>();
    }

    public void updateAllEnvironments() {
        for(Environment environment : REAL_ENVIRONMENTS.values()) {
            environment.update();
        }
    }

    public void addDevice(Device<?, ?, ?, ?> device) {
        Environment environment = getOrCreateEnvironment(device.getLocation());
        environment.addDevice(device);
    }

    public void removeDevice(Device<?, ?, ?, ?> device) {
        Environment environment = REAL_ENVIRONMENTS.get(device.getLocation());
        if(environment == null) {
            return;     // No device with such location exists.
        }
        environment.removeDevice(device.getId());
    }

    public Environment getOrCreateEnvironment(String location) {
        return REAL_ENVIRONMENTS.computeIfAbsent(location, (Environment::new));
    }

    public Map<String, Environment> getAllRealEnvironments() {
        return Collections.unmodifiableMap(REAL_ENVIRONMENTS);
    }

    public void reset() {
        for(Environment environment : REAL_ENVIRONMENTS.values()) {
            environment.resetAllDevices();
        }
    }
}

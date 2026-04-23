package edu.kennesaw.smarthome.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.EnvironmentResult;
import edu.kennesaw.smarthome.service.factory.DeviceFactory;

@Service
public class EnvironmentService {
    
    private final DeviceFactory DEVICE_FACTORY;  // Device creation is delegated to this.
    private final EnvironmentDeviceQueryService ENVIRONMENT_DEVICE_QUERY_SERVICE;   // Querying/filtering is delegated to this.

    private final Map<String, Environment> REAL_ENVIRONMENTS;  // Stores all environments that will have their contents change. (interacts with its contents)

    private Map<String, Environment> filteredEnvironments;  // Stores all environments to present. (only displays contents)

    // Spring provides a DeviceFactory and DeviceQueryService.
    public EnvironmentService(DeviceFactory deviceFactory, EnvironmentDeviceQueryService environmentDeviceQueryService) {
        this.DEVICE_FACTORY = deviceFactory;
        this.ENVIRONMENT_DEVICE_QUERY_SERVICE = environmentDeviceQueryService;
        this.REAL_ENVIRONMENTS = new HashMap<>();
        this.filteredEnvironments = null;
    }

    public EnvironmentResult reset() {
        for(Environment environment : REAL_ENVIRONMENTS.values()) {
            EnvironmentResult result = environment.resetAllDevices();
            if(!result.success()) {
                return result;
            }
        }
        return new EnvironmentResult(true, "RESET_ALL_DEVICES", "Successfully resetted all devices in every environment.");
    }

    
}

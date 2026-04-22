package edu.kennesaw.smarthome.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.DeviceFactory;

@Component
public class Simulation {
    
    private final DeviceFactory deviceFactory;  

    private final Map<String, Environment> REAL_ENVIRONMENTS;  // Stores all environments that will change. (interacts with its contents)
    private Map<String, Environment> filteredEnvironments;  // Stores all environments to present. (only displays contents)

    public Simulation(DeviceFactory deviceFactory) {
        this.deviceFactory = deviceFactory;
        this.REAL_ENVIRONMENTS = new HashMap<>();
        this.filteredEnvironments = null;
    }
}

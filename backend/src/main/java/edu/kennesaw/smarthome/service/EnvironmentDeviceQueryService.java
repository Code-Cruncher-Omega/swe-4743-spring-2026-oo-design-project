package edu.kennesaw.smarthome.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

@Service
public class EnvironmentDeviceQueryService {

    public EnvironmentDeviceQueryService() {}


    // All filters/queries should be able to handle null cases (specifically for what is being filtered).


    // Goes through the given Collection and adds any Environment whose name matches the location to a new
    // Collection, then returns said Collection. Environments each have a unique name, so break if this location is found.
    // Returns an empty Collection if no environment with matching location is found.
    public Collection<Environment> filterByLocation(Collection<Environment> environments, String location) {

        if(location == null || location.isEmpty()) {
            return environments;
        }

        Collection<Environment> filteredEnvironments = new ArrayList<>();

        for(Environment environment : environments) {
            if(environment.getName().equals(location)) {
                filteredEnvironments.add(environment);
                break;
            }
        }

        return filteredEnvironments;
    }

    // Checks every device inside each Environment. If a device's state activity matches activity, then it is
    // added to a new Map of Devices for the Environment being evaluated. Once all Devices in the old Environment
    // has been checked, make a new Environment using the new Map and add it to the filteredEnvironments Collection
    // only if the Map is not empty. Repeat for every Environment in the provided Collection, then return filteredEnvironments.
    public Collection<Environment> filterByActivity(Collection<Environment> environments, StateActivity activity) {

        if(activity == null) {
            return environments;
        }

        Collection<Environment> filteredEnvironments = new ArrayList<>();

        for(Environment environment : environments) {
            
            Map<UUID, Device<?, ?, ?, ?>> filteredDevices = new HashMap<>();
            for(Device<?, ?, ?, ?> device : environment.getDevices().values()) {

                if(device.getState().getStateActivity().equals(activity)) {
                    filteredDevices.put(device.getId(), device);
                }
            }

            if(!filteredDevices.isEmpty()) {
                filteredEnvironments.add(new Environment(environment.getName(), filteredDevices));
            }
        }

        return filteredEnvironments;
    }

    // Same gist as filterByActivity, but instead comparing each Device's DeviceType with the one provided.
    public Collection<Environment> filterByType(Collection<Environment> environments, DeviceType deviceType) {

        if(deviceType == null) {
            return environments;
        }

        Collection<Environment> filteredEnvironments = new ArrayList<>();

        for(Environment environment : environments) {
            
            Map<UUID, Device<?, ?, ?, ?>> filteredDevices = new HashMap<>();
            for(Device<?, ?, ?, ?> device : environment.getDevices().values()) {

                if(device.getType().equals(deviceType)) {
                    filteredDevices.put(device.getId(), device);
                }
            }

            if(!filteredDevices.isEmpty()) {
                filteredEnvironments.add(new Environment(environment.getName(), filteredDevices));
            }
        }

        return filteredEnvironments;
    }
}

package edu.kennesaw.smarthome.service;

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


    // Goes through the given Map to find an Environment whose name matches the provided location (case-insensitive) and 
    // returns a Map containing only that Environment. If none are found, then return an empty Map.
    // Environments cannot share the same name.
    public Map<String, Environment> filterByLocation(Map<String, Environment> environments, String location) {

        if(location == null || location.isEmpty()) {
            return environments;
        }

        Map<String, Environment> filteredEnvironments = new HashMap<>();

        for(Environment environment : environments.values()) {
            if(environment.getName().toLowerCase().equals(location)) {
                filteredEnvironments.put(environment.getName(), environment);
                break;
            }
        }

        return filteredEnvironments;
    }

    // A new Map containing filtered Environments is created, named filteredEnvironments.
    // For every Environment in the provided Map, create a new Map containing Devices.
    // Each Device in a Environment whose state activity matches the provided activity, that Device is added to their
    // respective Map of Devices. If the filtered Map of Devices for an Environment is empty, then do not bother
    // adding a filtered variation of that Environemnt to filteredEnvironments. Otherwise, add it using the old
    // Environment's name and make a new Environment object using said name and the filtered Map of Devices.
    // filteredEnvironments is returned in the end.
    public Map<String, Environment> filterByActivity(Map<String, Environment> environments, StateActivity activity) {

        if(activity == null) {
            return environments;
        }

        Map<String, Environment> filteredEnvironments = new HashMap<>();

        for(Environment environment : environments.values()) {
            
            Map<UUID, Device<?, ?, ?, ?>> filteredDevices = new HashMap<>();
            for(Device<?, ?, ?, ?> device : environment.getDevices().values()) {

                if(device.getState().getStateActivity().equals(activity)) {
                    filteredDevices.put(device.getId(), device);
                }
            }

            if(!filteredDevices.isEmpty()) {
                filteredEnvironments.put(environment.getName(), new Environment(environment.getName(), filteredDevices));
            }
        }

        return filteredEnvironments;
    }

    // Same gist as filterByActivity, but instead comparing each Device's DeviceType with the one provided.
    public Map<String, Environment> filterByType(Map<String, Environment> environments, DeviceType deviceType) {

        if(deviceType == null) {
            return environments;
        }

        Map<String, Environment> filteredEnvironments = new HashMap<>();

        for(Environment environment : environments.values()) {
            
            Map<UUID, Device<?, ?, ?, ?>> filteredDevices = new HashMap<>();
            for(Device<?, ?, ?, ?> device : environment.getDevices().values()) {

                if(device.getType().equals(deviceType)) {
                    filteredDevices.put(device.getId(), device);
                }
            }

            if(!filteredDevices.isEmpty()) {
                filteredEnvironments.put(environment.getName(), new Environment(environment.getName(), filteredDevices));
            }
        }

        return filteredEnvironments;
    }
}

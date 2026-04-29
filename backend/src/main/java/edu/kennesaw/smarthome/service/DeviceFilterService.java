package edu.kennesaw.smarthome.service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.service.dto.DeviceFilterRequest;
import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;

@Service
public class DeviceFilterService {

    // All filters should work with a null filter-parameter (i.e. location, activity, type, etc.).
    
    // Applies all filters on each Environment's Devices.
    public Collection<Environment> filterDevicesInEnvironments(Collection<Environment> environments, DeviceFilterRequest request) {
        return environments.stream()
                .map(environment -> {
                        Collection<Device<?, ?, ?, ?>> filteredDevices = environment.getDevices();

                        // Not ideal, but this works for now...

                        filteredDevices = filterByLocation(filteredDevices, request.location());
                        filteredDevices = filterByActivity(filteredDevices, request.activity());
                        filteredDevices = filterByType(filteredDevices, request.type());

                        Map<UUID, Device<?, ?, ?, ?>> devices = filteredDevices.stream().collect(Collectors.toMap(Device::getId, Function.identity()));
                        
                        return new Environment(environment.getName(), devices);
                    })
                            .filter(environment -> !environment.getDevices().isEmpty()) // Removes empty Environments from the Collection.
                                    .toList();
    }

    private Collection<Device<?, ?, ?, ?>> filterByLocation(Collection<Device<?, ?, ?, ?>> devices, String location) {
        if(location == null || location.isEmpty()) {
            return devices;
        }
        
        return devices.stream()
                .filter(device -> device.getLocation()
                        .equals(location)).toList();
    }

    // Makes activity and a device's state activity lowercase before making any comparisons.
    private Collection<Device<?, ?, ?, ?>> filterByActivity(Collection<Device<?, ?, ?, ?>> devices, String activity) {
        if(activity == null || activity.isEmpty()) {
            return devices;
        }
        
        return devices.stream()
                .filter(device -> device.getState().getStateActivity().toString().toLowerCase().equals(activity.toLowerCase()))
                        .toList();        
    }

    // Makes deviceType and a device's device type lowercase before making any comparisons.
    private Collection<Device<?, ?, ?, ?>> filterByType(Collection<Device<?, ?, ?, ?>> devices, String deviceType) {
        if(deviceType == null || deviceType.isEmpty()) {
            return devices;
        }
        
        return devices.stream()
                .filter(device -> device.getType().toString().toLowerCase().equals(deviceType.toLowerCase()))
                        .toList();  
    }
}

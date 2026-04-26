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
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

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

    private Collection<Device<?, ?, ?, ?>> filterByActivity(Collection<Device<?, ?, ?, ?>> devices, StateActivity activity) {
        if(activity == null) {
            return devices;
        }
        
        return devices.stream()
                .filter(device -> device.getState().getStateActivity().equals(activity))
                        .toList();        
    }

    private Collection<Device<?, ?, ?, ?>> filterByType(Collection<Device<?, ?, ?, ?>> devices, DeviceType deviceType) {
        if(deviceType == null) {
            return devices;
        }
        
        return devices.stream()
                .filter(device -> device.getType().equals(deviceType))
                        .toList();  
    }
}

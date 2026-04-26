package edu.kennesaw.smarthome.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.service.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.service.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.service.dto.DeviceFilterRequest;
import edu.kennesaw.smarthome.service.dto.DeviceStatus;
import edu.kennesaw.smarthome.service.dto.EnvironmentStatus;
import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.UpdateableDevice;

@Service
public class SmartHomeService {

    private final DeviceFactory DEVICE_FACTORY;
    private final EnvironmentService ENVIRONMENT_SERVICE;
    private final DeviceFilterService ENVIRONMENT_DEVICE_QUERY_SERVICE;

    // Spring provides an instance of all of these.
    public SmartHomeService(DeviceFactory factory, 
                            EnvironmentService environmentService, 
                            DeviceFilterService environmentDeviceQueryService) {
        this.DEVICE_FACTORY = factory;
        this.ENVIRONMENT_SERVICE = environmentService;
        this.ENVIRONMENT_DEVICE_QUERY_SERVICE = environmentDeviceQueryService;
    }

    public DeviceResult performDeviceAction(UUID id, DeviceActionRequest request) {
        Device<?, ?, ?, ?> device = ENVIRONMENT_SERVICE.getDevice(id);
        if(device != null) {
            return device.performAction(request);
        }
        return new DeviceResult(false, request.action().toString(), "Could not find device with ID " + id);
    }

    public UUID createAndAddDevice(DeviceCreationRequest request) {
        Device<?, ?, ?, ?> device = DEVICE_FACTORY.create(request);
        ENVIRONMENT_SERVICE.addDevice(device);
        return device.getId();
    }

    public void removeDevice(UUID id) {
        ENVIRONMENT_SERVICE.removeDevice(id);
    }

    public void update() {
        ENVIRONMENT_SERVICE.updateAllEnvironments();
    }

    public Collection<EnvironmentStatus> queryEnvironmentStatus(DeviceFilterRequest request) {
        return ENVIRONMENT_DEVICE_QUERY_SERVICE.filterDevicesInEnvironments(ENVIRONMENT_SERVICE.getAllEnvironments(), request)
                .stream()
                        .map(Environment::getStatus)
                                .toList();
    }

    // UpdateableDevices are likely to receive special treatment when it comes to dispalying their status. Thermostats are one of
    // these kinds of Devices that will receive such treatment.
    public Collection<DeviceStatus> getUpdateableDeviceStatus() {
        Collection<DeviceStatus> updateableDeviceStatuses = new ArrayList<>();

        for(Environment environment : ENVIRONMENT_SERVICE.getAllEnvironments()) {
            for(Device<?, ?, ?, ?> device : environment.getDevices()) {
                if(device instanceof UpdateableDevice) {    // I feel icky about this, but this just seems better than adding a switch-case.
                    updateableDeviceStatuses.add(device.getStatus());
                }
            }
        }

        return updateableDeviceStatuses;
    }
}

package edu.kennesaw.smarthome.application;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.application.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.application.dto.EnvironmentDeviceQueryRequest;
import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.service.DeviceFactory;
import edu.kennesaw.smarthome.service.EnvironmentDeviceQueryService;
import edu.kennesaw.smarthome.service.EnvironmentService;

@Service
public class SmartHomeService {

    private final DeviceFactory DEVICE_FACTORY;
    private final EnvironmentService ENVIRONMENT_SERVICE;
    private final EnvironmentDeviceQueryService ENVIRONMENT_DEVICE_QUERY_SERVICE;

    public SmartHomeService(DeviceFactory factory, 
                            EnvironmentService environmentService, 
                            EnvironmentDeviceQueryService environmentDeviceQueryService) {
        this.DEVICE_FACTORY = factory;
        this.ENVIRONMENT_SERVICE = environmentService;
        this.ENVIRONMENT_DEVICE_QUERY_SERVICE = environmentDeviceQueryService;
    }

    public void createAndAddDevice(DeviceCreationRequest request) {
        ENVIRONMENT_SERVICE.addDevice(DEVICE_FACTORY.create(request));
    }

    public void removeDevice(UUID id) {
        ENVIRONMENT_SERVICE.removeDevice(id);
    }

    public void update() {
        ENVIRONMENT_SERVICE.updateAllEnvironments();
    }

    public Collection<Environment> getRealEnvironments() {
        return ENVIRONMENT_SERVICE.getAllEnvironments();
    }

    public Collection<Environment> getFilteredEnvironments(EnvironmentDeviceQueryRequest request) {
        Collection<Environment> copyOfEnvironments = ENVIRONMENT_SERVICE.getAllEnvironments();

        copyOfEnvironments = ENVIRONMENT_DEVICE_QUERY_SERVICE.filterByActivity(copyOfEnvironments, request.activity());
        copyOfEnvironments = ENVIRONMENT_DEVICE_QUERY_SERVICE.filterByLocation(copyOfEnvironments, request.location());
        copyOfEnvironments = ENVIRONMENT_DEVICE_QUERY_SERVICE.filterByType(copyOfEnvironments, request.type());
        
        return copyOfEnvironments;
    }
}

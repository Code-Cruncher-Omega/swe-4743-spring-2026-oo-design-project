package edu.kennesaw.smarthome.controller;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kennesaw.smarthome.service.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.service.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.service.dto.DeviceFilterRequest;
import edu.kennesaw.smarthome.service.dto.DeviceStatus;
import edu.kennesaw.smarthome.service.dto.EnvironmentStatus;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.service.SmartHomeService;

@RestController
@RequestMapping("/api/smarthome")
public class SmartHomeController {

    private final SmartHomeService SMART_HOME_SERVICE;

    // Spring provides a SmartHomeService.
    public SmartHomeController(SmartHomeService smartHomeService) {
        this.SMART_HOME_SERVICE = smartHomeService;
    }

    @PostMapping("/devices")
    public ResponseEntity<Void> createDevice(@RequestBody DeviceCreationRequest request) {
        UUID id = SMART_HOME_SERVICE.createAndAddDevice(request);

        return ResponseEntity
                .created(URI.create("/api/devices/" + id))
                        .build();
    }

    @PostMapping("/devices/{id}/actions")
    public ResponseEntity<DeviceResult> performDeviceAction(@PathVariable UUID id, @RequestBody DeviceActionRequest request) {
        DeviceResult result = SMART_HOME_SERVICE.performDeviceAction(id, request);

        if(!result.success()) {    // For now, but needs to be updated...
            return ResponseEntity
                    .notFound()
                            .build();
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> removeDevice(@PathVariable UUID id) {
        SMART_HOME_SERVICE.removeDevice(id);
        return ResponseEntity
                .noContent()
                        .build();
    }

    @PostMapping("/simulation/update")
    public ResponseEntity<Void> updateSimulation() {
        SMART_HOME_SERVICE.update();
        return ResponseEntity
                .ok()
                        .build();
    }

    @PostMapping("/environments/status")
    public ResponseEntity<Collection<EnvironmentStatus>> queryEnvironments(@RequestBody DeviceFilterRequest request) {
        return ResponseEntity.ok(SMART_HOME_SERVICE.queryEnvironmentStatus(request));
    }

    @GetMapping("/devices/updateable")
    public ResponseEntity<Collection<DeviceStatus>> getUpdateableDevices() {
        return ResponseEntity.ok(SMART_HOME_SERVICE.getUpdateableDeviceStatus());
    }
}
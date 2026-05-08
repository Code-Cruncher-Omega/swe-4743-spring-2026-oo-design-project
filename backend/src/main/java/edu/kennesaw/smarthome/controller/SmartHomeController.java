package edu.kennesaw.smarthome.controller;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import edu.kennesaw.smarthome.dto.AuditEntry;
import edu.kennesaw.smarthome.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.dto.DeviceFilterRequest;
import edu.kennesaw.smarthome.dto.DeviceResult;
import edu.kennesaw.smarthome.dto.DeviceStatus;
import edu.kennesaw.smarthome.dto.EnvironmentStatus;
import edu.kennesaw.smarthome.service.AuditLog;
import edu.kennesaw.smarthome.service.PersistenceService;
import edu.kennesaw.smarthome.service.SmartHomeService;

@RestController
@RequestMapping("/api/smarthome")
@Tag(name = "Smart Home", description = "Smart Home device management API")
public class SmartHomeController {

    private final AuditLog AUDIT_LOG;
    private final PersistenceService PERSISTENCE_SERVICE;
    private final SmartHomeService SMART_HOME_SERVICE;

    // Spring provides a AuditLog and SmartHomeService.
    public SmartHomeController(AuditLog auditLog, PersistenceService persistenceService, SmartHomeService smartHomeService) {
        this.AUDIT_LOG = auditLog;
        this.PERSISTENCE_SERVICE = persistenceService;
        this.SMART_HOME_SERVICE = smartHomeService;
    }

    @Operation(summary = "Create a new device")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Device created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/devices")
    public ResponseEntity<Void> createDevice(@Valid @RequestBody DeviceCreationRequest request) {
        String id = SMART_HOME_SERVICE.createAndAddDevice(request);
        PERSISTENCE_SERVICE.save();
        return ResponseEntity
                .created(URI.create("/api/devices/" + id))
                        .build();
    }

    @Operation(summary = "Perform an action on a device")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Action performed successfully"),
        @ApiResponse(responseCode = "404", description = "Device not found or action failed")
    })
    @PostMapping("/devices/{id}/actions")
    public ResponseEntity<DeviceResult> performDeviceAction(@PathVariable String id, @Valid @RequestBody DeviceActionRequest request) {
        DeviceResult result = SMART_HOME_SERVICE.performDeviceAction(id, request);
        if(result.success()) {
            AUDIT_LOG.record(id, result.message());
        }
        if(!result.success()) {
            return ResponseEntity.badRequest().body(result);
        }
        PERSISTENCE_SERVICE.save();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get action history for all devices")
    @ApiResponse(responseCode = "200", description = "List of all audit log entries")
    @GetMapping("/devices/history")
    public ResponseEntity<List<AuditEntry>> getDeviceHistory() {
        return ResponseEntity.ok(AUDIT_LOG.getAll());
    }

    @Operation(summary = "Clear all device action history")
    @ApiResponse(responseCode = "204", description = "History cleared successfully")
    @DeleteMapping("/devices/history")
    public ResponseEntity<Void> clearDeviceHistory() {
        AUDIT_LOG.clear();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a device")
    @ApiResponse(responseCode = "204", description = "Device deleted successfully")
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> removeDevice(@PathVariable String id) {
        SMART_HOME_SERVICE.removeDevice(id);
        PERSISTENCE_SERVICE.save();
        return ResponseEntity
                .noContent()
                        .build();
    }

    @Operation(summary = "Update the simulation tick rate")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Simulation updated"),
        @ApiResponse(responseCode = "500", description = "Update failed")
    })
    @PostMapping("/simulation/update")
    public ResponseEntity<Collection<EnvironmentStatus>> updateSimulation(@RequestBody int tickRate) {
        SMART_HOME_SERVICE.update(tickRate);
        PERSISTENCE_SERVICE.save();
        return ResponseEntity.ok(SMART_HOME_SERVICE.getEnvironmentStatus());
    }

    @Operation(summary = "Query environment statuses")
    @ApiResponse(responseCode = "200", description = "List of environment statuses")
    @PostMapping("/environments/status")
    public ResponseEntity<Collection<EnvironmentStatus>> queryEnvironments(@Valid @RequestBody DeviceFilterRequest request) {
        return ResponseEntity.ok(SMART_HOME_SERVICE.queryEnvironmentStatus(request));
    }

    @Operation(summary = "Get all environment names")
    @ApiResponse(responseCode = "200", description = "List of environment names")
    @GetMapping("/environments")
    public ResponseEntity<Collection<String>> getEnvironmentNames() {
        return ResponseEntity.ok(SMART_HOME_SERVICE.getEnvironmentNames());
    }

    @Operation(summary = "Get devices that can be updated")
    @ApiResponse(responseCode = "200", description = "List of updateable devices")
    @GetMapping("/devices/updateable")
    public ResponseEntity<Collection<DeviceStatus>> getUpdateableDevices() {
        return ResponseEntity.ok(SMART_HOME_SERVICE.getUpdateableDeviceStatus());
    }

    @Operation(summary = "Reset all devices to their initial state")
    @ApiResponse(responseCode = "204", description = "All devices reset successfully")
    @PostMapping("/devices/reset")
    public ResponseEntity<Void> resetDevices() {
        SMART_HOME_SERVICE.resetAllDevices();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set ambient temperature for an environment")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Ambient temperature set successfully"),
        @ApiResponse(responseCode = "404", description = "Environment not found")
    })
    @PostMapping("/environments/{name}/ambient")
    public ResponseEntity<Void> setAmbientTemperature(@PathVariable String name, @RequestBody int temperature) {
        SMART_HOME_SERVICE.setAmbientTemperature(name, temperature);
        return ResponseEntity.noContent().build();
    }
}
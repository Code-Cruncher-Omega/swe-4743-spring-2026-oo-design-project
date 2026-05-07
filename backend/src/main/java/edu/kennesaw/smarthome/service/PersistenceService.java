package edu.kennesaw.smarthome.service;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.service.dto.DeviceSnapshot;

@Service
public class PersistenceService {

    private static final String DEVICES_PATH = "data/devices.json";

    private final ObjectMapper MAPPER;
    private final SmartHomeService SMART_HOME_SERVICE;

    public PersistenceService(SmartHomeService smartHomeService) {
        this.SMART_HOME_SERVICE = smartHomeService;
        this.MAPPER = new ObjectMapper();
        this.MAPPER.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void load() {
        File file = new File(DEVICES_PATH);
        if (!file.exists() || file.length() == 0) return;
        try {
            DeviceSnapshot[] snapshots = MAPPER.readValue(file, DeviceSnapshot[].class);
            for (DeviceSnapshot snapshot : snapshots) {
                SMART_HOME_SERVICE.createAndAddDevice(snapshot);
            }
        } catch (IOException e) {
            System.err.println("Failed to load devices: " + e.getMessage());
        }
    }

    @PreDestroy
    public void save() {
        try {
            File file = new File(DEVICES_PATH);
            file.getParentFile().mkdirs();
            MAPPER.writerWithDefaultPrettyPrinter()
                  .writeValue(file, SMART_HOME_SERVICE.getDeviceSnapshots());
        } catch (IOException e) {
            System.err.println("Failed to save devices: " + e.getMessage());
        }
    }
}
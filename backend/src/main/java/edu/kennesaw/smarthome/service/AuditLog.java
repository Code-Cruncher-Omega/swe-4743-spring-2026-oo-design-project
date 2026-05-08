package edu.kennesaw.smarthome.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import edu.kennesaw.smarthome.dto.AuditEntry;

import org.springframework.stereotype.Component;

@Component
public class AuditLog {
    private static final String LOG_PATH = "audit-log.json";
    private final ObjectMapper MAPPER;

    public AuditLog() {
        this.MAPPER = new ObjectMapper();
        this.MAPPER.registerModule(new JavaTimeModule());
    }

    private List<AuditEntry> readAll() {
        File file = new File(LOG_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return MAPPER.readValue(file, new TypeReference<List<AuditEntry>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void writeAll(List<AuditEntry> entries) {
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(LOG_PATH), entries);
        } catch (IOException e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }

    public void record(String id, String operation) {
        List<AuditEntry> entries = readAll();
        entries.add(AuditEntry.of(id, operation));
        writeAll(entries);
    }

    public void clear() {
        writeAll(new ArrayList<>());
    }

    public List<AuditEntry> getHistory(String id) {
        return readAll().stream()
                .filter(entry -> entry.id().equals(id))
                .toList();
    }

    public List<AuditEntry> getAll() {
        return readAll();
    }
}
package edu.kennesaw.smarthome.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kennesaw.smarthome.dto.AuditEntry;

class AuditLogTests {

    private static final Path LOG_FILE = Path.of("audit-log.json");
    private final AuditLog auditLog = new AuditLog();

    @BeforeEach
    void cleanupBefore() throws IOException {
        Files.deleteIfExists(LOG_FILE);
    }

    @AfterEach
    void cleanupAfter() throws IOException {
        Files.deleteIfExists(LOG_FILE);
    }

    @Test
    void record_StoresEntryWithCorrectDeviceIdAndDescription() {
        auditLog.record("device-1", "power toggled");

        List<AuditEntry> entries = auditLog.getAll();
        assertEquals(1, entries.size());

        AuditEntry entry = entries.get(0);
        assertEquals("device-1", entry.id());
        assertEquals("power toggled", entry.operation());
        assertNotNull(entry.timestamp());
    }

    @Test
    void getHistory_ReturnsOperationsInChronologicalOrder() throws InterruptedException {
        auditLog.record("device-1", "first operation");
        Thread.sleep(5);
        auditLog.record("device-1", "second operation");

        List<AuditEntry> allEntries = auditLog.getAll();
        assertEquals(2, allEntries.size());
        assertEquals("first operation", allEntries.get(0).operation());
        assertEquals("second operation", allEntries.get(1).operation());

        List<AuditEntry> history = auditLog.getHistory("device-1");
        assertEquals(2, history.size());
        assertEquals("first operation", history.get(0).operation());
        assertEquals("second operation", history.get(1).operation());
    }
}

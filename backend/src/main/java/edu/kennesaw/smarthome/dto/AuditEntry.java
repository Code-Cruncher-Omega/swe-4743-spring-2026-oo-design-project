package edu.kennesaw.smarthome.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "A single audit log entry for a device operation")
public record AuditEntry(

    @Schema(description = "When the operation occurred")
    Instant timestamp,

    @Schema(description = "ID of the device affected")
    String id,
    
    @Schema(
        description = "Description of the operation performed", 
        example = "power on"
    )
    String operation
) {
    public static AuditEntry of(String deviceId, String operation) {
        return new AuditEntry(Instant.now(), deviceId, operation);
    }
}
package edu.kennesaw.smarthome.service.dto;

import java.util.Map;

public record DeviceSnapshot(
    String id,
    String name,
    String location,
    String state,
    String deviceType,
    Map<String, String> attributes
) {}
package edu.kennesaw.smarthome.dto;

import java.util.Map;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;

public record DeviceSnapshot(

    String id,
    String name,
    String location,
    String state,
    DeviceType deviceType,
    Map<String, Object> attributes
) {}
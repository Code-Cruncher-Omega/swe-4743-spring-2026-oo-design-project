package edu.kennesaw.smarthome.application.dto;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

// Record used to specify the parameters for each filtering method contained in EnvironmentDeviceQueryService.
// Filtering should work with null inputs in the record.
public record EnvironmentDeviceQueryRequest(String location, StateActivity activity, DeviceType type) {}

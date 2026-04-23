package edu.kennesaw.smarthome.application.dto;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

public record EnvironmentDeviceQueryRequest(String location, StateActivity activity, DeviceType type) {}

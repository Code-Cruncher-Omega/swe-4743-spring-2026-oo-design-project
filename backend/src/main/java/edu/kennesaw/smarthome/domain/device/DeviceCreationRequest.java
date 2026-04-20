package edu.kennesaw.smarthome.domain;

import edu.kennesaw.smarthome.domain.device.DeviceType;

public record DeviceCreationRequest(String name, String location, DeviceType deviceType) {}

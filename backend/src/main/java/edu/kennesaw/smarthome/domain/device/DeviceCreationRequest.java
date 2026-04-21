package edu.kennesaw.smarthome.domain.device;

public record DeviceCreationRequest(String name, String location, DeviceType deviceType) {}

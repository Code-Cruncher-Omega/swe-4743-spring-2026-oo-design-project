package edu.kennesaw.smarthome.domain.device.abstraction;

// Record used to pass creation requests to a DeviceFactory, which is then passed down to its
// respective device creator.
public record DeviceCreationRequest(String name, String location, DeviceType deviceType) {}

package edu.kennesaw.smarthome.service.dto;

// Record used to pass creation requests to a DeviceFactory, which is then passed down to its
// respective device creator. Used by users to request the controller to make a device.
public record DeviceCreationRequest(String name, String location, String deviceType) {}

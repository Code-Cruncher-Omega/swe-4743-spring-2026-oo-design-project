package edu.kennesaw.smarthome.service.dto;

// Used to pass messages from a device to a higher class (i.e. environment or simulation).
public record DeviceResult(boolean success, String action, String message) {}

package edu.kennesaw.smarthome.service.dto;

// Record used to specify the parameters for each filtering method contained in EnvironmentDeviceQueryService.
// Filtering should work with null inputs in the record.
public record DeviceFilterRequest(String location, String activity, String type) {}

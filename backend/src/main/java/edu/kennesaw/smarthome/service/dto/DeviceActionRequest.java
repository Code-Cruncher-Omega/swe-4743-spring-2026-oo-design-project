package edu.kennesaw.smarthome.service.dto;

public record DeviceActionRequest(String action, Object... parameters) {}

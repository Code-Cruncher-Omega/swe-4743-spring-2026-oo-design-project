package edu.kennesaw.smarthome.service.dto;

// Pass an action name, and any number of parameters to the device which will perform an action.
// Used for communication between controller and users.
public record DeviceActionRequest(String action, Object... parameters) {}

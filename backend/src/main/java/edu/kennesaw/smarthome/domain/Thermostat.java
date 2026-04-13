package edu.kennesaw.smarthome.domain;

public class Thermostat implements Device {
    // Metadata
    private final String UUID;
    private String name;
    private String location;
    private final Type TYPE;

    // Thermostat-specific variables
    private String mode; // Values: "HEAT", "COOL", "AUTO" only
    private byte desiredTemperature; // Values: 60 - 80 only
    private byte ambientTemperature;
}
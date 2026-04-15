package edu.kennesaw.smarthome.domain.Devices;

import java.util.UUID;

public abstract class Device {
    // Common Device Metadata //

    private final UUID ID;
    private final String NAME;
    private final String LOCATION;
    public enum Type {DOOR_LOCK, FAN, LIGHT, THERMOSTAT};
    private final Type TYPE;

    // Constructor
    public Device(String name, String location, Type type) {
        this.ID = UUID.randomUUID();
        this.NAME = name;
        this.LOCATION = location;
        this.TYPE = type;
    }

    // Getters //
    
    public UUID getID() {
        return ID;
    }

    public String getName() {
        return NAME;
    }

    public String getLocation() {
        return LOCATION;
    }

    public Type getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s) - Type: %s, Location: %s", NAME, ID, TYPE, LOCATION);
    }
}
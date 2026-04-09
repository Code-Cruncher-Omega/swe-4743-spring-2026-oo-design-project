interface Device {
    // Common Metadata for all devices
    private final String UUID;
    private String name;
    private String location;
    public enum Type {DOOR_LOCK, FAN, LIGHT, THERMOSTAT};
    private final Type TYPE;

    // Metadata Getters
    public String getUUID() {
        return UUID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Type getType() {
        return TYPE;
    }
}
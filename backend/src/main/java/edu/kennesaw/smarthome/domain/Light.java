public class Light implements Device {
    // Metadata
    private final String UUID;
    private String name;
    private String location;
    private final Type TYPE;

    // Light-specific variables
    private byte int brightness; // Values 10-100 only
    private byte int[3] color;  // RGB values, each 0-255 only
    private LightState state;
}
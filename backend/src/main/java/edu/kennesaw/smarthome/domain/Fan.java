public class Fan implements Device {
    // Metadata
    private final String UUID;
    private String name;
    private String location;
    private final Type TYPE;

    // Fan-specific variables
    private String speed; // Values: "LOW", "MEDIUM", "HIGH" only
}
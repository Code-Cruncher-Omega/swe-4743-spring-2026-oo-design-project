package edu.kennesaw.smarthome.domain.device;

import java.util.UUID;

public abstract class Device<
    D extends Device<D, S, A>, 
    S extends DeviceState<D, A>, 
    A extends Action> {
    // Common Metadata and methods for all devices //

    private final UUID ID;
    private final String NAME;
    private final String LOCATION;
    // DeviceType is not stored as a field, but is retrieved via the getType() method, which is implemented by each concrete device class.

    protected S state;

    public Device(String name, String location, S state) {
        this.ID = UUID.randomUUID();
        this.NAME = name;
        this.LOCATION = location;
        this.state = state;
    }

    public UUID getId() {
        return ID;
    }

    public String getName() {
        return NAME;
    }

    public String getLocation() {
        return LOCATION;
    }

    public S getState() {
        return state;
    }

    // Abstract methods to be implemented by concrete device classes //
    // Device-specific actions and states should be the input for these methods, allowing for flexible and extensible device behavior.

    // The execute method will delegate the action execution to the current state of the device, allowing for state-specific behavior.
    protected abstract ActionResult execute(A action);

    protected abstract void setState(S newState);

    // Each concrete device class will implement this method to return its specific DeviceType, which can be used for categorization and handling of different device types in the system.
    public abstract DeviceType getType();

    // The reset method will be implemented by each concrete device class to define how the device should reset itself to a default state.
    public abstract ActionResult reset();

    @Override
    public String toString() {
        return String.format("%s (ID: %s, Location: %s, State: %s)", NAME, ID, LOCATION, state.getClass().getSimpleName());
    }
}
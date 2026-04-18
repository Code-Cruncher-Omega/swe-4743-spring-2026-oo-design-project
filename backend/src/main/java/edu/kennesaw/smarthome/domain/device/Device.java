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

    private S state;

    public Device(String name, String location, S state) {
        this.ID = UUID.randomUUID();
        this.NAME = name;
        this.LOCATION = location;
        this.state = state;
    }

    // Should not be used by the device itself, but rather by the state implementations to change the device's current state.
    protected void setState(S state) {
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
    public abstract ActionResult execute(A action);

    public abstract DeviceType getType();

    // The reset method will be implemented by each concrete device class to define how the device should reset itself to a default state.
    public abstract ActionResult reset();
}
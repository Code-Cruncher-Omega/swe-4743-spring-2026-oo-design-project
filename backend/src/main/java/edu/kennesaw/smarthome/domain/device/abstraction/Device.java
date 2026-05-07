package edu.kennesaw.smarthome.domain.device.abstraction;

import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.service.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.service.dto.DeviceResult;
import edu.kennesaw.smarthome.service.dto.DeviceStatus;

// All concrete Devices store all these methods and variables, while also needing to implement the mentioned abstract methods.
public abstract class Device<
    D extends Device<D, S, A, T>, 
    S extends DeviceState<D, A, T>, 
    A extends DeviceAction,
    T extends DeviceStateType> {
    // Common Metadata and methods for all devices //

    private final UUID ID;
    private final String NAME;
    private final String LOCATION;
    // DeviceType is not stored as a field, but is retrieved via the getType() method, which is implemented by each concrete device class.

    protected S state;

    protected final Map<String, S> STATES;
    
    public Device(  UUID id,
                    String name, 
                    String location, 
                    
                    S state, 
                    
                    Map<String, S> states) {
        this.ID = id;
        this.NAME = name;
        this.LOCATION = location;

        this.state = state;

        this.STATES = states;
    }

    public Device(  String name, 
                    String location, 
                    
                    S state, 
                    
                    Map<String, S> states) {
        this.ID = UUID.randomUUID();
        this.NAME = name;
        this.LOCATION = location;

        this.state = state;

        this.STATES = states;
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

    // DeviceStatus contains basic facts about Device, such as the id, name, location, state, and type.
    // Other attributes are gathered by each Device's unique implementation of getAttrubutes, which DeviceStatus's from-method
    // already calls to gather such information.
    public DeviceStatus getStatus() {
        return DeviceStatus.of(this);
    }

    // Abstract methods to be implemented by concrete device classes //
    // Device-specific actions and states should be the input for these methods, allowing for flexible and extensible device behavior.

    // The execute method will delegate the action execution to the current state of the device, allowing for state-specific behavior.
    protected abstract DeviceResult execute(A action);

    protected abstract void setState(S newState);

    // Each concrete device needs to set this up so that their methods can be called by users via API.
    public abstract DeviceResult performAction(DeviceActionRequest action);

    // Each concrete device class will implement this method to return its specific DeviceType, which can be used for categorization and handling of different device types in the system.
    public abstract DeviceType getType();

    // A getter that assists getStatus report all aspects of the Device. It should return a Map of attributes not listed in DeviceStatus, such as modes and device specific variables.
    // The key String should be lowercase.
    public abstract Map<String, String> getAttributes();

    // The reset method will be implemented by each concrete device class to define how the device should reset itself to a default state.
    public abstract DeviceResult reset();

    @Override
    public String toString() {
        return String.format("Device: %s (ID: %s, Location: %s, State: %s)", NAME, ID, LOCATION, (state.getStateType()).toString());
    }
}
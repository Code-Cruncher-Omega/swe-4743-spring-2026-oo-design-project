package edu.kennesaw.smarthome.domain;

import org.springframework.stereotype.Component;

@Component
public class DoorLock extends Device {
    // DoorLock-specific variables
    private DoorLockState state;
    public enum DoorLockActions {LOCK, UNLOCK};

    // Constructor
    public DoorLock(String name, String location, Type type, DoorLockState initialState) {
        super(name, location, Type.DOOR_LOCK);
        this.state = initialState;
    }
    
    // Methods
    public DeviceTransitionResult execute(DoorLockActions action) {
        return state.execute(action, this);
    }
}
package edu.kennesaw.smarthome.domain.Devices.DoorLock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Device;

@Component
public class DoorLock extends Device {
    // DoorLock-specific variables //

    private DoorLockState state;
    public enum DoorLockAction {LOCK, UNLOCK};  // All actions that can be performed

    // Constructor
    public DoorLock(String name, String location, Type type, DoorLockState initialState) {
        super(name, location, Type.DOOR_LOCK);
        this.state = initialState;
    }
    
    // Methods //

    // Pass action onto the state object to handle and return the result
    public ActionResult execute(DoorLockAction action) {
        return state.execute(action, this);
    }

    public String getStateName() {
        return state.getName();
    }

    // Allow state objects to transition the context's state object to another
    protected void setState(DoorLockState newState) {
        this.state = newState;
    }
}
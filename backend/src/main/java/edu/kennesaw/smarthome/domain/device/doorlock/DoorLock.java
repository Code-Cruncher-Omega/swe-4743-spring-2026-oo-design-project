package edu.kennesaw.smarthome.domain.device.doorlock;

import java.util.HashMap;
import java.util.Map;

import edu.kennesaw.smarthome.service.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;

public class DoorLock extends Device<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> {

    private final DoorLockState INITIAL_STATE;  // Stores the initial state, used for resetting the device to its default state.

    public DoorLock(String name, 
                    String location, 
                    DoorLockState initialState, 
                    Map<DoorLockStateType, DoorLockState> states) {
        super(name, location, initialState, states);
        
        this.INITIAL_STATE = initialState;
    }

    @Override
    // Delegate the action execution to the current state of the door lock, allowing for state-specific behavior.
    protected DeviceResult execute(DoorLockAction action) {
        return state.execute(this, action);
    }

    protected DoorLockState getLockedState() {
        return STATES.get(DoorLockStateType.LOCKED);
    }

    protected DoorLockState getUnlockedState() {
        return STATES.get(DoorLockStateType.UNLOCKED);
    }

    @Override
    protected void setState(DoorLockState newState) {
        this.state = newState;
    }

    @Override
    public DeviceResult performAction(DeviceActionRequest action) {
        switch(action.action()) {
            case "TOGGLE_LOCK": // DoorLockAction.TOGGLE_LOCK
                return toggleLock();
            default:
                return new DeviceResult(false, action.action().toString(), "Action unavailable for " + getType().name());
        }
    }
    
    @Override
    public DeviceType getType() {
        return DeviceType.DOOR_LOCK;
    }

    @Override
    public Map<String, String> getAttributes() {
        return new HashMap<>(); // No attributes, so return an empty HashMap.
    }

    @Override
    public DeviceResult reset() {
        state = INITIAL_STATE; // Reset to the initial state
        return new DeviceResult(true, "RESET_DOOR_LOCK", "Door lock reset to initial state.");
    }

    // Convenience methods for common actions, which internally call the execute method with the appropriate action.
    public DeviceResult toggleLock() {
        return execute(DoorLockAction.TOGGLE_LOCK);
    }
}

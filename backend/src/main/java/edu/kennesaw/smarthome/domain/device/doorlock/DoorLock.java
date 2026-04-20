package edu.kennesaw.smarthome.domain.device.doorlock;

import java.util.Map;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class DoorLock extends Device<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> {

    private final DoorLockState INITIAL_STATE;  // Stores the initial state, used for resetting the device to its default state.
    
    private final Map<DoorLockStateType, DoorLockState> STATES;

    public DoorLock(String name, 
                    String location, 

                    DoorLockState initialState, 
                    Map<DoorLockStateType, DoorLockState> states) {
        super(name, location, initialState);
        
        this.INITIAL_STATE = initialState;
        this.STATES = states;
    }

    @Override
    // Delegate the action execution to the current state of the door lock, allowing for state-specific behavior.
    protected ActionResult execute(DoorLockAction action) {
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
    public DeviceType getType() {
        return DeviceType.DOOR_LOCK;
    }

    @Override
    public ActionResult reset() {
        state = INITIAL_STATE; // Reset to the initial state
        return new ActionResult(true, "RESET_DOOR_LOCK", "Door lock reset to initial state.");
    }

    // Convenience methods for common actions, which internally call the execute method with the appropriate action.
    public ActionResult toggleLock() {
        return execute(DoorLockAction.TOGGLE_LOCK);
    }
}

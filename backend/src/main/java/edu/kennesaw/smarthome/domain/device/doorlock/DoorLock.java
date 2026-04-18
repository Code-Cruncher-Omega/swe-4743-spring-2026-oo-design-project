package edu.kennesaw.smarthome.domain.device.doorlock;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class DoorLock extends Device<DoorLock, DoorLockState, DoorLockAction> {

    private final DoorLockState INITIAL_STATE;  // Stores the initial state, used for resetting the device to its default state.
    private final DoorLockState LOCKED_STATE;
    private final DoorLockState UNLOCKED_STATE;

    public DoorLock(String name, String location, DoorLockState initialState, DoorLockState lockedState, DoorLockState unlockedState) {
        super(name, location, initialState);
        this.INITIAL_STATE = initialState;
        this.LOCKED_STATE = lockedState;
        this.UNLOCKED_STATE = unlockedState;
    }

    @Override
    // Delegate the action execution to the current state of the door lock, allowing for state-specific behavior.
    protected ActionResult execute(DoorLockAction action) {
        return state.execute(this, action);
    }

    protected DoorLockState getLockedState() {
        return LOCKED_STATE;
    }

    protected DoorLockState getUnlockedState() {
        return UNLOCKED_STATE;
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
    public ActionResult lock() {
        return execute(DoorLockAction.LOCK);
    }

    public ActionResult unlock() {
        return execute(DoorLockAction.UNLOCK);
    }
}

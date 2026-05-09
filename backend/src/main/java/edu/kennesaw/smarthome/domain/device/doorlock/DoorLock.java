package edu.kennesaw.smarthome.domain.device.doorlock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.service.creator.DoorLockCreator;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.dto.DeviceResult;

public class DoorLock extends Device<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> {

    public DoorLock(UUID id,
                    String name, 
                    String location, 
                    DoorLockState savedState, 
                    Map<DoorLockStateType, DoorLockState> states) {
        super(id, name, location, savedState, states);
    }

    public DoorLock(String name, 
                    String location, 
                    DoorLockState initialState, 
                    Map<DoorLockStateType, DoorLockState> states) {
        super(name, location, initialState, states);
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
        return execute(DoorLockAction.from(action.action()));
    }
    
    @Override
    public DeviceType getType() {
        return DeviceType.DOOR_LOCK;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>(); // No attributes, so return an empty HashMap.
    }

    @Override
    public void reset() {
        state = STATES.get(DoorLockCreator.initialState()); // Reset to the initial state
    }

    // Convenience methods for common actions, which internally call the execute method with the appropriate action.
    public DeviceResult toggleLock() {
        return execute(DoorLockAction.TOGGLE_LOCK);
    }
}

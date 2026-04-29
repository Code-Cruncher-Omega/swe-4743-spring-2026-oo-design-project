package edu.kennesaw.smarthome.domain.device.doorlock;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

public class UnlockedState implements DoorLockState {
    @Override
    public DeviceResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case TOGGLE_LOCK:
                context.setState(context.getLockedState());
                return new DeviceResult(true, "TOGGLE_DOOR_LOCK", "Door lock toggled to locked state.");
            default:
                return new DeviceResult(false, action.name(), "Action not valid for door lock in unlocked state.");
        }
    }
    @Override
    public DoorLockStateType getStateType() {
        return DoorLockStateType.UNLOCKED;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.ON;
    }
}

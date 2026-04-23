package edu.kennesaw.smarthome.domain.device.doorlock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

@Component
public class LockedState implements DoorLockState {
    @Override
    public DeviceResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case TOGGLE_LOCK:
                context.setState(context.getUnlockedState());
                return new DeviceResult(true, "TOGGLE_DOOR_LOCK", "Door lock toggled to unlocked state.");
            default:
                return new DeviceResult(false, action.name(), "Action not valid for door lock in locked state.");
        }
    }
    @Override
    public DoorLockStateType getStateType() {
        return DoorLockStateType.LOCKED;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.ON;
    }
}

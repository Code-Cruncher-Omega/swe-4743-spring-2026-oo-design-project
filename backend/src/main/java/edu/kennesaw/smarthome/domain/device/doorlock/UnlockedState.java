package edu.kennesaw.smarthome.domain.device.doorlock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.dto.DeviceResult;

@Component("doorLockUnlockedState")
public class UnlockedState implements DoorLockState {
    @Override
    public DeviceResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case TOGGLE_LOCK:
                context.setState(context.getLockedState());
                return new DeviceResult(true, action, context.getName() + " toggled to locked state.");
            default:
                throw new IllegalStateException("Action not valid for " + context.getName() + " in unlocked state.");
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

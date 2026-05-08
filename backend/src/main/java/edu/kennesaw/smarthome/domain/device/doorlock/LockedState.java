package edu.kennesaw.smarthome.domain.device.doorlock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.dto.DeviceResult;

@Component("doorLockLockedState")
public class LockedState implements DoorLockState {
    @Override
    public DeviceResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case TOGGLE_LOCK:
                context.setState(context.getUnlockedState());
                return new DeviceResult(true, action, context.getName() + " toggled to unlocked state.");
            default:
                return new DeviceResult(false, action, "Action not valid for " + context.getName() + " in locked state.");
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

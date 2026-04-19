package edu.kennesaw.smarthome.domain.device.doorlock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
public class LockedState implements DoorLockState {
    @Override
    public ActionResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case TOGGLE_LOCK:
                context.setState(context.getUnlockedState());
                return new ActionResult(true, "TOGGLE_DOOR_LOCK", "Door lock toggled to UNLOCKED state.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for door lock in LOCKED state.");
        }
    }

    @Override
    public String getStateName() {
        return "Locked";
    }
}

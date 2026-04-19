package edu.kennesaw.smarthome.domain.device.doorlock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
public class UnlockedState implements DoorLockState {
    @Override
    public ActionResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case TOGGLE_LOCK:
                context.setState(context.getLockedState());
                return new ActionResult(true, "TOGGLE_DOOR_LOCK", "Door lock toggled to LOCKED state.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for door lock in UNLOCKED state.");
        }
    }

    @Override
    public String getStateName() {
        return "Unlocked";
    }
}

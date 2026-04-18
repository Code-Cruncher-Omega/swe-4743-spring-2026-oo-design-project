package edu.kennesaw.smarthome.domain.device.doorlock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
public class LockedState implements DoorLockState {
    @Override
    public ActionResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case UNLOCK:
                context.setState(context.getUnlockedState()); // Transition to the unlocked state
                return new ActionResult(true, "UNLOCK_DOOR_LOCK", "Door unlocked successfully.");
            case LOCK:
                return new ActionResult(false, "LOCK_DOOR_LOCK", "Door is already locked.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for door lock in LOCKED state.");
        }
    }
}

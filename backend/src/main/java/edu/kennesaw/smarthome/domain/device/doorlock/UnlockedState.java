package edu.kennesaw.smarthome.domain.device.doorlock;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
public class UnlockedState implements DoorLockState {
    @Override
    public ActionResult execute(DoorLock context, DoorLockAction action) {
        switch (action) {
            case LOCK:
                context.setState(context.getLockedState()); // Transition to the locked state
                return new ActionResult(true, "LOCK_DOOR_LOCK", "Door locked successfully.");
            case UNLOCK:
                return new ActionResult(false, "UNLOCK_DOOR_LOCK", "Door is already unlocked.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for door lock in UNLOCKED state.");
        }
    }
}

package edu.kennesaw.smarthome.domain.Devices.DoorLock;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.DoorLock.DoorLock.DoorLockAction;

public class Unlocked implements DoorLockState {
    @Override
    public String getName() {
        return "Unlocked";
    }

    @Override
    public ActionResult execute(DoorLockAction action, DoorLock context) {
        switch (action) {
            case LOCK:
                context.setState(new Locked());
                return new ActionResult(true, "LOCK", "Door locked successfully.");
            case UNLOCK:
                return new ActionResult(false, "UNLOCK", "Door is already unlocked.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Door Lock.");
        }
    }
    
}

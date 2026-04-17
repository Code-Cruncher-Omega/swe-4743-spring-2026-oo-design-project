package edu.kennesaw.smarthome.domain.Devices.DoorLock;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.DoorLock.DoorLock.DoorLockAction;

public class Locked implements DoorLockState {
    @Override
    public String getName() {
        return "Locked";
    }

    @Override
    public ActionResult execute(DoorLockAction action, DoorLock context) {
        switch (action) {
            case UNLOCK:
                context.setState(new Unlocked());
                return new ActionResult(true, "UNLOCK", "Door unlocked successfully.");
            case LOCK:
                return new ActionResult(false, "LOCK", "Door is already locked.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Door Lock.");
        }
    }
    
}
package edu.kennesaw.smarthome.domain.Devices.DoorLock;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.State;

public interface DoorLockState extends State {
    public ActionResult execute(DoorLock.DoorLockAction action, DoorLock context);
}

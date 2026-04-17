package edu.kennesaw.smarthome.domain.Devices.DoorLock;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.State;
import edu.kennesaw.smarthome.domain.Devices.DoorLock.DoorLock.DoorLockAction;

public interface DoorLockState extends State {
    public ActionResult execute(DoorLockAction action, DoorLock deviceContext);
}

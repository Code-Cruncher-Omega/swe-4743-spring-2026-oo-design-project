package edu.kennesaw.smarthome.domain.device.doorlock;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface DoorLockState extends DeviceState<DoorLock, DoorLockAction> {
    @Override
    ActionResult execute(DoorLock context, DoorLockAction action);
}

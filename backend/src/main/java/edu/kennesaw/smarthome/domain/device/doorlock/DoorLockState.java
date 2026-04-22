package edu.kennesaw.smarthome.domain.device.doorlock;

import edu.kennesaw.smarthome.domain.device.DeviceResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface DoorLockState extends DeviceState<DoorLock, DoorLockAction, DoorLockStateType> {
    @Override
    public DeviceResult execute(DoorLock context, DoorLockAction action);
    @Override
    public DoorLockStateType getStateType();
}

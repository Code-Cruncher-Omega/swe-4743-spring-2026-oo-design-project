package edu.kennesaw.smarthome.domain.device.doorlock;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceState;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

public interface DoorLockState extends DeviceState<DoorLock, DoorLockAction, DoorLockStateType> {
    @Override
    public DeviceResult execute(DoorLock context, DoorLockAction action);
    @Override
    public DoorLockStateType getStateType();
}

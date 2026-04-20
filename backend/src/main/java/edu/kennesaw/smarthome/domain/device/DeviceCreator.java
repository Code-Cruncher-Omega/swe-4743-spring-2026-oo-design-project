package edu.kennesaw.smarthome.domain;

import edu.kennesaw.smarthome.domain.device.DeviceAction;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceState;
import edu.kennesaw.smarthome.domain.device.DeviceStateType;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public interface DeviceCreator<D extends Device<D, S, A, T>, S extends DeviceState<D, A, T>, A extends DeviceAction, T extends DeviceStateType> {
    public Device<D, S, A, T> createDevice(DeviceCreationRequest request);
    public DeviceType getDeviceType();
}

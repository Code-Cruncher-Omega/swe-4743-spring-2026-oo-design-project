package edu.kennesaw.smarthome.domain.device;

public interface DeviceCreator<D extends Device<D, S, A, T>, S extends DeviceState<D, A, T>, A extends DeviceAction, T extends DeviceStateType> {
    public Device<D, S, A, T> createDevice(DeviceCreationRequest request);
    public DeviceType getDeviceType();
}

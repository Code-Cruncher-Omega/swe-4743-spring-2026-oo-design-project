package edu.kennesaw.smarthome.domain.device.abstraction;

// Each device creator will need to implement this interface so that a DeviceFactory can create that kind of device.
// Device creators are passed through as parameters for DeviceFactory.
public interface DeviceCreator<D extends Device<D, S, A, T>, S extends DeviceState<D, A, T>, A extends DeviceAction, T extends DeviceStateType> {
    // Method returns a device based on its specifications listed in the request parameter.
    public Device<D, S, A, T> createDevice(DeviceCreationRequest request);
    // Used by DeviceFactory as a key to find the proper creator for the device being created.
    public DeviceType getDeviceType();
}

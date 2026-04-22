package edu.kennesaw.smarthome.domain.device;


public interface DeviceState<D extends Device<D, ?, A, T>, A extends DeviceAction, T extends DeviceStateType> {
    // Each state will implement this method to handle the actions that are valid for that state.
    // The device context is passed in so that the state can modify the device's properties if needed.
    public DeviceResult execute(D context, A action);
    // Used for passing as a key in a device's STATES map.
    public T getStateType();
}

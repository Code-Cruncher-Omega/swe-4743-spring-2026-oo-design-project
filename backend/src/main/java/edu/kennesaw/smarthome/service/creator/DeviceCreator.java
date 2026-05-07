package edu.kennesaw.smarthome.service.creator;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceAction;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceState;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceStateType;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.service.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.service.dto.DeviceSnapshot;

// Each device creator will need to implement this interface so that a DeviceFactory can create that kind of device.
// Device creators are passed through as parameters for DeviceFactory.
public interface DeviceCreator<D extends Device<D, S, A, T>, S extends DeviceState<D, A, T>, A extends DeviceAction, T extends DeviceStateType> {
    // Method returns a device based on its specifications listed in the request parameter.
    public Device<D, S, A, T> createDevice(DeviceCreationRequest request);
    // Recreate a device using precise values. Used for app rehydration.
    public Device<D, S, A, T> createDevice(DeviceSnapshot snapshot);
    // Used by DeviceFactory as a key to find the proper creator for the device being created.
    public DeviceType getDeviceType();
    // Device Creators should also implement static public methods that created Devices can reference their
    // initial values. This is so that rehydrated devices can reuse their initial state without having to store
    // the actual initial state provided by their creator.
}

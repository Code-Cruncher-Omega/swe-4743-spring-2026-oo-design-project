package edu.kennesaw.smarthome.domain.device.abstraction;

import edu.kennesaw.smarthome.dto.DeviceResult;

public interface UpdateableDevice {
    public DeviceResult update(int tickRate);
}

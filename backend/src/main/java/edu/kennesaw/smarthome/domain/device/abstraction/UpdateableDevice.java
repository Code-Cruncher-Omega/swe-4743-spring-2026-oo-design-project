package edu.kennesaw.smarthome.domain.device.abstraction;

import edu.kennesaw.smarthome.service.dto.DeviceResult;

public interface UpdateableDevice {
    public DeviceResult update(int tickRate);
}

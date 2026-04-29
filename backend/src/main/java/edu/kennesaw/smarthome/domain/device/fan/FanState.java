package edu.kennesaw.smarthome.domain.device.fan;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceState;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

public interface FanState extends DeviceState<Fan, FanAction, FanStateType> {
    @Override
    public DeviceResult execute(Fan context, FanAction action);
    @Override
    public FanStateType getStateType();
}

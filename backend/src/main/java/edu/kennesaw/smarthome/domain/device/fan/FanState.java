package edu.kennesaw.smarthome.domain.device.fan;

import edu.kennesaw.smarthome.domain.device.DeviceResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface FanState extends DeviceState<Fan, FanAction, FanStateType> {
    @Override
    public DeviceResult execute(Fan context, FanAction action);
    @Override
    public FanStateType getStateType();
}

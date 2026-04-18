package edu.kennesaw.smarthome.domain.device.fan;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface FanState extends DeviceState<Fan, FanAction> {
    @Override
    ActionResult execute(Fan context, FanAction action);
}

package edu.kennesaw.smarthome.domain.device.light;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface LightState extends DeviceState<Light, LightAction> {
    ActionResult execute(Light context, LightAction action, int brightnessLevel);
    ActionResult execute(Light context, LightAction action, int[] colorValues);
    @Override
    ActionResult execute(Light context, LightAction action);
}

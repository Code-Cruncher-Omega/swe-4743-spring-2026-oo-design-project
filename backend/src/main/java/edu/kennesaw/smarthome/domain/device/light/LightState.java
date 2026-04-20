package edu.kennesaw.smarthome.domain.device.light;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface LightState extends DeviceState<Light, LightAction> {
    public ActionResult execute(Light context, LightAction action, int brightnessLevel);
    public ActionResult execute(Light context, LightAction action, int[] colorValues);
    @Override
    public ActionResult execute(Light context, LightAction action);
}

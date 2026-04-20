package edu.kennesaw.smarthome.domain.device.light;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface LightState extends DeviceState<Light, LightAction, LightStateType> {
    // Different attributes for a Light object can be changed only during specific states, so methods dedicated to 
    // changing them must be handled by every state.
    public ActionResult execute(Light context, LightAction action, int brightnessLevel);
    public ActionResult execute(Light context, LightAction action, int[] colorValues);
    @Override
    public ActionResult execute(Light context, LightAction action);
    @Override
    public LightStateType getStateType();
}

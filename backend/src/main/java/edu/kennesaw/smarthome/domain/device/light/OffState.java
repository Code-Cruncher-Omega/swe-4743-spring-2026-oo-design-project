package edu.kennesaw.smarthome.domain.device.light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.DeviceResult;

@Component("lightOffState")
public class OffState implements LightState {
    @Override
    public DeviceResult execute(Light context, LightAction action, int brightnessLevel) {
        return new DeviceResult(false, action.name(), "Cannot set brightness while light is off. Please turn on the light first.");}
    @Override
    public DeviceResult execute(Light context, LightAction action, int[] colorValues) {
        return new DeviceResult(false, action.name(), "Cannot set color while light is off. Please turn on the light first.");
    }
    @Override
    public DeviceResult execute(Light context, LightAction action) {
        switch(action) {
            case TOGGLE_POWER:
                context.setState(context.getOnState()); // Transition to the on state
                return new DeviceResult(true, "TOGGLE_LIGHT_POWER", "Light turned on successfully.");
            default:
                return new DeviceResult(false, action.name(), "Action not valid for light in off state.");
        }
    }
    @Override
    public LightStateType getStateType() {
        return LightStateType.OFF;
    }
}

package edu.kennesaw.smarthome.domain.device.light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

@Component
public class OnState implements LightState {
    @Override
    public DeviceResult execute(Light context, LightAction action, int[] colorValues) {
        if (action == LightAction.SET_COLOR) {
            if (colorValues.length != 3 || 
                colorValues[0] < 0 || colorValues[0] > 255 || 
                colorValues[1] < 0 || colorValues[1] > 255 || 
                colorValues[2] < 0 || colorValues[2] > 255) {
                return new DeviceResult(false, action.name(), "Invalid light color values. RGB values must be between 0 and 255.");
            }
            context.setColor(colorValues);
            return new DeviceResult(true, "SET_LIGHT_COLOR", "Light color set to [" + colorValues[0] + ", " + colorValues[1] + ", " + colorValues[2] + "].");
        }
        return new DeviceResult(false, action.name(), "Improper action for setting light color."); // Invalid action for this method
    }
    @Override
    public DeviceResult execute(Light context, LightAction action, int brightnessLevel) {
        if (action == LightAction.SET_BRIGHTNESS) {
            if (brightnessLevel < 10 || brightnessLevel > 100) {
                return new DeviceResult(false, action.name(), "Invalid light brightness level. Must be between 10 and 100.");
            }
            context.setBrightness(brightnessLevel);
            return new DeviceResult(true, "SET_LIGHT_BRIGHTNESS", "Light brightness set to " + brightnessLevel + "%.");
        }
        return new DeviceResult(false, action.name(), "Improper action for setting light brightness."); // Invalid action for this method
    }
    @Override
    public DeviceResult execute(Light context, LightAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState()); // Transition to the off state
                return new DeviceResult(true, "TOGGLE_LIGHT_POWER", "Light turned off successfully.");
            default:
                return new DeviceResult(false, action.name(), "Action not valid for light in on state.");
        }
    }
    @Override
    public LightStateType getStateType() {
        return LightStateType.ON;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.ON;
    }
}

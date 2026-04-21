package edu.kennesaw.smarthome.domain.device.light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component("lightOnState")
public class OnState implements LightState {
    @Override
    public ActionResult execute(Light context, LightAction action, int[] colorValues) {
        if (action == LightAction.SET_COLOR) {
            if (colorValues.length != 3 || 
                colorValues[0] < 0 || colorValues[0] > 255 || 
                colorValues[1] < 0 || colorValues[1] > 255 || 
                colorValues[2] < 0 || colorValues[2] > 255) {
                return new ActionResult(false, action.name(), "Invalid light color values. RGB values must be between 0 and 255.");
            }
            context.setColor(colorValues);
            return new ActionResult(true, "SET_LIGHT_COLOR", "Light color updated successfully.");
        }
        return new ActionResult(false, action.name(), "Improper action for setting light color."); // Invalid action for this method
    }
    @Override
    public ActionResult execute(Light context, LightAction action, int brightnessLevel) {
        if (action == LightAction.SET_BRIGHTNESS) {
            if (brightnessLevel < 10 || brightnessLevel > 100) {
                return new ActionResult(false, action.name(), "Invalid light brightness level. Must be between 10 and 100.");
            }
            context.setBrightness(brightnessLevel);
            return new ActionResult(true, "SET_LIGHT_BRIGHTNESS", "Light brightness updated successfully.");
        }
        return new ActionResult(false, action.name(), "Improper action for setting light brightness."); // Invalid action for this method
    }
    @Override
    public ActionResult execute(Light context, LightAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState()); // Transition to the off state
                return new ActionResult(true, "TOGGLE_LIGHT_POWER", "Light turned off successfully.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for light in on state.");
        }
    }
    @Override
    public LightStateType getStateType() {
        return LightStateType.ON;
    }
}

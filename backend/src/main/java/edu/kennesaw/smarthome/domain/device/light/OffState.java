package edu.kennesaw.smarthome.domain.device.light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component("lightOffState")
public class OffState implements LightState {

    @Override
    public ActionResult execute(Light context, LightAction action, int brightnessLevel) {
        return new ActionResult(false, action.name(), "Cannot set brightness while light is off. Please turn on the light first.");}

    @Override
    public ActionResult execute(Light context, LightAction action, int[] colorValues) {
        return new ActionResult(false, action.name(), "Cannot set color while light is off. Please turn on the light first.");
    }

    @Override
    public ActionResult execute(Light context, LightAction action) {
        switch(action) {
            case TOGGLE_POWER:
                context.setState(context.getOnState()); // Transition to the on state
                return new ActionResult(true, "TOGGLE_LIGHT_POWER", "Light turned on successfully.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for light in off state.");
        }
    }
    
    @Override
    public String getStateName() {
        return "Off";
    }
}

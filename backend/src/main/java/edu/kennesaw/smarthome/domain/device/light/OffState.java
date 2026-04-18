package edu.kennesaw.smarthome.domain.device.light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
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
            case TURN_ON:
                context.setState(context.getOnState()); // Transition to the on state
                return new ActionResult(true, "TURN_ON_LIGHT", "Light turned on successfully.");
            case TURN_OFF:
                return new ActionResult(true, "TURN_OFF_LIGHT", "Light is already off.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for light in OFF state.");
        }
    }
    
}

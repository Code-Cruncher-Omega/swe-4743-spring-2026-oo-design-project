package edu.kennesaw.smarthome.domain.Devices.Light;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;

public class Off implements LightState {
    @Override
    public String getName() {
        return "OFF";
    }

    @Override
    public ActionResult execute(Light.LightAction action, Light context, byte... params) {
        switch (action) {
            case TURN_ON:
                context.setState(new On());
                return new ActionResult(true, "TURN_ON", "Light turned on successfully.");
            default:
                return new ActionResult(false, "TURN_ON", "Invalid action for OFF state.");
        }
    }
    
}

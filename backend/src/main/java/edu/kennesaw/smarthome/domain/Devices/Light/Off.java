package edu.kennesaw.smarthome.domain.Devices.Light;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Light.Light.LightAction;

public class Off implements LightState {
    @Override
    public String getName() {
        return "Off";
    }

    @Override
    public ActionResult execute(LightAction action, Light deviceContext, int[] params) {
        switch (action) {
            case TURN_ON:   // params is ignored
                deviceContext.setState(new On());
                return new ActionResult(true, "TURN_ON", "Light turned on successfully.");
            case TURN_OFF:  // params is ignored
                return new ActionResult(false, "TURN_OFF", "Light is already off.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Off state.");
        }
    }
    
}

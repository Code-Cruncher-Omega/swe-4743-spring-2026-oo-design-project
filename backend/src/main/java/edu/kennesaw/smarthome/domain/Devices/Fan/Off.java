package edu.kennesaw.smarthome.domain.Devices.Fan;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;

public class Off implements FanState {
    @Override
    public String getName() {
        return "Off";
    }

    @Override
    public ActionResult execute(Fan.FanAction action, Fan context) {
        switch (action) {
            case TURN_ON:
                context.setState(new On());
                return new ActionResult(true, "TURN_ON", "Fan turned on successfully.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Fan in Off state.");
        }
    }
    
}

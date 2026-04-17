package edu.kennesaw.smarthome.domain.Devices.Fan;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Fan.Fan.FanAction;

public class Off implements FanState {
    @Override
    public String getName() {
        return "Off";
    }

    @Override
    public ActionResult execute(FanAction action, Fan context, String[] params) {
        switch (action) {
            case TURN_ON:   // params is ignored
                context.setState(new On());
                return new ActionResult(true, "TURN_ON", "Fan turned on successfully.");
            case TURN_OFF:  // params is ignored
                return new ActionResult(false, "TURN_OFF", "Fan is already off.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Fan in Off state.");
        }
    }
    
}

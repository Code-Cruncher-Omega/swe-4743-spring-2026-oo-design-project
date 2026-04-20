package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component("fanOffState")
public class OffState implements FanState {
    @Override
    public ActionResult execute(Fan context, FanAction action) {
        switch(action) {
            case TOGGLE_POWER:
                context.setState(context.getOnState());
                return new ActionResult(true, "TOGGLE_FAN_POWER", "Fan turned on.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for fan in off state.");
        }
    }

    @Override
    public String getStateName() {
        return "Off";
    }
}

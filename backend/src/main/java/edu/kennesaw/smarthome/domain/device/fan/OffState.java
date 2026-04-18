package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
public class OffState implements FanState {
    @Override
    public ActionResult execute(Fan context, FanAction action) {
        switch(action) {
            case TURN_ON:
                context.setState(context.getOnState());
                return new ActionResult(true, "TURN_ON_FAN", "Fan turned on.");
            case TURN_OFF:
                return new ActionResult(true, "TURN_OFF_FAN", "Fan is already off.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for fan in OFF state.");
        }
    }
}

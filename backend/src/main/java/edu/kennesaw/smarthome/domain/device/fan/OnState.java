package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
public class OnState implements FanState {
    @Override
    public ActionResult execute(Fan context, FanAction action) {
        switch (action) {
            case TURN_OFF:
                context.setState(context.getOffState());
                return new ActionResult(true, "TURN_OFF_FAN", "Fan turned off.");
            case TURN_ON:
                return new ActionResult(true, "TURN_ON_FAN", "Fan is already on.");
            case SET_SPEED_LOW:
                context.setSpeed(Speed.LOW);
                return new ActionResult(true, "SET_FAN_SPEED_LOW", "Fan speed set to low.");
            case SET_SPEED_MEDIUM:
                context.setSpeed(Speed.MEDIUM);
                return new ActionResult(true, "SET_FAN_SPEED_MEDIUM", "Fan speed set to medium.");
            case SET_SPEED_HIGH:
                context.setSpeed(Speed.HIGH);
                return new ActionResult(true, "SET_FAN_SPEED_HIGH", "Fan speed set to high.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for fan in ON state.");
        }
    }
}

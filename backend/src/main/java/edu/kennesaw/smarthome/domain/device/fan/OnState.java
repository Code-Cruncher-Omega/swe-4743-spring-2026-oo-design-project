package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.ActionResult;

@Component
public class OnState implements FanState {
    @Override
    public ActionResult execute(Fan context, FanAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new ActionResult(true, "TOGGLE_FAN_POWER", "Fan turned off.");
            case SET_SPEED_LOW:
                context.setSpeed(FanSpeed.LOW);
                return new ActionResult(true, "SET_FAN_SPEED_LOW", "Fan speed set to low.");
            case SET_SPEED_MEDIUM:
                context.setSpeed(FanSpeed.MEDIUM);
                return new ActionResult(true, "SET_FAN_SPEED_MEDIUM", "Fan speed set to medium.");
            case SET_SPEED_HIGH:
                context.setSpeed(FanSpeed.HIGH);
                return new ActionResult(true, "SET_FAN_SPEED_HIGH", "Fan speed set to high.");
            default:
                return new ActionResult(false, action.name(), "Action not valid for fan in on state.");
        }
    }

    @Override
    public FanStateType getStateType() {
        return FanStateType.ON;
    }
}

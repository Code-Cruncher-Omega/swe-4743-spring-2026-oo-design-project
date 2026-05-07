package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

@Component("fanOnState")
public class OnState implements FanState {
    @Override
    public DeviceResult execute(Fan context, FanAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, "TOGGLE_FAN_POWER", context.getName() + " turned off.");
            case SET_SPEED_LOW:
                context.setSpeed(context.getLowSpeed());
                return new DeviceResult(true, "SET_FAN_SPEED_LOW", context.getName() + " set to low.");
            case SET_SPEED_MEDIUM:
                context.setSpeed(context.getMediumSpeed());
                return new DeviceResult(true, "SET_FAN_SPEED_MEDIUM", context.getName() + " set to medium.");
            case SET_SPEED_HIGH:
                context.setSpeed(context.getHighSpeed());
                return new DeviceResult(true, "SET_FAN_SPEED_HIGH", context.getName() + " set to high.");
            default:
                return new DeviceResult(false, action.toString(), "Action not valid for " + context.getName() + " in on state.");
        }
    }
    @Override
    public FanStateType getStateType() {
        return FanStateType.ON;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.ON;
    }
}

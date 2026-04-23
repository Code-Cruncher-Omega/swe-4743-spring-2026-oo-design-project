package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

@Component
public class OnState implements FanState {
    @Override
    public DeviceResult execute(Fan context, FanAction action) {
        switch (action) {
            case TOGGLE_POWER:
                context.setState(context.getOffState());
                return new DeviceResult(true, "TOGGLE_FAN_POWER", "Fan turned off.");
            case SET_SPEED_LOW:
                context.setSpeed(FanSpeed.LOW);
                return new DeviceResult(true, "SET_FAN_SPEED_LOW", "Fan speed set to low.");
            case SET_SPEED_MEDIUM:
                context.setSpeed(FanSpeed.MEDIUM);
                return new DeviceResult(true, "SET_FAN_SPEED_MEDIUM", "Fan speed set to medium.");
            case SET_SPEED_HIGH:
                context.setSpeed(FanSpeed.HIGH);
                return new DeviceResult(true, "SET_FAN_SPEED_HIGH", "Fan speed set to high.");
            default:
                return new DeviceResult(false, action.name(), "Action not valid for fan in on state.");
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

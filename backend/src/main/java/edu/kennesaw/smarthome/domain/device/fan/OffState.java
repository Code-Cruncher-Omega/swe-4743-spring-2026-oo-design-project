package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

@Component("fanOffState")
public class OffState implements FanState {
    @Override
    public DeviceResult execute(Fan context, FanAction action) {
        switch(action) {
            case TOGGLE_POWER:
                context.setState(context.getOnState());
                return new DeviceResult(true, "TOGGLE_FAN_POWER", "Fan turned on.");
            default:
                return new DeviceResult(false, action.name(), "Action not valid for fan in off state.");
        }
    }
    @Override
    public FanStateType getStateType() {
        return FanStateType.OFF;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.OFF;
    }
}

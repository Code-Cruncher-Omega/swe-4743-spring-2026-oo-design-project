package edu.kennesaw.smarthome.domain.device.fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.dto.DeviceResult;

@Component("fanOffState")
public class OffState implements FanState {
    @Override
    public DeviceResult execute(Fan context, FanAction action) {
        switch(action) {
            case TOGGLE_POWER:
                context.setState(context.getOnState());
                return new DeviceResult(true, action, context.getName() + " turned on.");
            default:
                throw new IllegalStateException("Action not valid for " + context.getName() + " in off state.");
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

package edu.kennesaw.smarthome.domain.device.light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.dto.DeviceResult;

@Component("lightOffState")
public class OffState implements LightState {
    @Override
    public DeviceResult execute(Light context, LightAction action, int brightnessLevel) {
        return new DeviceResult(false, action, "Cannot set brightness while " + context.getName() + " is off.");}
    @Override
    public DeviceResult execute(Light context, LightAction action, int[] colorValues) {
        return new DeviceResult(false, action, "Cannot set color while " + context.getName() + " is off.");
    }
    @Override
    public DeviceResult execute(Light context, LightAction action) {
        switch(action) {
            case TOGGLE_POWER:
                context.setState(context.getOnState());
                return new DeviceResult(true, action, context.getName() + " turned on.");
            default:
                throw new IllegalStateException("Action not valid for " + context.getName() + " in off state.");
        }
    }
    @Override
    public LightStateType getStateType() {
        return LightStateType.OFF;
    }
    @Override
    public StateActivity getStateActivity() {
        return StateActivity.OFF;
    }
}

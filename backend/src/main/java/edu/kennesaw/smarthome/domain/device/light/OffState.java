package edu.kennesaw.smarthome.domain.device.light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

@Component("lightOffState")
public class OffState implements LightState {
    @Override
    public DeviceResult execute(Light context, LightAction action, int brightnessLevel) {
        return new DeviceResult(false, action.toString(), "Cannot set brightness while " + context.getName() + " is off.");}
    @Override
    public DeviceResult execute(Light context, LightAction action, int[] colorValues) {
        return new DeviceResult(false, action.toString(), "Cannot set color while " + context.getName() + " is off.");
    }
    @Override
    public DeviceResult execute(Light context, LightAction action) {
        switch(action) {
            case TOGGLE_POWER:
                context.setState(context.getOnState());
                return new DeviceResult(true, "TOGGLE_LIGHT_POWER", context.getName() + " turned on.");
            default:
                return new DeviceResult(false, action.toString(), "Action not valid for " + context.getName() + " in off state.");
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

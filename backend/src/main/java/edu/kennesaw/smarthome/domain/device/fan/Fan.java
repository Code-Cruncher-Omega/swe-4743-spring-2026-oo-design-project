package edu.kennesaw.smarthome.domain.device.fan;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.service.creator.FanCreator;
import edu.kennesaw.smarthome.service.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.service.dto.DeviceResult;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;

public class Fan extends Device<Fan, FanState, FanAction, FanStateType> {

    private final Map<String, FanSpeed> SPEEDS;

    private FanSpeed speed; // Speed enum values only.

    public Fan( UUID id,
                String name, 
                String location, 
                FanState savedState, 
                Map<String, FanState> states,
                
                Map<String, FanSpeed> speeds,
                FanSpeed savedSpeed
                ) {
        super(id, name, location, savedState, states);
        
        this.SPEEDS = speeds;
        this.speed = savedSpeed;
    }

    public Fan( String name, 
                String location, 
                FanState initialState, 
                Map<String, FanState> states,

                Map<String, FanSpeed> speeds,
                FanSpeed initialSpeed
                ) {
        super(name, location, initialState, states);
        
        this.SPEEDS = speeds;
        this.speed = initialSpeed;
    }

    @Override
    // Delegate the action execution to the current state of the fan, allowing for state-specific behavior.
    protected DeviceResult execute(FanAction action) {
        return state.execute(this, action);
    }

    protected FanState getOnState() {
        return STATES.get(FanStateType.ON.toString());
    }

    protected FanState getOffState() {
        return STATES.get(FanStateType.OFF.toString());
    }

    protected FanSpeed getLowSpeed() {
        return SPEEDS.get(FanSpeed.LOW.toString());
    }

    protected FanSpeed getMediumSpeed() {
        return SPEEDS.get(FanSpeed.MEDIUM.toString());
    }

    protected FanSpeed getHighSpeed() {
        return SPEEDS.get(FanSpeed.HIGH.toString());
    }
    
    @Override
    protected void setState(FanState newState) {
        state = newState;
    }

    protected void setSpeed(FanSpeed newSpeed) {
        speed = newSpeed;
    }

    @Override
    public DeviceResult performAction(DeviceActionRequest action) {
        switch(action.action()) {
            case "TOGGLE_POWER":    // FanAction.TOGGLE_POWER
                return togglePower();
            case "SET_SPEED_LOW":    // FanAction.SET_SPEED_LOW
                return changeSpeedLow();
            case "SET_SPEED_MEDIUM":    // FanAction.SET_SPEED_MEDIUM
                return changeSpeedMedium();
            case "SET_SPEED_HIGH":  // FanAction.SET_SPEED_HIGH
                return changeSpeedHigh();
            default:
                return new DeviceResult(false, action.action().toString(), "Cannot perform " + action.action().toString() + " with " + getName());
        }
    }

    @Override
    public DeviceType getType() {
        return DeviceType.FAN;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("speed", speed.toString());
        return attributes;
    }

    public FanSpeed getSpeed() {
        return speed;
    }

    @Override
    public DeviceResult reset() {
        state = STATES.get(FanCreator.initialState()); // Reset to the initial state
        speed = SPEEDS.get(FanCreator.initialFanSpeed()); // Reset speed to the initial speed
        return new DeviceResult(true, "RESET_FAN", getName() + " reset to initial state.");
    }

    public DeviceResult togglePower() {
        return state.execute(this, FanAction.TOGGLE_POWER);
    }

    public DeviceResult changeSpeedLow() {
        return execute(FanAction.SET_SPEED_LOW);
    }

    public DeviceResult changeSpeedMedium() {
        return execute(FanAction.SET_SPEED_MEDIUM);
    }

    public DeviceResult changeSpeedHigh() {
        return execute(FanAction.SET_SPEED_HIGH);
    }
}

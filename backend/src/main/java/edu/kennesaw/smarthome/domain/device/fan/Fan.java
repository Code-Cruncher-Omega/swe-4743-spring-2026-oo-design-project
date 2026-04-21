package edu.kennesaw.smarthome.domain.device.fan;

import java.util.Map;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class Fan extends Device<Fan, FanState, FanAction, FanStateType> {

    private final FanState INITIAL_STATE;
    private final FanSpeed INITIAL_SPEED;

    private FanSpeed speed; // Speed enum values only.

    public Fan( String name, 
                String location, 
                FanState initialState, 
                Map<FanStateType, FanState> states,

                FanSpeed initialSpeed
                ) {
        super(name, location, initialState, states);

        this.INITIAL_STATE = initialState;
        this.INITIAL_SPEED = initialSpeed;
        
        this.speed = initialSpeed;
    }

    @Override
    // Delegate the action execution to the current state of the fan, allowing for state-specific behavior.
    protected ActionResult execute(FanAction action) {
        return state.execute(this, action);
    }

    protected FanState getOnState() {
        return STATES.get(FanStateType.ON);
    }

    protected FanState getOffState() {
        return STATES.get(FanStateType.OFF);
    }
    
    @Override
    protected void setState(FanState newState) {
        state = newState;
    }

    protected void setSpeed(FanSpeed newSpeed) {
        speed = newSpeed;
    }

    @Override
    public DeviceType getType() {
        return DeviceType.FAN;
    }

    public FanSpeed getSpeed() {
        return speed;
    }

    @Override
    public ActionResult reset() {
        state = INITIAL_STATE; // Reset to the initial state
        speed = INITIAL_SPEED; // Reset speed to the initial speed
        return new ActionResult(true, "RESET_FAN", "Fan reset to initial state.");
    }

    public ActionResult togglePower() {
        return state.execute(this, FanAction.TOGGLE_POWER);
    }

    public ActionResult changeSpeedLow() {
        return execute(FanAction.SET_SPEED_LOW);
    }

    public ActionResult changeSpeedMedium() {
        return execute(FanAction.SET_SPEED_MEDIUM);
    }

    public ActionResult changeSpeedHigh() {
        return execute(FanAction.SET_SPEED_HIGH);
    }
}

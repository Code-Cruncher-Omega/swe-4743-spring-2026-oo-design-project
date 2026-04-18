package edu.kennesaw.smarthome.domain.device.fan;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class Fan extends Device<Fan, FanState, FanAction> {

    private final FanState INITIAL_STATE;
    private final Speed INITIAL_SPEED;
    private final FanState ON_STATE;
    private final FanState OFF_STATE;

    private Speed speed; // Speed enum values only.

    public Fan(String name, String location, FanState initialState, Speed initialSpeed, FanState onState, FanState offState) {
        super(name, location, initialState);
        this.INITIAL_STATE = initialState;
        this.INITIAL_SPEED = initialSpeed;
        this.ON_STATE = onState;
        this.OFF_STATE = offState;
        this.speed = initialSpeed;
    }

    @Override
    // Delegate the action execution to the current state of the fan, allowing for state-specific behavior.
    protected ActionResult execute(FanAction action) {
        return state.execute(this, action);
    }

    protected FanState getOnState() {
        return ON_STATE;
    }

    protected FanState getOffState() {
        return OFF_STATE;
    }
    
    @Override
    protected void setState(FanState newState) {
        this.state = newState;
    }

    protected void setSpeed(Speed newSpeed) {
        this.speed = newSpeed;
    }

    @Override
    public DeviceType getType() {
        return DeviceType.FAN;
    }

    public Speed getSpeed() {
        return speed;
    }

    @Override
    public ActionResult reset() {
        this.state = INITIAL_STATE; // Reset to the initial state
        this.speed = INITIAL_SPEED; // Reset speed to the initial speed
        return new ActionResult(true, "RESET_FAN", "Fan reset to initial state.");
    }

    public ActionResult turnOn() {
        return execute(FanAction.TURN_ON);
    }

    public ActionResult turnOff() {
        return execute(FanAction.TURN_OFF);
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

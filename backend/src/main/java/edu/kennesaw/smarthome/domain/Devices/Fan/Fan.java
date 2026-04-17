package edu.kennesaw.smarthome.domain.Devices.Fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Device;

@Component
public class Fan extends Device {
    // Fan-specific variables //

    private String speed; // Values: "Low", "Medium", "High" only
    private FanState state;
    public enum FanAction {TURN_ON, TURN_OFF, SET_SPEED};  // All state transition actions

    // Constructor
    public Fan(String name, String location, FanState initialState, String initialSpeed) {
        super(name, location, Type.FAN);
        this.state = initialState;
        this.speed = initialSpeed;
    }

    // Methods //

    // Pass action onto the state object to handle and return the result
    private ActionResult execute(FanAction action, String... params) {
        return state.execute(action, this, params);
    }

    // Allow state objects to change the context's speed variable
    protected void setSpeed(String newSpeed) {
        this.speed = newSpeed;
    }

    // Allow state objects to transition the context's state object to another
    protected void setState(FanState newState) {
        this.state = newState;
    }

    // Simplify attribute changing methods for external callers by directly exposing the actions as methods
    public ActionResult changeSpeed(String input) {
        return execute(FanAction.SET_SPEED, input);
    }

    public ActionResult turnOn() {
        return execute(FanAction.TURN_ON);
    }

    public ActionResult turnOff() {
        return execute(FanAction.TURN_OFF);
    }

    public String getStateName() {
        return state.getName();
    }

    public String getSpeed() {
        return speed;
    }
}
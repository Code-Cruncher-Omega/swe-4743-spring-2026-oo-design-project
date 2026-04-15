package edu.kennesaw.smarthome.domain.Devices.Fan;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Device;

@Component
public class Fan extends Device {
    // Fan-specific variables //

    private FanState state;
    public enum FanAction {TURN_ON, TURN_OFF, SET_SPEED_LOW, SET_SPEED_MEDIUM, SET_SPEED_HIGH};  // All actions that can be performed
    private String speed; // Values: "LOW", "MEDIUM", "HIGH" only

    // Constructor
    public Fan(String name, String location, FanState initialState, String initialSpeed) {
        super(name, location, Type.FAN);
        this.state = initialState;
        this.speed = initialSpeed;
    }

    // Methods //

    // Pass action onto the state object to handle and return the result
    public ActionResult execute(FanAction action) {
        return state.execute(action, this);
    }

    public String getStateName() {
        return state.getName();
    }

    public String getSpeed() {
        return speed;
    }

    // Allow state objects to transition the context's state object to another
    protected void setState(FanState newState) {
        this.state = newState;
    }

    // Allow state objects to change the context's speed variable
    protected void setSpeed(String newSpeed) {
        this.speed = newSpeed;
    }
}
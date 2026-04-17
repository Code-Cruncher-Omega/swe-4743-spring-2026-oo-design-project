package edu.kennesaw.smarthome.domain.Devices.Light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Device;

@Component
public class Light extends Device {
    // Light-specific variables //

    private int brightness; // Values 10-100 only
    private int[] color;  // Size 3 array of RGB values, each 0-255 only
    private LightState state;
    public enum LightAction {TURN_ON, TURN_OFF, SET_BRIGHTNESS, SET_COLOR};  // All state transition actions

    // Constructor
    public Light(String name, String location, LightState initialState, int initialBrightness, int[] initialColor) {
        super(name, location, Type.LIGHT);
        this.state = initialState;
        this.brightness = initialBrightness;
        this.color = initialColor;
    }

    // Methods //

    // Pass action onto the state object to handle and return the result
    private ActionResult execute(LightAction action, int... params) {
        return state.execute(action, this, params);
    }

    // Allow state objects to change the context's brightness variable
    protected void setBrightness(int newBrightness) {
        this.brightness = newBrightness;
    }

    // Allow state objects to change the context's color variable
    protected void setColor(int[] newColor) {
        this.color = newColor;
    }

    // Allow state objects to transition the context's state object to another
    protected void setState(LightState newState) {
        this.state = newState;
    }

    // Simplify attribute changing methods for external callers by directly exposing the actions as methods
    public ActionResult changeBrightness(int newBrightness) {
        return execute(LightAction.SET_BRIGHTNESS, newBrightness);
    }

    public ActionResult turnOn() {
        return execute(LightAction.TURN_ON);
    }

    public ActionResult turnOff() {
        return execute(LightAction.TURN_OFF);
    }

    public String getStateName() {
        return state.getName();
    }

    public int getBrightness() {
        return brightness;
    }

    public int[] getColor() {
        return color;
    }
}
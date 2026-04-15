package edu.kennesaw.smarthome.domain.Devices.Light;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Device;

public class Light extends Device {
    // Light-specific variables //

    private LightState state;
    public enum LightAction {TURN_ON, TURN_OFF, SET_BRIGHTNESS, SET_COLOR};  // All actions that can be performed
    private byte brightness; // Values 10-100 only
    private byte[] color;  // Size 3 array of RGB values, each 0-255 only
    
    // Constructor
    public Light(String name, String location, LightState initialState, byte initialBrightness, byte[] initialColor) {
        super(name, location, Type.LIGHT);
        this.state = initialState;
        this.brightness = initialBrightness;
        this.color = initialColor;
    }

    // Methods //

    // Pass action onto the state object to handle and return the result
    public ActionResult execute(LightAction action, Object... params) {
        return state.execute(action, this, params);
    }

    public String getStateName() {
        return state.getName();
    }

    public byte getBrightness() {
        return brightness;
    }

    public byte[] getColor() {
        return color;
    }

    // Allow state objects to transition the context's state object to another
    protected void setState(LightState newState) {
        this.state = newState;
    }

    // Allow state objects to change the context's brightness variable
    protected void setBrightness(byte newBrightness) {
        this.brightness = newBrightness;
    }

    // Allow state objects to change the context's color variable
    protected void setColor(byte[] newColor) {
        this.color = newColor;
    }
}
package edu.kennesaw.smarthome.domain.Devices.Light;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Device;

@Component
public class Light extends Device {
    // Light-specific variables //

    private byte brightness; // Values 10-100 only
    private byte[] color;  // Size 3 array of RGB values, each 0-255 only
    private LightState state;
    public enum LightAction {TURN_ON, TURN_OFF, SET_BRIGHTNESS, SET_COLOR};  // All actions that can be performed

    // Constructor
    public Light(String name, String location, LightState initialState, byte initialBrightness, byte[] initialColor) {
        super(name, location, Type.LIGHT);
        this.state = initialState;
        this.brightness = initialBrightness;
        this.color = initialColor;
    }

    // Methods //

    // Pass action onto the state object to handle and return the result
    private ActionResult execute(LightAction action, byte... params) {
        return state.execute(action, this, params);
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

    // Simplify state changing methods for external callers by directly exposing the actions as methods
    public ActionResult changeBrightness(byte newBrightness) {
        if(newBrightness < 10 || newBrightness > 100) {
            return new ActionResult(false, "CHANGE_BRIGHTNESS", "Invalid brightness input. Use a value between 10 and 100.");
        }
        return execute(LightAction.SET_BRIGHTNESS, newBrightness);
    }

    public ActionResult changeColor(byte[] newColor) {
        if(newColor.length != 3) {
            return new ActionResult(false, "CHANGE_COLOR", "Invalid color input. Use an array of 3 bytes (RGB).");
        }
        for(byte colorComponent : newColor) {
            if(colorComponent < 0 || colorComponent > 255) {
                return new ActionResult(false, "CHANGE_COLOR", "Invalid color input. Each RGB value must be between 0 and 255.");
            }
        }
        return execute(LightAction.SET_COLOR, newColor);
    }

    public ActionResult changeColor(byte r, byte g, byte b) {
        return changeColor(new byte[] {r, g, b});
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

    public byte getBrightness() {
        return brightness;
    }

    public byte[] getColor() {
        return color;
    }
}
package edu.kennesaw.smarthome.domain.device.light;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class Light extends Device<Light, LightState, LightAction> {

    private final LightState INITIAL_STATE;
    private final int INITIAL_BRIGHTNESS;
    private final int[] INITIAL_COLOR;
    private final LightState ON_STATE;
    private final LightState OFF_STATE;

    private int brightness; // Brightness level (10-100)
    private int[] color; // RGB color values (0-255)

    public Light(String name, String location, LightState initialState, int initialBrightness, int[] initialColor, LightState onState, LightState offState) {
        super(name, location, initialState);
        this.INITIAL_STATE = initialState;
        this.INITIAL_BRIGHTNESS = initialBrightness;
        this.INITIAL_COLOR = initialColor;
        this.ON_STATE = onState;
        this.OFF_STATE = offState;
        this.brightness = initialBrightness;
        this.color = initialColor;
    }

    protected ActionResult execute(LightAction action, int brightnessLevel) {
        return state.execute(this, action, brightnessLevel);
    }

    protected ActionResult execute(LightAction action, int[] colorValues) {
        return state.execute(this, action, colorValues);
    }

    @Override
    protected ActionResult execute(LightAction action) {
        return state.execute(this, action);
    }

    protected LightState getOnState() {
        return ON_STATE;
    }

    protected LightState getOffState() {
        return OFF_STATE;
    }

    @Override
    protected void setState(LightState newState) {
        this.state = newState;
    }

    protected void setBrightness(int newBrightness) {
        this.brightness = newBrightness;
    }

    protected void setColor(int[] newColor) {
        this.color = newColor;
    }

    public int getBrightness() {
        return brightness;
    }

    public int[] getColor() {
        return color;
    }

    @Override
    public DeviceType getType() {
        return DeviceType.LIGHT;
    }

    @Override
    public ActionResult reset() {
        this.brightness = INITIAL_BRIGHTNESS; // Reset brightness to the initial level
        this.color = INITIAL_COLOR; // Reset color to the initial RGB values
        this.state = INITIAL_STATE; // Reset to the initial state
        return new ActionResult(true, "RESET_LIGHT", "Light reset to initial state, brightness, and color.");
    }

    public ActionResult changeBrightness(int newBrightness) {
        return state.execute(this, LightAction.SET_BRIGHTNESS, newBrightness);
    }

    public ActionResult changeColor(int[] newColor) {
        return state.execute(this, LightAction.SET_COLOR, newColor);
    }

    public ActionResult changeColor(int r, int g, int b) {
        return changeColor(new int[] {r, g, b});
    }
}

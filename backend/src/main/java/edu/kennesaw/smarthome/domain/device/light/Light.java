package edu.kennesaw.smarthome.domain.device.light;

import java.util.Map;

import edu.kennesaw.smarthome.domain.device.DeviceResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class Light extends Device<Light, LightState, LightAction, LightStateType> {

    private final LightState INITIAL_STATE;
    private final int INITIAL_BRIGHTNESS;
    private final int[] INITIAL_COLOR;

    private int brightness; // Brightness level (10-100)
    private int[] color; // RGB color values (0-255)

    public Light(   String name, 
                    String location, 
                    LightState initialState, 
                    Map<LightStateType, LightState> states,

                    int initialBrightness, 
                    int[] initialColor) {
        super(name, location, initialState, states);

        this.INITIAL_STATE = initialState;
        this.INITIAL_BRIGHTNESS = initialBrightness;
        this.INITIAL_COLOR = initialColor;
        
        this.brightness = initialBrightness;
        this.color = initialColor;
    }

    // Delegate the action execution to the current state of the light, allowing for state-specific behavior.
    protected DeviceResult execute(LightAction action, int brightnessLevel) {
        return state.execute(this, action, brightnessLevel);
    }

    protected DeviceResult execute(LightAction action, int[] colorValues) {
        return state.execute(this, action, colorValues);
    }

    @Override
    protected DeviceResult execute(LightAction action) {
        return state.execute(this, action);
    }

    protected LightState getOnState() {
        return STATES.get(LightStateType.ON);
    }

    protected LightState getOffState() {
        return STATES.get(LightStateType.OFF);
    }

    @Override
    protected void setState(LightState newState) {
        state = newState;
    }

    protected void setBrightness(int newBrightness) {
        brightness = newBrightness;
    }

    protected void setColor(int[] newColor) {
        color = newColor;
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
    public DeviceResult reset() {
        brightness = INITIAL_BRIGHTNESS; // Reset brightness to the initial level
        color = INITIAL_COLOR; // Reset color to the initial RGB values
        state = INITIAL_STATE; // Reset to the initial state
        return new DeviceResult(true, "RESET_LIGHT", "Light reset to initial state, brightness, and color.");
    }

    public DeviceResult changeBrightness(int newBrightness) {
        return state.execute(this, LightAction.SET_BRIGHTNESS, newBrightness);
    }

    public DeviceResult changeColor(int[] newColor) {
        return state.execute(this, LightAction.SET_COLOR, newColor);
    }

    public DeviceResult changeColor(int r, int g, int b) {
        return changeColor(new int[] {r, g, b});
    }

    public DeviceResult togglePower() {
        return state.execute(this, LightAction.TOGGLE_POWER);
    }
}

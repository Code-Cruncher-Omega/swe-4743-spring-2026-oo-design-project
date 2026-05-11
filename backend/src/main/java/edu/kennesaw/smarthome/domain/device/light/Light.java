package edu.kennesaw.smarthome.domain.device.light;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.service.creator.LightCreator;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.dto.DeviceResult;

public class Light extends Device<Light, LightState, LightAction, LightStateType> {

    private int brightness; // Brightness level (10-100)
    private int[] color; // RGB color values (0-255)

    public Light(   UUID id,
                    String name, 
                    String location, 
                    LightState initialState, 
                    Map<LightStateType, LightState> states,

                    int savedBrightness, 
                    int[] savedColor) {
        super(id, name, location, initialState, states);
        
        this.brightness = savedBrightness;
        this.color = savedColor;
    }

    public Light(   String name, 
                    String location, 
                    LightState initialState, 
                    Map<LightStateType, LightState> states,

                    int initialBrightness, 
                    int[] initialColor) {
        super(name, location, initialState, states);
        
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
    public void setState(LightState newState) {
        state = newState;
    }

    public void setBrightness(int newBrightness) {
        brightness = newBrightness;
    }

    public void setColor(int[] newColor) {
        color = newColor;
    }

    @Override
    public DeviceResult performAction(DeviceActionRequest action) {
        LightAction lightAction = LightAction.from(action.action());
        if(action.parameters().length == 0) {
            return execute(lightAction);
        }
        if(action.parameters().length == 1) {
            return execute(lightAction, (int) action.parameters()[0]);
        }
        return execute(lightAction, 
            new int[] {(int) action.parameters()[0], 
                        (int) action.parameters()[1], 
                        (int) action.parameters()[2]});
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
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("brightness", brightness);
        attributes.put("red", color[0]);
        attributes.put("green", color[1]);
        attributes.put("blue", color[2]);
        return attributes;
    }

    @Override
    public void reset() {
        brightness = LightCreator.initialBrightness(); // Reset brightness to the initial level
        color = LightCreator.initialColor(); // Reset color to the initial RGB values
        state = STATES.get(LightCreator.initialState()); // Reset to the initial state
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

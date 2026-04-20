package edu.kennesaw.smarthome.domain.device.light;

import edu.kennesaw.smarthome.domain.device.DeviceAction;

public enum LightAction implements DeviceAction {
    TOGGLE_POWER,
    SET_BRIGHTNESS,
    SET_COLOR
}

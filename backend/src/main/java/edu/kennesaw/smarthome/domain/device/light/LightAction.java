package edu.kennesaw.smarthome.domain.device.light;

import edu.kennesaw.smarthome.domain.device.Action;

public enum LightAction implements Action {
    TOGGLE_POWER,
    SET_BRIGHTNESS,
    SET_COLOR
}

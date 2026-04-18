package edu.kennesaw.smarthome.domain.device.light;

import edu.kennesaw.smarthome.domain.device.Action;

public enum LightAction implements Action {
    TURN_ON,
    TURN_OFF,
    SET_BRIGHTNESS,
    SET_COLOR
}

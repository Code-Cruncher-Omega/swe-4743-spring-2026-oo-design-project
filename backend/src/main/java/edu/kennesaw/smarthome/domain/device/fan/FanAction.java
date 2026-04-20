package edu.kennesaw.smarthome.domain.device.fan;

import edu.kennesaw.smarthome.domain.device.Action;

public enum FanAction implements Action {
    TOGGLE_POWER,
    SET_SPEED_LOW,
    SET_SPEED_MEDIUM,
    SET_SPEED_HIGH
}

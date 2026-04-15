package edu.kennesaw.smarthome.domain.Devices.Light;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;

public class On implements LightState {
    @Override
    public String getName() {
        return "On";
    }

    @Override
    public ActionResult execute(Light.LightAction action, Light context, byte... params) {
        switch (action) {
            case TURN_OFF:
                context.setState(new Off());
                return new ActionResult(true, "TURN_OFF", "Light turned off successfully.");
            case SET_BRIGHTNESS:
                if (params.length < 1) {
                    return new ActionResult(false, "SET_BRIGHTNESS", "Missing brightness parameter.");
                }
                byte brightness = params[0];
                if (brightness < 0 || brightness > 100) {
                    return new ActionResult(false, "SET_BRIGHTNESS", "Brightness must be between 0 and 100.");
                }
                context.setBrightness(brightness);
                return new ActionResult(true, "SET_BRIGHTNESS", String.format("Brightness set to %d%% successfully.", brightness));
            default:
                return new ActionResult(false, action.name(), "Invalid action for Light in On state.");
        }
    }
    
}

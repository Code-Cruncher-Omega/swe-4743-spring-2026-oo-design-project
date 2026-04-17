package edu.kennesaw.smarthome.domain.Devices.Light;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Light.Light.LightAction;

public class On implements LightState {
    @Override
    public String getName() {
        return "On";
    }

    @Override
    public ActionResult execute(LightAction action, Light context, int[] params) {
        switch (action) {
            case TURN_OFF:  // params is ignored
                context.setState(new Off());
                return new ActionResult(true, "TURN_OFF", "Light turned off successfully.");
            case TURN_ON:   // params is ignored
                return new ActionResult(false, "TURN_ON", "Light is already on.");
            case SET_BRIGHTNESS:
                if (params.length != 1) {
                    return new ActionResult(false, "SET_BRIGHTNESS", "SET_BRIGHTNESS action requires exactly 1 parameter (brightness level).");
                }
                int brightness = params[0];
                if (brightness < 10 || brightness > 100) {
                    return new ActionResult(false, "SET_BRIGHTNESS", "Brightness must be between 0 and 100.");
                }
                context.setBrightness(brightness);
                return new ActionResult(true, "SET_BRIGHTNESS", String.format("Brightness set to %d%% successfully.", brightness));
            case SET_COLOR:
                if (params.length != 3) {
                    return new ActionResult(false, "SET_COLOR", "SET_COLOR action requires exactly 3 parameters (R, G, B).");
                }
                int[] rgb = new int[] {params[0], params[1], params[2]};
                for (int colorComponent : rgb) {
                    if (colorComponent < 0 || colorComponent > 255) {
                        return new ActionResult(false, "SET_COLOR", "Each RGB value must be between 0 and 255.");
                    }
                }
                context.setColor(rgb);
                return new ActionResult(true, "SET_COLOR", String.format("Color set to RGB(%d, %d, %d) successfully.", rgb[0], rgb[1], rgb[2]));
            default:
                return new ActionResult(false, action.name(), "Invalid action for Light in On state.");
        }
    }
}

package edu.kennesaw.smarthome.domain.Devices.Fan;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.Fan.Fan.FanAction;

public class On implements FanState {
    @Override
    public String getName() {
        return "On";
    }
    
    @Override
    public ActionResult execute(FanAction action, Fan context, String[] params) {
        switch (action) {
            case TURN_OFF:  // params is ignored
                context.setState(new Off());
                return new ActionResult(true, "TURN_OFF", "Fan turned off successfully.");
            case TURN_ON:   // params is ignored
                return new ActionResult(false, "TURN_ON", "Fan is already on.");
            case SET_SPEED:
                if (params.length != 1) {
                    return new ActionResult(false, "SET_SPEED", "SET_SPEED action requires exactly 1 parameter (speed level).");
                }
                String newSpeed = params[0].toUpperCase();
                if (newSpeed.equals(context.getSpeed().toUpperCase())) {
                    return new ActionResult(false, "SET_SPEED_" + newSpeed, String.format("Fan is already at %s speed.", newSpeed));
                }
                switch (newSpeed) {
                    case "LOW":
                    case "MEDIUM":
                    case "HIGH":
                        String formattedSpeed = newSpeed.charAt(0) + newSpeed.substring(1).toLowerCase(); // Convert to "Low", "Medium", "High"
                        context.setSpeed(formattedSpeed);
                        return new ActionResult(true, "SET_SPEED_" + newSpeed, String.format("Fan speed set to %s successfully.", formattedSpeed));
                    default:
                        return new ActionResult(false, "SET_SPEED_" + newSpeed, "Invalid speed parameter for SET_SPEED action.");
                }
            default:
                return new ActionResult(false, action.name(), "Invalid action for Fan in On state.");
        }
    }
    
}

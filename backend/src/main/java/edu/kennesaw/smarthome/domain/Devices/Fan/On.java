package edu.kennesaw.smarthome.domain.Devices.Fan;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;

public class On implements FanState {
    @Override
    public String getName() {
        return "On";
    }

    @Override
    public ActionResult execute(Fan.FanAction action, Fan context) {
        switch (action) {
            case TURN_OFF:
                context.setState(new Off());
                return new ActionResult(true, "TURN_OFF", "Fan turned off successfully.");
            case SET_SPEED_LOW:
                if(context.getSpeed().equals("LOW")) {
                    return new ActionResult(false, "SET_SPEED_LOW", "Fan is already at LOW speed.");
                }
                context.setSpeed("LOW");
                return new ActionResult(true, "SET_SPEED_LOW", "Fan speed set to LOW.");
            case SET_SPEED_MEDIUM:
                if(context.getSpeed().equals("MEDIUM")) {
                    return new ActionResult(false, "SET_SPEED_MEDIUM", "Fan is already at MEDIUM speed.");
                }
                context.setSpeed("MEDIUM");
                return new ActionResult(true, "SET_SPEED_MEDIUM", "Fan speed set to MEDIUM.");
            case SET_SPEED_HIGH:
                if(context.getSpeed().equals("HIGH")) {
                    return new ActionResult(false, "SET_SPEED_HIGH", "Fan is already at HIGH speed.");
                }
                context.setSpeed("HIGH");
                return new ActionResult(true, "SET_SPEED_HIGH", "Fan speed set to HIGH.");
            default:
                return new ActionResult(false, action.name(), "Invalid action for Fan in On state.");
        }
    }
    
}

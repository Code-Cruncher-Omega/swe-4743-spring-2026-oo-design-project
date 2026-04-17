package edu.kennesaw.smarthome.domain.Devices.Fan;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.State;
import edu.kennesaw.smarthome.domain.Devices.Fan.Fan.FanAction;

public interface FanState extends State {
    public ActionResult execute(FanAction action, Fan context, String[] params);
}

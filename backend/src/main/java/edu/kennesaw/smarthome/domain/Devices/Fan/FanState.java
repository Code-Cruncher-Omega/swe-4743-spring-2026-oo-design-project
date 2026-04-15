package edu.kennesaw.smarthome.domain.Devices.Fan;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.State;

public interface FanState extends State {
    public ActionResult execute(Fan.FanAction action, Fan context);
}

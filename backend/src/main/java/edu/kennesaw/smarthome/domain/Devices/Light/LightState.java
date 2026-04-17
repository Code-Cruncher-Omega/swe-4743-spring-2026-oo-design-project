package edu.kennesaw.smarthome.domain.Devices.Light;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.State;
import edu.kennesaw.smarthome.domain.Devices.Light.Light.LightAction;

interface LightState extends State {
    public ActionResult execute(LightAction action, Light deviceContext, int[] params);
}
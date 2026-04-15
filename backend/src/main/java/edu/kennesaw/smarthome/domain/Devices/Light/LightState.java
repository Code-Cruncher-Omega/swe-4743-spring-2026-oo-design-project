package edu.kennesaw.smarthome.domain.Devices.Light;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.State;

interface LightState extends State {
    public ActionResult execute(Light.LightAction action, Light context, byte... params);
}
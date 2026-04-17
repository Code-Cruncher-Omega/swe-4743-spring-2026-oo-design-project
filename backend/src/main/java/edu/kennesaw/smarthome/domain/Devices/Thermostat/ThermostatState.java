package edu.kennesaw.smarthome.domain.Devices.Thermostat;

import edu.kennesaw.smarthome.domain.Devices.ActionResult;
import edu.kennesaw.smarthome.domain.Devices.State;
import edu.kennesaw.smarthome.domain.Devices.Thermostat.Thermostat.ThermostatAction;

public interface ThermostatState extends State {
    ActionResult execute(ThermostatAction action, Thermostat deviceContext);
}
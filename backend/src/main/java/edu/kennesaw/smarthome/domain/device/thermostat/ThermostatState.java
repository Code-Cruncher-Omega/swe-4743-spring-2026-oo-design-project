package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface ThermostatState extends DeviceState<Thermostat, ThermostatAction> {       
    @Override
    // execute does the actual increment or decrement in ambient temperature for the thermostat, as well as returning the ActionResult object.
    // But it does not change the context's state.
    ActionResult execute(Thermostat context, ThermostatAction action);
    // updateAmbientTemperature compares the ambient and desired temperatures, checks the mode, sets the state, and calls execute on the state
    // to do an action (increment or decrement) if needed. Returns execute's object or returns its own if not applicable.
    ActionResult updateAmbientTemperature(Thermostat context);
}

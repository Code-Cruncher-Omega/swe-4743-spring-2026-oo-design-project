package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface ThermostatState extends DeviceState<Thermostat, ThermostatAction> {
    // updateAmbientTemperature checks if TODO
    
    @Override
    ActionResult execute(Thermostat context, ThermostatAction action);
    // How the ambient temperature is updated depends on the current state.
    ActionResult updateAmbientTemperature(Thermostat context);
}

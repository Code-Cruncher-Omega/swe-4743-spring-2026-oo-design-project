package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface ThermostatState extends DeviceState<Thermostat, ThermostatAction, ThermostatStateType> {       
    @Override
    public ActionResult execute(Thermostat context, ThermostatAction action);
    @Override
    public ThermostatStateType getStateType();
}

package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.DeviceResult;
import edu.kennesaw.smarthome.domain.device.DeviceState;

public interface ThermostatState extends DeviceState<Thermostat, ThermostatAction, ThermostatStateType> {       
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action);
    @Override
    public ThermostatStateType getStateType();
}

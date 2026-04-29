package edu.kennesaw.smarthome.domain.device.thermostat;

import edu.kennesaw.smarthome.domain.device.abstraction.DeviceState;
import edu.kennesaw.smarthome.service.dto.DeviceResult;

public interface ThermostatState extends DeviceState<Thermostat, ThermostatAction, ThermostatStateType> {       
    // Updates the temperature a set number of times.
    public DeviceResult execute(Thermostat context, ThermostatAction action, int tickRate);
    @Override
    public DeviceResult execute(Thermostat context, ThermostatAction action);
    @Override
    public ThermostatStateType getStateType();
}

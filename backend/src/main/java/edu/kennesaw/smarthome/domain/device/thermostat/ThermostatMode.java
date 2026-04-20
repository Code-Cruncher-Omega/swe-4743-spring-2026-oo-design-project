package edu.kennesaw.smarthome.domain.device.thermostat;

// Used for strategy pattern; each ThermostatMode defines the conditions for changing between ThermostatStates.
public interface ThermostatMode {
    
    // Returns the ThermostatState that needs to be changed to for updating the ambient temperature.
    // Does not set ThermostatState to off.
    public ThermostatState updateAmbientTemperature(Thermostat context);
    // Similar concept with DeviceStateType, but for Thermostat's specific modes.
    public ThermostatModeType getModeType();
}

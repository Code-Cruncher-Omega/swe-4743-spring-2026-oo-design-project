package edu.kennesaw.smarthome.domain.device.thermostat;

// Used for strategy pattern; each ThermostatMode defines the conditions for changing between ThermostatStates.
// Returns the ThermostatState that needs to be changed to for updating the ambient temperature.
// Does not set ThermostatState to off.
public interface ThermostatMode {
    public ThermostatState updateAmbientTemperature(Thermostat context);
}

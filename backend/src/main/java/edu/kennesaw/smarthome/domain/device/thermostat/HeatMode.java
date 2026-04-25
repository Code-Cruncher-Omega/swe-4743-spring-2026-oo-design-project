package edu.kennesaw.smarthome.domain.device.thermostat;

public class HeatMode implements ThermostatMode {
    @Override
    public ThermostatState updateAmbientTemperature(Thermostat context) {
        Temperature ambientTemperature = context.getAmbientTemperature();
        Temperature desiredTemperature = context.getDesiredTemperature();

        if(ambientTemperature.getValue() < desiredTemperature.getValue()) {
            return context.getHeatingState();
        }
        return context.getIdleState();  // ambient >= desired, meaning stop heating up ambient, so return IDLE state.
    }
    @Override
    public ThermostatModeType getModeType() {
        return ThermostatModeType.HEAT;
    }
}

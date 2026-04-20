package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

@Component
public class CoolMode implements ThermostatMode {
    @Override
    public ThermostatState updateAmbientTemperature(Thermostat context) {
        Temperature ambientTemperature = context.getAmbientTemperature();
        Temperature desiredTemperature = context.getDesiredTemperature();

        if(ambientTemperature.getValue() > desiredTemperature.getValue()) {
            return context.getCoolingState();
        }
        return context.getIdleState();  // ambient <= desired, meaning stop cooling down ambient, so return IDLE state.
    }
    @Override
    public ThermostatModeType getModeType() {
        return ThermostatModeType.COOL;
    }
}

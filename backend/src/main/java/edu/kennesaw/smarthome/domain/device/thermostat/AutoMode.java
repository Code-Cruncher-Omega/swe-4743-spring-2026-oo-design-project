package edu.kennesaw.smarthome.domain.device.thermostat;

import org.springframework.stereotype.Component;

@Component("thermostatAutoMode")
public class AutoMode implements ThermostatMode {
    @Override
    public ThermostatState updateAmbientTemperature(Thermostat context) {
        Temperature ambientTemperature = context.getAmbientTemperature();
        Temperature desiredTemperature = context.getDesiredTemperature();

        if(ambientTemperature.getValue() < desiredTemperature.getValue()) {
            return context.getHeatingState();
        } else if(ambientTemperature.getValue() > desiredTemperature.getValue()) {
            return context.getCoolingState();
        }
        return context.getIdleState();  // ambient == desired, so return IDLE state.
    }
}

package edu.kennesaw.smarthome.service.creator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.service.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.thermostat.Temperature;
import edu.kennesaw.smarthome.domain.device.thermostat.Thermostat;
import edu.kennesaw.smarthome.domain.device.thermostat.ThermostatAction;
import edu.kennesaw.smarthome.domain.device.thermostat.ThermostatMode;
import edu.kennesaw.smarthome.domain.device.thermostat.ThermostatModeType;
import edu.kennesaw.smarthome.domain.device.thermostat.ThermostatState;
import edu.kennesaw.smarthome.domain.device.thermostat.ThermostatStateType;

@Component
public class ThermostatCreator implements DeviceCreator<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> {
        
    private final Map<ThermostatStateType, ThermostatState> STATES;
    private final Map<ThermostatModeType, ThermostatMode> MODES;

    // Spring provides a List containing an instance from each concrete ThermostatState.
    // Spring also provides a List containing an instance from each concrete ThermostatMode.
    public ThermostatCreator(List<ThermostatState> stateList, List<ThermostatMode> modeList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(
                        ThermostatState::getStateType,
                        Function.identity()
                ));
        this.MODES = modeList.stream()
                .collect(Collectors.toMap(
                        ThermostatMode::getModeType,
                        Function.identity()
                ));
    }
    
    @Override
    public Device<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> createDevice(DeviceCreationRequest request) {
        return new Thermostat(request.name(), 
                            request.location(), 
                            initialState(), 
                            STATES,
                            initialMode(),
                            initialDesiredTemperature(),
                            initialAmbientTemperature(),
                            MODES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.THERMOSTAT;
    }

    @Override
    public ThermostatState initialState() {
        return STATES.get(ThermostatStateType.OFF);
    }

    // Initial mode for all Thermostat instances is defined here.
    public ThermostatMode initialMode() {
        return MODES.get(ThermostatModeType.AUTO);
    }

    // Temperature mainly serves as a values class, so managing it through Spring
    // would only add more complexity. Hence, instances are created manually here.

    // Initial desired temperature for all Thermostat instances is defined here.
    public Temperature initialDesiredTemperature() {
        return new Temperature(72);
    }

    // Initial ambient temperature for all Thermostat instances is defined here.
    public Temperature initialAmbientTemperature() {
        return new Temperature(60);
    }
}

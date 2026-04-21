package edu.kennesaw.smarthome.domain.device.thermostat;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.device.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.DeviceType;

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
        ThermostatState initialState = STATES.get(ThermostatStateType.OFF); // Initial state for all Thermostats is set here.
        ThermostatMode initialMode = MODES.get(ThermostatModeType.AUTO);    // Initial mode fora all Thermostats is set here.
        Temperature initialDesiredTemperature = new Temperature(72);    // Inital temperature for all Thermostats is set here. 
        Temperature ambientTemperature = new Temperature(60);   // Associated ambient temperatures are also created here with default values.
        // Temperature serves as a value object, so using a DI container to set up these objects seems like added complexity.
        
        return new Thermostat(request.name(), 
                            request.location(), 
                            initialState, 
                            STATES,
                            initialMode,
                            initialDesiredTemperature,
                            ambientTemperature,
                            MODES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DOOR_LOCK;
    }
}

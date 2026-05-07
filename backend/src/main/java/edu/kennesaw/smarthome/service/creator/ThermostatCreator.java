package edu.kennesaw.smarthome.service.creator;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.service.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.service.dto.DeviceSnapshot;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
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
        
    private final Map<String, ThermostatState> STATES;
    private final Map<String, ThermostatMode> MODES;

    // Spring provides a List containing an instance from each concrete ThermostatState.
    // Spring also provides a List containing an instance from each concrete ThermostatMode.
    public ThermostatCreator(List<ThermostatState> stateList, List<ThermostatMode> modeList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(state -> {return state.getStateType().toString();}, Function.identity()));
        this.MODES = modeList.stream()
                .collect(Collectors.toMap(mode -> {return mode.getModeType().toString();}, Function.identity()));
    }
    
    @Override
    public Device<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> createDevice(DeviceCreationRequest request) {
        return new Thermostat(  request.name(), 
                                request.location(), 
                                STATES.get(initialState()), 
                                STATES,
                                MODES.get(initialMode()),
                                initialDesiredTemperature(),
                                initialAmbientTemperature(),
                                MODES);
    }

    @Override
    public Device<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> createDevice(DeviceSnapshot snapshot) {
        return new Thermostat(  UUID.fromString(snapshot.id()),
                                snapshot.name(), 
                                snapshot.location(), 
                                STATES.get(snapshot.state()), 
                                STATES,
                                MODES.get(snapshot.attributes().get("mode")),
                                new Temperature(Integer.parseInt(snapshot.attributes().get("desired"))),
                                new Temperature(Integer.parseInt(snapshot.attributes().get("ambient"))),
                                MODES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.THERMOSTAT;
    }

    public static String initialState() {
        return ThermostatStateType.OFF.toString();
    }

    // Initial mode for all Thermostat instances is defined here.
    public static String initialMode() {
        return ThermostatModeType.AUTO.toString();
    }

    // Temperature mainly serves as a values class, so managing it through Spring
    // would only add more complexity. Hence, instances are created manually here.

    // Initial desired temperature for all Thermostat instances is defined here.
    public static Temperature initialDesiredTemperature() {
        return new Temperature(72);
    }

    // Initial ambient temperature for all Thermostat instances is defined here.
    public static Temperature initialAmbientTemperature() {
        return new Temperature(60);
    }
}

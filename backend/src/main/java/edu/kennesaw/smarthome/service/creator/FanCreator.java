package edu.kennesaw.smarthome.service.creator;

import java.util.Arrays;
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
import edu.kennesaw.smarthome.domain.device.fan.Fan;
import edu.kennesaw.smarthome.domain.device.fan.FanAction;
import edu.kennesaw.smarthome.domain.device.fan.FanSpeed;
import edu.kennesaw.smarthome.domain.device.fan.FanState;
import edu.kennesaw.smarthome.domain.device.fan.FanStateType;

@Component
public class FanCreator implements DeviceCreator<Fan, FanState, FanAction, FanStateType> {

    private final Map<String, FanState> STATES;
    private final Map<String, FanSpeed> SPEEDS;

    // Spring provides a List containing an instance from each concrete FanState, as well as each enum instance from FanSpeed.
    public FanCreator(List<FanState> stateList, List<FanSpeed> speedList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(state -> {return state.getStateType().toString();}, Function.identity()));
        this.SPEEDS = Arrays.stream(FanSpeed.values())
            .collect(Collectors.toMap(FanSpeed::toString, Function.identity()));
    }
    
    @Override
    public Device<Fan, FanState, FanAction, FanStateType> createDevice(DeviceCreationRequest request) {
        return new Fan( request.name(), 
                        request.location(), 
                        STATES.get(initialState()),
                        STATES,
                        SPEEDS,
                        SPEEDS.get(initialFanSpeed()));
    }

    @Override
    public Device<Fan, FanState, FanAction, FanStateType> createDevice(DeviceSnapshot snapshot) {
        return new Fan( UUID.fromString(snapshot.id()),
                        snapshot.name(), 
                        snapshot.location(), 
                        STATES.get(snapshot.state()),
                        STATES,
                        SPEEDS,
                        SPEEDS.get(snapshot.attributes().get("speed")));
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.FAN;
    }

    public static String initialState() {
        return FanStateType.OFF.toString();
    }

    // Initial speed for all Fan instances is defined here.
    public static String initialFanSpeed() {
        return FanSpeed.MEDIUM.toString();
    }
}

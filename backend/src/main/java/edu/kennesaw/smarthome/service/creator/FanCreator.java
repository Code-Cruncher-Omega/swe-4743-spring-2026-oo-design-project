package edu.kennesaw.smarthome.service.creator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.application.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.fan.Fan;
import edu.kennesaw.smarthome.domain.device.fan.FanAction;
import edu.kennesaw.smarthome.domain.device.fan.FanSpeed;
import edu.kennesaw.smarthome.domain.device.fan.FanState;
import edu.kennesaw.smarthome.domain.device.fan.FanStateType;

@Component
public class FanCreator implements DeviceCreator<Fan, FanState, FanAction, FanStateType> {

    private final Map<FanStateType, FanState> STATES;

    // Spring provides a List containing an instance from each concrete FanState.
    public FanCreator(List<FanState> stateList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(
                        FanState::getStateType,
                        Function.identity()
                ));
    }
    
    @Override
    public Device<Fan, FanState, FanAction, FanStateType> createDevice(DeviceCreationRequest request) {
        return new Fan( request.name(), 
                        request.location(), 
                        initialState(),
                        STATES,
                        initialFanSpeed());
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.FAN;
    }

    @Override
    public FanState initialState() {
        return STATES.get(FanStateType.OFF);
    }

    // Initial speed for all Fan instances is defined here.
    public FanSpeed initialFanSpeed() {
        return FanSpeed.MEDIUM;
    }
}

package edu.kennesaw.smarthome.domain.device.fan;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;

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
        FanState initialState = STATES.get(FanStateType.OFF);  // Initial state for all Fans is set here.
        FanSpeed initialSpeed = FanSpeed.MEDIUM;    // Initial speed for all Fans is set here.
        
        return new Fan(request.name(), 
                            request.location(), 
                            initialState,
                            initialSpeed, 
                            STATES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.FAN;
    }
}

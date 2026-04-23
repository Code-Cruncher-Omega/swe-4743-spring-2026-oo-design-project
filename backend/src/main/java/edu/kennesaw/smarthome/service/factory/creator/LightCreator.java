package edu.kennesaw.smarthome.service.factory.creator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.light.Light;
import edu.kennesaw.smarthome.domain.device.light.LightAction;
import edu.kennesaw.smarthome.domain.device.light.LightState;
import edu.kennesaw.smarthome.domain.device.light.LightStateType;

@Component
public class LightCreator implements DeviceCreator<Light, LightState, LightAction, LightStateType> {
    private final Map<LightStateType, LightState> STATES;

    // Spring provides a List containing an instance from each concrete LightState.
    public LightCreator(List<LightState> stateList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(
                        LightState::getStateType,
                        Function.identity()
                ));
    }
    
    @Override
    public Device<Light, LightState, LightAction, LightStateType> createDevice(DeviceCreationRequest request) {
        LightState initialState = STATES.get(LightStateType.OFF);  // Initial state for all Lights is set here.
        int initialBrightness = 100;    // Initial brightness (10 - 100) for all Lights is set here.
        int[] initialColor = new int[] {255, 255, 255}; // Initial color (0 - 255 for each r, g, b value) for all Lights is set here.
        
        return new Light(   request.name(), 
                            request.location(), 
                            initialState,
                            STATES,
                            initialBrightness,
                            initialColor);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.FAN;
    }
}

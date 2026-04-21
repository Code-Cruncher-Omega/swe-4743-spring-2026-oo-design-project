package edu.kennesaw.smarthome.domain.device.light;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.device.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.DeviceType;

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
        int[] initialColor = new int[] {255, 255, 255}; // Initial color (0 - 255 for each r, g, b value) is set here.
        
        return new Light(request.name(), 
                            request.location(), 
                            initialState,
                            initialBrightness,
                            initialColor, 
                            STATES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.FAN;
    }
}

package edu.kennesaw.smarthome.service.creator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.service.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
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
                .collect(Collectors.toMap(LightState::getStateType, Function.identity()));
    }
    
    @Override
    public Device<Light, LightState, LightAction, LightStateType> createDevice(DeviceCreationRequest request) {
        return new Light(   request.name(), 
                            request.location(), 
                            initialState(),
                            STATES,
                            initialBrightness(),
                            initialColor());
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.LIGHT;
    }

    @Override
    public LightState initialState() {
        return STATES.get(LightStateType.OFF);
    }

    // Initial brightness for all Light instances is defined here.
    public int initialBrightness() {
        return 100;
    }

    // Initial speed for all Light instances is defined here.
    public int[] initialColor() {
        return new int[] {255, 255, 255};
    }
}

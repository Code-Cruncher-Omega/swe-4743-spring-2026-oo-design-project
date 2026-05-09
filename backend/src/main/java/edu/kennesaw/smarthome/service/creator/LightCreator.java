package edu.kennesaw.smarthome.service.creator;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.light.Light;
import edu.kennesaw.smarthome.domain.device.light.LightAction;
import edu.kennesaw.smarthome.domain.device.light.LightState;
import edu.kennesaw.smarthome.domain.device.light.LightStateType;
import edu.kennesaw.smarthome.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.dto.DeviceSnapshot;

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
                            STATES.get(initialState()),
                            STATES,
                            initialBrightness(),
                            initialColor());
    }

    @Override
    public Device<Light, LightState, LightAction, LightStateType> createDevice(DeviceSnapshot snapshot) {
        return new Light(   UUID.fromString(snapshot.id()),
                            snapshot.name(), 
                            snapshot.location(), 
                            STATES.get(LightStateType.valueOf(snapshot.state())),
                            STATES,
                            (int) snapshot.attributes().get("brightness"),
                            new int[] {
                                (int) snapshot.attributes().get("red"),
                                (int) snapshot.attributes().get("green"),
                                (int) snapshot.attributes().get("blue")
                            });
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.LIGHT;
    }

    public static LightStateType initialState() {
        return LightStateType.OFF;
    }

    // Initial brightness for all Light instances is defined here.
    public static int initialBrightness() {
        return 100;
    }

    // Initial speed for all Light instances is defined here.
    public static int[] initialColor() {
        return new int[] {255, 255, 255};
    }
}

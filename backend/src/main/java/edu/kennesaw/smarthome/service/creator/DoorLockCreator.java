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
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLock;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockAction;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockState;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockStateType;

@Component
public class DoorLockCreator implements DeviceCreator<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> {

    private final Map<String, DoorLockState> STATES;

    // Spring provides a List containing an instance from each concrete DoorLockState.
    public DoorLockCreator(List<DoorLockState> stateList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(state -> {return state.getStateType().toString();}, Function.identity()));
    }
    
    @Override
    public Device<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> createDevice(DeviceCreationRequest request) {
        return new DoorLock(request.name(), 
                            request.location(), 
                            STATES.get(initialState().toString()), 
                            STATES);
    }

    @Override
    public Device<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> createDevice(DeviceSnapshot snapshot) {
        return new DoorLock(UUID.fromString(snapshot.id()),
                            snapshot.name(), 
                            snapshot.location(), 
                            STATES.get(snapshot.state()), 
                            STATES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DOOR_LOCK;
    }

    public static String initialState() {
        return DoorLockStateType.LOCKED.toString();
    }
}

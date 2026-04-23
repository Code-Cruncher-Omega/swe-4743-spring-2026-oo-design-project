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
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLock;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockAction;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockState;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockStateType;

@Component
public class DoorLockCreator implements DeviceCreator<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> {

    private final Map<DoorLockStateType, DoorLockState> STATES;

    // Spring provides a List containing an instance from each concrete DoorLockState.
    public DoorLockCreator(List<DoorLockState> stateList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(
                        DoorLockState::getStateType,
                        Function.identity()
                ));
    }
    
    @Override
    public Device<DoorLock, DoorLockState, DoorLockAction, DoorLockStateType> createDevice(DeviceCreationRequest request) {
        DoorLockState initialState = STATES.get(DoorLockStateType.LOCKED);  // Initial state for all Door Locks is set here.
        
        return new DoorLock(request.name(), 
                            request.location(), 
                            initialState, 
                            STATES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DOOR_LOCK;
    }
}

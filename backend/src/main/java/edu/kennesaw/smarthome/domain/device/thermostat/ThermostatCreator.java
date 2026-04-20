package edu.kennesaw.smarthome.domain.device.thermostat;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.kennesaw.smarthome.domain.DeviceCreationRequest;
import edu.kennesaw.smarthome.domain.DeviceCreator;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLock;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockAction;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockState;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockStateType;

public class ThermostatCreator implements DeviceCreator<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> {
    private final Map<ThermostatStateType, ThermostatState> STATES;
    private final Map<ThermostatModeType, ThermostatMode> MODES;

    // Spring provides a List containing an instance from each concrete ThermostatState.
    public ThermostatCreator(List<ThermostatState> stateList, List<ThermostatMode> modeList) {
        this.STATES = stateList.stream()
                .collect(Collectors.toMap(
                        ThermostatState::getStateType,
                        Function.identity()
                ));
        this.MODES = modeList.stream()
                .collect(Collectors.toMap(
                        ThermostatMode::getModeType,
                        Function.identity()
                ));
    }
    
    @Override
    public Device<Thermostat, ThermostatState, ThermostatAction, ThermostatStateType> createDevice(DeviceCreationRequest request) {
        ThermostatState initialState = STATES.get(ThermostatStateType.OFF);  // Initial state for all Door Locks is set here.
        ThermostatMode initialMode = 
        
        return new Thermostat(request.name(), 
                            request.location(), 
                            initialState, 
                            STATES);
    }
    
    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DOOR_LOCK;
    }
}

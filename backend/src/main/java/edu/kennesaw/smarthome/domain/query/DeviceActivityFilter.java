package edu.kennesaw.smarthome.domain.query;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.StateActivity;

@Component
public class DeviceActivityFilter extends EnvironmentDeviceQueryDecoratorBase {

    private final StateActivity ACTIVITY;

    public DeviceActivityFilter(EnvironmentDeviceQuery inner, Environment environment, StateActivity activity) {
        super(inner, environment);
        this.ACTIVITY = activity;
    }

    @Override
    public Environment run() {
        Map<UUID, Device<?, ?, ?, ?>> filteredDevices = new HashMap<>();

        for(Device<?, ?, ?, ?> device : environment.getDevices().values()) {    // Check every device in the environment.
            if(device.getState().getStateActivity().equals(ACTIVITY)) {         // If the device's state activity matches ACTIVITY,
                filteredDevices.put(device.getId(), device);                    // add it to filteredDevices.
            }
        }

        return new Environment(environment.getName(), filteredDevices);
    }

    @Override
    public EnvironmentDeviceFilterType getFilterType() {
        return EnvironmentDeviceFilterType.ACTIVITY;
    }
}

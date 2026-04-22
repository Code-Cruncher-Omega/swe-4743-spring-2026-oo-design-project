package edu.kennesaw.smarthome.domain;

import java.util.HashMap;
import java.util.Map;

import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceType;
import edu.kennesaw.smarthome.domain.device.thermostat.Thermostat;

public class Environment {

    private final String NAME;

    private Map<String, Device<?, ?, ?, ?>> devices;
    private Thermostat thermostat;  // Only one thermostat is allowed per-environment. (stored in devices too)

    public Environment(String name, Map<String, Device<?, ?, ?, ?>> devices) {
        this.NAME = name;
        this.devices = devices;
        this.thermostat = null;
        // Search through the devices the environment has, and if a thermostat is found,
        // then store it to quickly check for other factors.
        for(Device<?, ?, ?, ?> device : devices.values()) {
            if(device.getType().equals(DeviceType.THERMOSTAT)) {
                thermostat = (Thermostat) device;
                break;
            }
        }
    }

    public Environment(String name) {
        this.NAME = name;
        this.devices = new HashMap<>();
        // Environments with no devices start with no thermostat.
        this.thermostat = null;
    }

    protected Map<String, Device<?, ?, ?, ?>> getDevices() {
        return devices;
    }

    // Used for controlling ambient temperature per environment (aka location).
    protected Thermostat getThermostat() {
        return thermostat;
    }

    public Device<?, ?, ?, ?> getDevice(String deviceName) {
        return devices.get(deviceName);
    }

    public String getName() {
        return NAME;
    }

    // Devices are not allowed to have the same name.
    // No more than 1 Thermostat can exist in each environment.
    public EnvironmentResult addDevice(Device<?, ?, ?, ?> newDevice) {
        if(devices.containsKey(newDevice.getName())) {
            return new EnvironmentResult(false, "ADD_DEVICE_TO_" + NAME.toUpperCase(), "A device already exists with the same name " + newDevice.getName());
        }
        if(newDevice.getType().equals(DeviceType.THERMOSTAT)) {
            if(thermostat != null) {
                return new EnvironmentResult(false, "ADD_THERMOSTAT_TO_" + NAME.toUpperCase(), "A thermostat already exists in " + NAME);
            }
            devices.put(newDevice.getName(), newDevice);
            thermostat = (Thermostat) newDevice;
            return new EnvironmentResult(true, "ADD_THERMOSTAT_TO_" + NAME.toUpperCase(), "Successfully added " + newDevice.getName() + " to " + NAME);
        }
        devices.put(newDevice.getName(), newDevice);
        return new EnvironmentResult(true, "ADD_" + newDevice.getType().name() + "TO_" + NAME.toUpperCase(), "Successfully added " + newDevice.getName() + " to " + NAME);
    }

    // If a device is a Thermostat, then reset ambientTemperature back to null.
    public EnvironmentResult removeDevice(String name) {
        if(devices.containsKey(name)) {
            Device<?, ?, ?, ?> removedDevice = devices.remove(name);
            if(removedDevice.getType().equals(DeviceType.THERMOSTAT)) {
                thermostat = null;
            }
            return new EnvironmentResult(true, "REMOVE_DEVICE_FROM_" + NAME.toUpperCase(), "Successfully removed " + removedDevice.getName() + " from " + NAME);
        }
        return new EnvironmentResult(false, "REMOVE_DEVICE_FROM_" + NAME.toUpperCase(), "No device named " + name + " could be found");
    }

    public EnvironmentResult resetAllDevices() {
        for(Device<?, ?, ?, ?> device : devices.values()) {
            if(!device.reset().success()) {
                return new EnvironmentResult(false, "RESET_ALL_DEVICES", device + " could not be reset. Halting the reset process for " + NAME);
            }
        }
        return new EnvironmentResult(true, "RESET_ALL_DEVICES", "Successfully resetted all devices in " + NAME);
    }

    @Override
    public String toString() {
        return "Environment: " + NAME + "(" + devices + ", Thermostat: [" + (thermostat != null ? thermostat : "null") + "])";
    }
}

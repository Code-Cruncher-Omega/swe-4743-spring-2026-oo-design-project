package edu.kennesaw.smarthome.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.abstraction.UpdateableDevice;
import edu.kennesaw.smarthome.domain.device.thermostat.Thermostat;
import edu.kennesaw.smarthome.dto.EnvironmentResult;
import edu.kennesaw.smarthome.dto.EnvironmentStatus;

public class Environment {

    private final String NAME;

    private Map<UUID, Device<?, ?, ?, ?>> devices;
    private Thermostat thermostat;  // Only one thermostat is allowed per-environment. (stored in devices too)

    public Environment(String name, Map<UUID, Device<?, ?, ?, ?>> devices) {
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

    // Devices are not allowed to have the same UUID.
    // No more than 1 Thermostat can exist in each environment.
    public EnvironmentResult addDevice(Device<?, ?, ?, ?> newDevice) {
        if(devices.containsKey(newDevice.getId())) {
            return new EnvironmentResult(false, "ADD_DEVICE_TO_" + NAME.toUpperCase(), "A device already exists with the same ID " + newDevice.getId());
        }
        if(newDevice.getType().equals(DeviceType.THERMOSTAT)) {
            if(thermostat != null) {
                return new EnvironmentResult(false, "ADD_THERMOSTAT_TO_" + NAME.toUpperCase(), "A thermostat already exists in " + NAME);
            }
            devices.put(newDevice.getId(), newDevice);
            thermostat = (Thermostat) newDevice;
            return new EnvironmentResult(true, "ADD_THERMOSTAT_TO_" + NAME.toUpperCase(), "Successfully added " + newDevice.getName() + " to " + NAME);
        }
        devices.put(newDevice.getId(), newDevice);
        return new EnvironmentResult(true, "ADD_" + newDevice.getType().toString() + "_TO_" + NAME.toUpperCase(), "Successfully added " + newDevice.getName() + " to " + NAME);
    }

    // If a device is a Thermostat, then reset ambientTemperature back to null.
    public EnvironmentResult removeDevice(UUID id) {
        Device<?, ?, ?, ?> removedDevice = devices.remove(id);
        if(removedDevice == null) {
            return new EnvironmentResult(false, "REMOVE_DEVICE_FROM_" + NAME.toUpperCase(), "No device with ID " + id + " could be found");
        }
        if(removedDevice.getType().equals(DeviceType.THERMOSTAT)) {
                thermostat = null;
        }
        return new EnvironmentResult(true, "REMOVE_" + removedDevice.getType().toString() + "_FROM_" + NAME.toUpperCase(), "Successfully removed device with ID " + id + " from " + NAME);
    }

    public EnvironmentResult resetAllDevices() {
        for(Device<?, ?, ?, ?> device : devices.values()) {
            device.reset();
        }
        return new EnvironmentResult(true, "RESET_ALL_DEVICES_IN_" + NAME.toUpperCase(), "Successfully resetted all devices in " + NAME);
    }

    // Updates any devices that run on ticks, in this case it is Thermostat, if there is one.
    public void update(int tickRate) {
        for(Device<?, ?, ?, ?> device : devices.values()) {
            if(device instanceof UpdateableDevice) {    // I feel icky about this, but this just seems better than adding a switch-case.
                ((UpdateableDevice) device).update(tickRate);
            }
        }
    }

    public Collection<Device<?, ?, ?, ?>> getDevices() {
        return Collections.unmodifiableCollection(devices.values());
    }

    public Device<?, ?, ?, ?> getDevice(UUID deviceName) {
        return devices.get(deviceName);
    }

    public String getName() {
        return NAME;
    }

    public EnvironmentStatus getStatus() {
        return EnvironmentStatus.of(this);
    }

    public EnvironmentResult setAmbientTemperature(int newTemp) {
        if(thermostat != null) {
            thermostat.setAmbientTemperature(newTemp);
            return new EnvironmentResult(true, "SET_ENVIRONMENT_AMBIENT_TEMPERATURE", NAME + " ambient temperature updated to " + newTemp + " Farenheit.");
        }
        return new EnvironmentResult(false, "SET_ENVIRONMENT_AMBIENT_TEMPERATURE", NAME + " must have a thermostat before ambient temperature can be changed.");
    }

    @Override
    public String toString() {
        return "Environment: " + NAME + "(" + devices + ", Thermostat: [" + (thermostat != null ? thermostat : "null") + "])";
    }
}

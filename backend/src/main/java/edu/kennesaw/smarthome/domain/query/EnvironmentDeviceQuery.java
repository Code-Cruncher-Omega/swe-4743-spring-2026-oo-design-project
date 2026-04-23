package edu.kennesaw.smarthome.domain.query;

import edu.kennesaw.smarthome.domain.Environment;

// Filter the devices in a Environment object.
public interface EnvironmentDeviceQuery {
    public Environment run();
    public EnvironmentDeviceFilterType getFilterType();
}

package edu.kennesaw.smarthome.domain.query;

import org.springframework.stereotype.Component;

import edu.kennesaw.smarthome.domain.Environment;

@Component
public class DeviceLocationFilter extends EnvironmentDeviceQueryDecoratorBase {
    
    private final String LOCATION;

    public DeviceLocationFilter(EnvironmentDeviceQuery inner, Environment environment, String location) {
        super(inner, environment);
        this.LOCATION = location;
    }

    @Override
    // Return the environment if the location matches its name, otherwise return an empty Environment with the same name (case-insensitive).
    // It is assumed that every Device inside a Environment shares the same location (Environemnt).
    public Environment run() {
        return environment.getName().toLowerCase().equals(LOCATION.toLowerCase()) ? environment : new Environment(environment.getName());
    }

    @Override
    public EnvironmentDeviceFilterType getFilterType() {
        return EnvironmentDeviceFilterType.LOCATION;
    }
}

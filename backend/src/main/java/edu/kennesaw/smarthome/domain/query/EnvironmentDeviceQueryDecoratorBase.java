package edu.kennesaw.smarthome.domain.query;

import edu.kennesaw.smarthome.domain.Environment;

public abstract class EnvironmentDeviceQueryDecoratorBase implements EnvironmentDeviceQuery {
    
    protected final EnvironmentDeviceQuery INNER;

    protected Environment environment;

    protected EnvironmentDeviceQueryDecoratorBase(EnvironmentDeviceQuery inner, Environment environment) {
        this.INNER = inner;
        this.environment = environment;
    }
}

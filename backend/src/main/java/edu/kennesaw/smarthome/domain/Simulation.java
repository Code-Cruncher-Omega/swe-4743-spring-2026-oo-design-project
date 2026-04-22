package edu.kennesaw.smarthome.domain;

import java.util.HashMap;
import java.util.Map;

public class Simulation {
    
    private Map<String, Environment> realEnvironments;  // Stores all environments that will change. (interacts with its contents)
    private Map<String, Environment> filteredEnvironments;  // Stores all environments to present. (only displays contents)
    private int tickSpeed;

    public Simulation(Map<String, Environment> environments) {
        this.realEnvironments = environments;
        this.tickSpeed = 1;
    }

    public Simulation() {
        this.realEnvironments = new HashMap<>();
        this.tickSpeed = 1;
    }
}

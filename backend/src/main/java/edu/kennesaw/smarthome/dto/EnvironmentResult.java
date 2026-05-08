package edu.kennesaw.smarthome.dto;

public record EnvironmentResult(

    boolean success, 
    String action, 
    String message
) {}

package edu.kennesaw.smarthome.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.kennesaw.smarthome.domain.Environment;
import edu.kennesaw.smarthome.domain.query.DeviceLocationFilter;
import edu.kennesaw.smarthome.domain.query.EnvironmentDeviceFilterType;
import edu.kennesaw.smarthome.domain.query.EnvironmentDeviceQuery;

@Service
public class EnvironmentDeviceQueryService {
    
    private final Map<EnvironmentDeviceFilterType, EnvironmentDeviceQuery> DEVICE_FILTERS;

    // Spring provides a List containing an instance from each concrete EnvironmentDeviceQuery.
    public EnvironmentDeviceQueryService(List<EnvironmentDeviceQuery> environmentDeviceQueries) {
        this.DEVICE_FILTERS = environmentDeviceQueries.stream()
                .collect(Collectors.toMap(
                        EnvironmentDeviceQuery::getFilterType,
                        Function.identity()
                ));
    }

    public Environment filterByLocation(Environment environment, String location) {
        return ((DeviceLocationFilter) (DEVICE_FILTERS.get(EnvironmentDeviceFilterType.LOCATION))).
    }
}

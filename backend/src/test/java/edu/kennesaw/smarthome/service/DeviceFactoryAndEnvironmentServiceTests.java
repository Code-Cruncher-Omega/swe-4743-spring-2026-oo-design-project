package edu.kennesaw.smarthome.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kennesaw.smarthome.domain.device.abstraction.Device;
import edu.kennesaw.smarthome.domain.device.abstraction.DeviceType;
import edu.kennesaw.smarthome.domain.device.fan.FanSpeed;
import edu.kennesaw.smarthome.domain.device.fan.OffState;
import edu.kennesaw.smarthome.domain.device.fan.OnState;
import edu.kennesaw.smarthome.domain.device.thermostat.AutoMode;
import edu.kennesaw.smarthome.domain.device.thermostat.CoolMode;
import edu.kennesaw.smarthome.domain.device.thermostat.CoolingState;
import edu.kennesaw.smarthome.domain.device.thermostat.HeatMode;
import edu.kennesaw.smarthome.domain.device.thermostat.HeatingState;
import edu.kennesaw.smarthome.domain.device.thermostat.IdleState;
import edu.kennesaw.smarthome.dto.DeviceCreationRequest;
import edu.kennesaw.smarthome.service.creator.DoorLockCreator;
import edu.kennesaw.smarthome.service.creator.FanCreator;
import edu.kennesaw.smarthome.service.creator.LightCreator;
import edu.kennesaw.smarthome.service.creator.ThermostatCreator;

class DeviceFactoryAndEnvironmentServiceTests {
    private DeviceFactory deviceFactory;
    private EnvironmentService environmentService;

    @BeforeEach
    void setup() {
        LightCreator lightCreator = new LightCreator(List.of(
            new edu.kennesaw.smarthome.domain.device.light.OffState(),
            new edu.kennesaw.smarthome.domain.device.light.OnState()
        ));
        FanCreator fanCreator = new FanCreator(
            List.of(new OffState(), new OnState()),
            List.of(FanSpeed.LOW, FanSpeed.MEDIUM, FanSpeed.HIGH)
        );
        ThermostatCreator thermostatCreator = new ThermostatCreator(
            List.of(
                new edu.kennesaw.smarthome.domain.device.thermostat.OffState(),
                new IdleState(),
                new HeatingState(),
                new CoolingState()
            ),
            List.of(new AutoMode(), new HeatMode(), new CoolMode())
        );
        DoorLockCreator doorLockCreator = new DoorLockCreator(List.of(new edu.kennesaw.smarthome.domain.device.doorlock.LockedState(), new edu.kennesaw.smarthome.domain.device.doorlock.UnlockedState()));

        deviceFactory = new DeviceFactory(List.of(lightCreator, fanCreator, thermostatCreator, doorLockCreator));
        environmentService = new EnvironmentService();
    }

    @Test
    void DeviceFactory_CreateDevice_CorrectTypeReturnedForEachDeviceType() {
        Device<?, ?, ?, ?> light = deviceFactory.create(new DeviceCreationRequest("Desk Light", "Office", DeviceType.LIGHT));
        assertEquals(DeviceType.LIGHT, light.getType());

        Device<?, ?, ?, ?> fan = deviceFactory.create(new DeviceCreationRequest("Ceiling Fan", "Office", DeviceType.FAN));
        assertEquals(DeviceType.FAN, fan.getType());

        Device<?, ?, ?, ?> thermostat = deviceFactory.create(new DeviceCreationRequest("Room Thermostat", "Office", DeviceType.THERMOSTAT));
        assertEquals(DeviceType.THERMOSTAT, thermostat.getType());

        Device<?, ?, ?, ?> doorLock = deviceFactory.create(new DeviceCreationRequest("Front Door", "Office", DeviceType.DOOR_LOCK));
        assertEquals(DeviceType.DOOR_LOCK, doorLock.getType());
    }

    @Test
    void DeviceRegistration_ValidDeviceCreatedWithDefaultState() {
        Device<?, ?, ?, ?> device = deviceFactory.create(new DeviceCreationRequest("Desk Light", "Office", DeviceType.LIGHT));
        assertEquals(DeviceType.LIGHT, device.getType());
        assertEquals(edu.kennesaw.smarthome.domain.device.light.LightStateType.OFF, ((edu.kennesaw.smarthome.domain.device.light.Light) device).getState().getStateType());
        assertEquals(100, ((edu.kennesaw.smarthome.domain.device.light.Light) device).getBrightness());

        environmentService.addDevice(device);
        assertSame(device, environmentService.getDevice(device.getId()));
    }

    @Test
    void DeviceRemoval_DeviceDeletedNoLongerRetrievable() {
        Device<?, ?, ?, ?> device = deviceFactory.create(new DeviceCreationRequest("Desk Light", "Office", DeviceType.LIGHT));
        SmartHomeService smartHomeService = new SmartHomeService(deviceFactory, environmentService, mock(DeviceFilterService.class));
        smartHomeService.removeDevice(device.getId().toString());

        assertThrows(IllegalArgumentException.class, () -> environmentService.getDevice(device.getId()));
    }

    @Test
    void ThermostatInvariant_RegisteringSecondThermostatInSameLocationRejected() {
        Device<?, ?, ?, ?> firstThermostat = deviceFactory.create(new DeviceCreationRequest("Thermostat A", "Office", DeviceType.THERMOSTAT));
        Device<?, ?, ?, ?> secondThermostat = deviceFactory.create(new DeviceCreationRequest("Thermostat B", "Office", DeviceType.THERMOSTAT));

        environmentService.addDevice(firstThermostat);
        environmentService.addDevice(secondThermostat);

        assertThrows(IllegalArgumentException.class, () -> environmentService.getDevice(secondThermostat.getId()));
        assertEquals(1, environmentService.getAllEnvironments().iterator().next().getDevices().size());
    }
}

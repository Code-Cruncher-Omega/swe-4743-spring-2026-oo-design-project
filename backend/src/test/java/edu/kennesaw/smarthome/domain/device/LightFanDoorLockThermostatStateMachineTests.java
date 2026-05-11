package edu.kennesaw.smarthome.domain.device;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import edu.kennesaw.smarthome.domain.device.doorlock.DoorLock;
import edu.kennesaw.smarthome.domain.device.doorlock.DoorLockStateType;
import edu.kennesaw.smarthome.domain.device.doorlock.LockedState;
import edu.kennesaw.smarthome.domain.device.doorlock.UnlockedState;
import edu.kennesaw.smarthome.domain.device.fan.Fan;
import edu.kennesaw.smarthome.domain.device.fan.FanAction;
import edu.kennesaw.smarthome.domain.device.fan.FanSpeed;
import edu.kennesaw.smarthome.domain.device.fan.FanState;
import edu.kennesaw.smarthome.domain.device.fan.FanStateType;
import edu.kennesaw.smarthome.domain.device.fan.OffState;
import edu.kennesaw.smarthome.domain.device.fan.OnState;
import edu.kennesaw.smarthome.domain.device.light.Light;
import edu.kennesaw.smarthome.domain.device.light.LightStateType;
import edu.kennesaw.smarthome.domain.device.thermostat.AutoMode;
import edu.kennesaw.smarthome.domain.device.thermostat.CoolMode;
import edu.kennesaw.smarthome.domain.device.thermostat.CoolingState;
import edu.kennesaw.smarthome.domain.device.thermostat.HeatMode;
import edu.kennesaw.smarthome.domain.device.thermostat.HeatingState;
import edu.kennesaw.smarthome.domain.device.thermostat.IdleState;
import edu.kennesaw.smarthome.domain.device.thermostat.Temperature;
import edu.kennesaw.smarthome.domain.device.thermostat.Thermostat;
import edu.kennesaw.smarthome.domain.device.thermostat.ThermostatModeType;
import edu.kennesaw.smarthome.domain.device.thermostat.ThermostatStateType;
import edu.kennesaw.smarthome.dto.DeviceResult;

class LightFanDoorLockThermostatStateMachineTests {

    private Light createLight() {
        Map<LightStateType, edu.kennesaw.smarthome.domain.device.light.LightState> states = Map.of(
            LightStateType.OFF, new edu.kennesaw.smarthome.domain.device.light.OffState(),
            LightStateType.ON, new edu.kennesaw.smarthome.domain.device.light.OnState()
        );
        return new Light("Test Light", "Living Room", states.get(LightStateType.OFF), states, 100, new int[] {255, 255, 255});
    }

    private Fan createFan() {
        Map<FanStateType, FanState> states = Map.of(
            FanStateType.OFF, new OffState(),
            FanStateType.ON, new OnState()
        );
        Map<String, FanSpeed> speeds = Map.of(
            FanSpeed.LOW.toString(), FanSpeed.LOW,
            FanSpeed.MEDIUM.toString(), FanSpeed.MEDIUM,
            FanSpeed.HIGH.toString(), FanSpeed.HIGH
        );
        return new Fan("Test Fan", "Living Room", states.get(FanStateType.OFF), states, speeds, FanSpeed.MEDIUM);
    }

    private Thermostat createThermostat(int ambient, int desired, ThermostatModeType initialModeType) {
        Map<ThermostatStateType, edu.kennesaw.smarthome.domain.device.thermostat.ThermostatState> states = Map.of(
            ThermostatStateType.OFF, new edu.kennesaw.smarthome.domain.device.thermostat.OffState(),
            ThermostatStateType.IDLE, new IdleState(),
            ThermostatStateType.HEATING, new HeatingState(),
            ThermostatStateType.COOLING, new CoolingState()
        );
        Map<ThermostatModeType, edu.kennesaw.smarthome.domain.device.thermostat.ThermostatMode> modes = Map.of(
            ThermostatModeType.AUTO, new AutoMode(),
            ThermostatModeType.HEAT, new HeatMode(),
            ThermostatModeType.COOL, new CoolMode()
        );
        return new Thermostat(
            "Test Thermostat",
            "Living Room",
            states.get(ThermostatStateType.OFF),
            states,
            modes.get(initialModeType),
            new Temperature(desired),
            new Temperature(ambient),
            modes
        );
    }

    private DoorLock createDoorLock() {
        Map<DoorLockStateType, edu.kennesaw.smarthome.domain.device.doorlock.DoorLockState> states = Map.of(
            DoorLockStateType.LOCKED, new LockedState(),
            DoorLockStateType.UNLOCKED, new UnlockedState()
        );
        return new DoorLock("Test Lock", "Front Door", states.get(DoorLockStateType.LOCKED), states);
    }

    @Test
    void Light_TogglePower_OffToOnThenOnToOff() {
        Light light = createLight();

        assertEquals(LightStateType.OFF, light.getState().getStateType());

        DeviceResult turnOn = light.togglePower();
        assertTrue(turnOn.success());
        assertEquals(LightStateType.ON, light.getState().getStateType());

        DeviceResult turnOff = light.togglePower();
        assertTrue(turnOff.success());
        assertEquals(LightStateType.OFF, light.getState().getStateType());
    }

    @Test
    void Light_ChangeBrightnessWhileOff_Reject() {
        Light light = createLight();

        DeviceResult result = light.changeBrightness(50);

        assertFalse(result.success());
        assertTrue(result.message().contains("Cannot set brightness"));
        assertEquals(LightStateType.OFF, light.getState().getStateType());
    }

    @Test
    void Light_SettingsRetainedAcrossPowerCycle() {
        Light light = createLight();
        light.togglePower();
        assertEquals(LightStateType.ON, light.getState().getStateType());

        DeviceResult brightnessResult = light.changeBrightness(80);
        assertTrue(brightnessResult.success());
        DeviceResult colorResult = light.changeColor(128, 64, 255);
        assertTrue(colorResult.success());

        assertArrayEquals(new int[] {128, 64, 255}, light.getColor());
        assertEquals(80, light.getBrightness());

        light.togglePower();
        assertEquals(LightStateType.OFF, light.getState().getStateType());

        light.togglePower();
        assertEquals(LightStateType.ON, light.getState().getStateType());
        assertEquals(80, light.getBrightness());
        assertArrayEquals(new int[] {128, 64, 255}, light.getColor());
    }

    @Test
    void Light_BrightnessBoundaryValues_AcceptAndReject() {
        Light light = createLight();
        light.togglePower();

        assertTrue(light.changeBrightness(10).success());
        assertTrue(light.changeBrightness(100).success());

        assertFalse(light.changeBrightness(9).success());
        assertFalse(light.changeBrightness(101).success());
        assertFalse(light.changeBrightness(0).success());
        assertFalse(light.changeBrightness(-1).success());
    }

    @Test
    void Thermostat_DesiredTemperatureBoundaryValues_AcceptAndReject() {
        Thermostat thermostat = createThermostat(70, 72, ThermostatModeType.AUTO);
        assertTrue(thermostat.setDesiredTemperature(60).success());
        assertTrue(thermostat.setDesiredTemperature(80).success());

        assertFalse(thermostat.setDesiredTemperature(59).success());
        assertFalse(thermostat.setDesiredTemperature(81).success());
    }

    @Test
    void Fan_InvalidSpeedString_Reject() {
        assertThrows(IllegalArgumentException.class, () -> FanAction.from("SET_SPEED_ULTRA"));
    }

    @Test
    void Fan_TogglePower_OffToOnThenOnToOff() {
        Fan fan = createFan();

        assertEquals(FanStateType.OFF, fan.getState().getStateType());

        DeviceResult turnOn = fan.togglePower();
        assertTrue(turnOn.success());
        assertEquals(FanStateType.ON, fan.getState().getStateType());

        DeviceResult turnOff = fan.togglePower();
        assertTrue(turnOff.success());
        assertEquals(FanStateType.OFF, fan.getState().getStateType());
    }

    @Test
    void Fan_SpeedChangeWhileOn_Valid() {
        Fan fan = createFan();
        fan.togglePower();

        assertTrue(fan.changeSpeedLow().success());
        assertEquals(FanSpeed.LOW, fan.getSpeed());

        assertTrue(fan.changeSpeedMedium().success());
        assertEquals(FanSpeed.MEDIUM, fan.getSpeed());

        assertTrue(fan.changeSpeedHigh().success());
        assertEquals(FanSpeed.HIGH, fan.getSpeed());
    }

    @Test
    void Fan_SpeedChangeWhileOff_Reject() {
        Fan fan = createFan();

        assertThrows(IllegalStateException.class, fan::changeSpeedLow);
        assertThrows(IllegalStateException.class, fan::changeSpeedMedium);
        assertThrows(IllegalStateException.class, fan::changeSpeedHigh);
    }

    @Test
    void Fan_SpeedSurvivesPowerCycle() {
        Fan fan = createFan();
        fan.togglePower();
        fan.changeSpeedHigh();
        assertEquals(FanSpeed.HIGH, fan.getSpeed());

        fan.togglePower();
        assertEquals(FanStateType.OFF, fan.getState().getStateType());

        fan.togglePower();
        assertEquals(FanStateType.ON, fan.getState().getStateType());
        assertEquals(FanSpeed.HIGH, fan.getSpeed());
    }

    @Test
    void Thermostat_OffToIdleToHeatingToIdle_Valid() {
        Thermostat thermostat = createThermostat(65, 72, ThermostatModeType.AUTO);

        assertEquals(ThermostatStateType.OFF, thermostat.getState().getStateType());

        thermostat.togglePower();
        assertEquals(ThermostatStateType.IDLE, thermostat.getState().getStateType());

        DeviceResult firstUpdate = thermostat.update(1);
        assertTrue(firstUpdate.success());
        assertEquals(ThermostatStateType.HEATING, thermostat.getState().getStateType());

        thermostat.update(20);
        assertEquals(72, thermostat.getAmbientTemperature().getValue());
        assertEquals(ThermostatStateType.HEATING, thermostat.getState().getStateType());

        thermostat.update(1);
        assertEquals(ThermostatStateType.IDLE, thermostat.getState().getStateType());
    }

    @Test
    void Thermostat_OffToIdleToCoolingToIdle_Valid() {
        Thermostat thermostat = createThermostat(75, 72, ThermostatModeType.AUTO);

        thermostat.togglePower();
        assertEquals(ThermostatStateType.IDLE, thermostat.getState().getStateType());

        DeviceResult firstUpdate = thermostat.update(1);
        assertTrue(firstUpdate.success());
        assertEquals(ThermostatStateType.COOLING, thermostat.getState().getStateType());

        thermostat.update(20);
        assertEquals(72, thermostat.getAmbientTemperature().getValue());
        assertEquals(ThermostatStateType.COOLING, thermostat.getState().getStateType());

        thermostat.update(1);
        assertEquals(ThermostatStateType.IDLE, thermostat.getState().getStateType());
    }

    @Test
    void Thermostat_UpdateFromOff_Reject() {
        Thermostat thermostat = createThermostat(65, 72, ThermostatModeType.AUTO);

        assertThrows(IllegalStateException.class, () -> thermostat.update(1));
    }

    @Test
    void Thermostat_ModeChangesWhileActive_Accepted() {
        Thermostat thermostat = createThermostat(65, 72, ThermostatModeType.AUTO);
        thermostat.togglePower();
        thermostat.update(1);
        assertEquals(ThermostatStateType.HEATING, thermostat.getState().getStateType());

        thermostat.setModeCool();
        assertEquals(ThermostatModeType.COOL, thermostat.getCurrentMode().getModeType());

        thermostat.setModeHeat();
        assertEquals(ThermostatModeType.HEAT, thermostat.getCurrentMode().getModeType());

        thermostat.setModeAuto();
        assertEquals(ThermostatModeType.AUTO, thermostat.getCurrentMode().getModeType());
    }

    @Test
    void DoorLock_ToggleLock_LockedToUnlockedAndBack() {
        DoorLock doorLock = createDoorLock();

        assertEquals(DoorLockStateType.LOCKED, doorLock.getState().getStateType());

        doorLock.toggleLock();
        assertEquals(DoorLockStateType.UNLOCKED, doorLock.getState().getStateType());

        doorLock.toggleLock();
        assertEquals(DoorLockStateType.LOCKED, doorLock.getState().getStateType());
    }
}

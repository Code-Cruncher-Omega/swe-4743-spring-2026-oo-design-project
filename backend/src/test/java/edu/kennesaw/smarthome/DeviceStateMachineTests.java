package edu.kennesaw.smarthome;

import edu.kennesaw.smarthome.domain.device.doorlock.*;
import edu.kennesaw.smarthome.domain.device.fan.*;
import edu.kennesaw.smarthome.domain.device.light.*;
import edu.kennesaw.smarthome.domain.device.thermostat.*;
import edu.kennesaw.smarthome.dto.DeviceResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * State machine and boundary tests for Smart Home Simulator devices.
 */
public class DeviceStateMachineTests {

    private Light light;
    private Fan fan;
    private Thermostat thermostat;
    private DoorLock doorLock;

    @BeforeEach
    void setUp() {

        // ---------------- LIGHT ----------------

        LightState offLightState = mock(LightState.class);
        LightState onLightState = mock(LightState.class);

        when(offLightState.execute(any(), eq(LightAction.TOGGLE_POWER)))
                .thenAnswer(invocation -> {
                    Light l = invocation.getArgument(0);
                    l.setState(onLightState);

                    return new DeviceResult(
                            true,
                            LightAction.TOGGLE_POWER,
                            "Light turned on"
                    );
                });

        when(onLightState.execute(any(), eq(LightAction.TOGGLE_POWER)))
                .thenAnswer(invocation -> {
                    Light l = invocation.getArgument(0);
                    l.setState(offLightState);

                    return new DeviceResult(
                            true,
                            LightAction.TOGGLE_POWER,
                            "Light turned off"
                    );
                });

        when(onLightState.execute(any(), eq(LightAction.SET_BRIGHTNESS), anyInt()))
                .thenAnswer(invocation -> {
                    int brightness = invocation.getArgument(2);

                    if (brightness < 10 || brightness > 100) {
                        return new DeviceResult(
                                false,
                                LightAction.SET_BRIGHTNESS,
                                "Invalid brightness"
                        );
                    }

                    Light l = invocation.getArgument(0);
                    l.setBrightness(brightness);

                    return new DeviceResult(
                            true,
                            LightAction.SET_BRIGHTNESS,
                            "Brightness changed"
                    );
                });

        when(onLightState.execute(any(), eq(LightAction.SET_COLOR), any(int[].class)))
                .thenAnswer(invocation -> {
                    Light l = invocation.getArgument(0);
                    int[] color = invocation.getArgument(2);

                    l.setColor(color);

                    return new DeviceResult(
                            true,
                            LightAction.SET_COLOR,
                            "Color changed"
                    );
                });

        Map<LightStateType, LightState> lightStates = new HashMap<>();
        lightStates.put(LightStateType.OFF, offLightState);
        lightStates.put(LightStateType.ON, onLightState);

        light = new Light(
                "Bedroom Light",
                "Bedroom",
                offLightState,
                lightStates,
                50,
                new int[]{255, 255, 255}
        );

        // ---------------- FAN ----------------

        FanState fanOffState = mock(FanState.class);
        FanState fanOnState = mock(FanState.class);

        FanSpeed lowSpeed = mock(FanSpeed.class);
        FanSpeed mediumSpeed = mock(FanSpeed.class);
        FanSpeed highSpeed = mock(FanSpeed.class);

        when(lowSpeed.toString()).thenReturn("LOW");
        when(mediumSpeed.toString()).thenReturn("MEDIUM");
        when(highSpeed.toString()).thenReturn("HIGH");

        when(fanOffState.execute(any(), eq(FanAction.TOGGLE_POWER)))
                .thenAnswer(invocation -> {
                    Fan f = invocation.getArgument(0);
                    f.setState(fanOnState);

                    return new DeviceResult(true, FanAction.TOGGLE_POWER, "Fan on");
                });

        when(fanOnState.execute(any(), eq(FanAction.TOGGLE_POWER)))
                .thenAnswer(invocation -> {
                    Fan f = invocation.getArgument(0);
                    f.setState(fanOffState);

                    return new DeviceResult(true, FanAction.TOGGLE_POWER, "Fan off");
                });

        when(fanOnState.execute(any(), eq(FanAction.SET_SPEED_HIGH)))
                .thenAnswer(invocation -> {
                    Fan f = invocation.getArgument(0);
                    f.setSpeed(highSpeed);

                    return new DeviceResult(true, FanAction.SET_SPEED_HIGH, "High speed");
                });

        when(fanOffState.execute(any(), eq(FanAction.SET_SPEED_HIGH)))
                .thenReturn(
                        new DeviceResult(
                                false,
                                FanAction.SET_SPEED_HIGH,
                                "Cannot change speed while off"
                        )
                );

        Map<FanStateType, FanState> fanStates = new HashMap<>();
        fanStates.put(FanStateType.OFF, fanOffState);
        fanStates.put(FanStateType.ON, fanOnState);

        Map<String, FanSpeed> speeds = new HashMap<>();
        speeds.put("LOW", lowSpeed);
        speeds.put("MEDIUM", mediumSpeed);
        speeds.put("HIGH", highSpeed);

        fan = new Fan(
                "Ceiling Fan",
                "Living Room",
                fanOffState,
                fanStates,
                speeds,
                lowSpeed
        );

        // ---------------- THERMOSTAT ----------------

        ThermostatState offState = mock(ThermostatState.class);
        ThermostatState idleState = mock(ThermostatState.class);
        ThermostatState heatingState = mock(ThermostatState.class);
        ThermostatState coolingState = mock(ThermostatState.class);

        Temperature desired = new Temperature(70);
        Temperature ambient = new Temperature(68);

        ThermostatMode heatMode = mock(ThermostatMode.class);
        ThermostatMode coolMode = mock(ThermostatMode.class);
        ThermostatMode autoMode = mock(ThermostatMode.class);

        when(heatMode.getModeType()).thenReturn(ThermostatModeType.HEAT);
        when(coolMode.getModeType()).thenReturn(ThermostatModeType.COOL);
        when(autoMode.getModeType()).thenReturn(ThermostatModeType.AUTO);

        Map<ThermostatStateType, ThermostatState> thermostatStates = new HashMap<>();
        thermostatStates.put(ThermostatStateType.OFF, offState);
        thermostatStates.put(ThermostatStateType.IDLE, idleState);
        thermostatStates.put(ThermostatStateType.HEATING, heatingState);
        thermostatStates.put(ThermostatStateType.COOLING, coolingState);

        Map<ThermostatModeType, ThermostatMode> modes = new HashMap<>();
        modes.put(ThermostatModeType.HEAT, heatMode);
        modes.put(ThermostatModeType.COOL, coolMode);
        modes.put(ThermostatModeType.AUTO, autoMode);

        thermostat = new Thermostat(
                "Thermostat",
                "Hallway",
                offState,
                thermostatStates,
                autoMode,
                desired,
                ambient,
                modes
        );

        // ---------------- DOOR LOCK ----------------

        DoorLockState lockedState = mock(DoorLockState.class);
        DoorLockState unlockedState = mock(DoorLockState.class);

        when(lockedState.execute(any(), eq(DoorLockAction.TOGGLE_LOCK)))
                .thenAnswer(invocation -> {
                    DoorLock d = invocation.getArgument(0);
                    d.setState(unlockedState);

                    return new DeviceResult(true, DoorLockAction.TOGGLE_LOCK, "Unlocked");
                });

        when(unlockedState.execute(any(), eq(DoorLockAction.TOGGLE_LOCK)))
                .thenAnswer(invocation -> {
                    DoorLock d = invocation.getArgument(0);
                    d.setState(lockedState);

                    return new DeviceResult(true, DoorLockAction.TOGGLE_LOCK, "Locked");
                });

        Map<DoorLockStateType, DoorLockState> lockStates = new HashMap<>();
        lockStates.put(DoorLockStateType.LOCKED, lockedState);
        lockStates.put(DoorLockStateType.UNLOCKED, unlockedState);

        doorLock = new DoorLock(
                "Front Door",
                "Entry",
                lockedState,
                lockStates
        );
    }

    // =========================================================
    // LIGHT STATE MACHINE TESTS
    // =========================================================

    @Test
    void togglePower_OffToOn_ExpectedResult() {

        DeviceResult result = light.togglePower();

        assertTrue(result.success());
        assertEquals(LightStateType.ON, light.getState().getStateType());
    }

    @Test
    void togglePower_OnToOff_ExpectedResult() {

        light.togglePower();

        DeviceResult result = light.togglePower();

        assertTrue(result.success());
        assertEquals(LightStateType.OFF, light.getState().getStateType());
    }

    @Test
    void changeBrightness_ValidMinimumBrightness_ExpectedResult() {

        DeviceResult result = light.changeBrightness(10);

        assertTrue(result.success());
        assertEquals(10, light.getBrightness());
    }

    @Test
    void changeBrightness_ValidMaximumBrightness_ExpectedResult() {

        DeviceResult result = light.changeBrightness(100);

        assertTrue(result.success());
        assertEquals(100, light.getBrightness());
    }

    @Test
    void changeBrightness_BelowMinimum_RejectResult() {

        DeviceResult result = light.changeBrightness(9);

        assertFalse(result.success());
    }

    @Test
    void changeBrightness_AboveMaximum_RejectResult() {

        DeviceResult result = light.changeBrightness(101);

        assertFalse(result.success());
    }

    @Test
    void changeBrightness_NegativeValue_RejectResult() {

        DeviceResult result = light.changeBrightness(-1);

        assertFalse(result.success());
    }

    @Test
    void changeColor_ThenPowerCycle_SettingsRetained_ExpectedResult() {

        light.togglePower();

        light.changeBrightness(75);
        light.changeColor(120, 80, 200);

        light.togglePower();
        light.togglePower();

        assertEquals(75, light.getBrightness());

        assertArrayEquals(
                new int[]{120, 80, 200},
                light.getColor()
        );
    }

    // =========================================================
    // FAN TESTS
    // =========================================================

    @Test
    void togglePower_OffToOn_FanExpectedResult() {

        DeviceResult result = fan.togglePower();

        assertTrue(result.success());
        assertEquals(FanStateType.ON, fan.getState().getStateType());
    }

    @Test
    void togglePower_OnToOff_FanExpectedResult() {

        fan.togglePower();

        DeviceResult result = fan.togglePower();

        assertTrue(result.success());
        assertEquals(FanStateType.OFF, fan.getState().getStateType());
    }

    @Test
    void changeSpeed_WhileOn_ExpectedResult() {

        fan.togglePower();

        DeviceResult result = fan.changeSpeedHigh();

        assertTrue(result.success());
        assertEquals("HIGH", fan.getSpeed().toString());
    }

    @Test
    void changeSpeed_WhileOff_RejectResult() {

        DeviceResult result = fan.changeSpeedHigh();

        assertFalse(result.success());
    }

    @Test
    void changeSpeed_ThenPowerCycle_SpeedRetained_ExpectedResult() {

        fan.togglePower();
        fan.changeSpeedHigh();

        fan.togglePower();
        fan.togglePower();

        assertEquals("HIGH", fan.getSpeed().toString());
    }

    // =========================================================
    // THERMOSTAT TESTS
    // =========================================================

    @Test
    void setDesiredTemperature_MinimumBoundary_ExpectedResult() {

        DeviceResult result = thermostat.setDesiredTemperature(60);

        assertTrue(result.success());
        assertEquals(60, thermostat.getDesiredTemperature().getValue());
    }

    @Test
    void setDesiredTemperature_MaximumBoundary_ExpectedResult() {

        DeviceResult result = thermostat.setDesiredTemperature(80);

        assertTrue(result.success());
        assertEquals(80, thermostat.getDesiredTemperature().getValue());
    }

    @Test
    void setDesiredTemperature_BelowMinimum_RejectResult() {

        DeviceResult result = thermostat.setDesiredTemperature(59);

        assertFalse(result.success());
    }

    @Test
    void setDesiredTemperature_AboveMaximum_RejectResult() {

        DeviceResult result = thermostat.setDesiredTemperature(81);

        assertFalse(result.success());
    }

    @Test
    void setModeHeat_AnyActiveState_ExpectedResult() {

        DeviceResult result = thermostat.setModeHeat();

        assertTrue(result.success());

        assertEquals(
                ThermostatModeType.HEAT,
                thermostat.getAttributes().get("mode")
        );
    }

    @Test
    void setModeCool_AnyActiveState_ExpectedResult() {

        DeviceResult result = thermostat.setModeCool();

        assertTrue(result.success());

        assertEquals(
                ThermostatModeType.COOL,
                thermostat.getAttributes().get("mode")
        );
    }

    @Test
    void setModeAuto_AnyActiveState_ExpectedResult() {

        thermostat.setModeHeat();

        DeviceResult result = thermostat.setModeAuto();

        assertTrue(result.success());

        assertEquals(
                ThermostatModeType.AUTO,
                thermostat.getAttributes().get("mode")
        );
    }

    // =========================================================
    // DOOR LOCK TESTS
    // =========================================================

    @Test
    void toggleLock_LockedToUnlocked_ExpectedResult() {

        DeviceResult result = doorLock.toggleLock();

        assertTrue(result.success());

        assertEquals(
                DoorLockStateType.UNLOCKED,
                doorLock.getState().getStateType()
        );
    }

    @Test
    void toggleLock_UnlockedToLocked_ExpectedResult() {

        doorLock.toggleLock();

        DeviceResult result = doorLock.toggleLock();

        assertTrue(result.success());

        assertEquals(
                DoorLockStateType.LOCKED,
                doorLock.getState().getStateType()
        );
    }
}
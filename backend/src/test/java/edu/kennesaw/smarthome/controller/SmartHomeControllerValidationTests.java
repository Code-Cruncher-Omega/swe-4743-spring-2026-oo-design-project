package edu.kennesaw.smarthome.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kennesaw.smarthome.config.GlobalExceptionHandler;

import edu.kennesaw.smarthome.dto.DeviceActionRequest;
import edu.kennesaw.smarthome.dto.DeviceResult;
import edu.kennesaw.smarthome.service.AuditLog;
import edu.kennesaw.smarthome.service.PersistenceService;
import edu.kennesaw.smarthome.service.SmartHomeService;

class SmartHomeControllerValidationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SmartHomeService smartHomeService;
    private PersistenceService persistenceService;
    private AuditLog auditLog;

    @BeforeEach
    void setup() {
        smartHomeService = mock(SmartHomeService.class);
        persistenceService = mock(PersistenceService.class);
        auditLog = mock(AuditLog.class);
        SmartHomeController controller = new SmartHomeController(auditLog, persistenceService, smartHomeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createDevice_MissingRequiredFields_ReturnsBadRequest() throws Exception {
        String json = "{\"location\":\"Office\",\"deviceType\":\"LIGHT\"}";

        mockMvc.perform(post("/api/smarthome/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDevice_InvalidDeviceType_ReturnsBadRequest() throws Exception {
        String json = "{\"name\":\"Desk Light\",\"location\":\"Office\",\"deviceType\":\"INVALID\"}";

        mockMvc.perform(post("/api/smarthome/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void performDeviceAction_InvalidActionType_ReturnsBadRequest() throws Exception {
        String id = "00000000-0000-0000-0000-000000000000";
        when(smartHomeService.performDeviceAction(eq(id), any(DeviceActionRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid action type"));

        String json = "{\"action\":123,\"parameters\":[]}";

        mockMvc.perform(post("/api/smarthome/devices/" + id + "/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void performDeviceAction_RecordsAuditHistory() throws Exception {
        String id = "00000000-0000-0000-0000-000000000001";
        DeviceResult result = new DeviceResult(true, edu.kennesaw.smarthome.domain.device.light.LightAction.TOGGLE_POWER, "Light turned on successfully.");

        when(smartHomeService.performDeviceAction(eq(id), any(DeviceActionRequest.class))).thenReturn(result);

        String json = objectMapper.writeValueAsString(new DeviceActionRequest("TOGGLE_POWER"));

        mockMvc.perform(post("/api/smarthome/devices/" + id + "/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(auditLog).record(eq(id), eq("Light turned on successfully."));
    }

    @Test
    void performDeviceAction_InvalidBrightnessLow_ReturnsBadRequest() throws Exception {
        String id = "00000000-0000-0000-0000-000000000000";
        when(smartHomeService.performDeviceAction(eq(id), any(DeviceActionRequest.class)))
                .thenThrow(new IllegalArgumentException("Brightness must be between 10 and 100"));

        String json = "{\"action\":\"SET_BRIGHTNESS\",\"parameters\":[9]}";

        mockMvc.perform(post("/api/smarthome/devices/" + id + "/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void performDeviceAction_InvalidBrightnessHigh_ReturnsBadRequest() throws Exception {
        String id = "00000000-0000-0000-0000-000000000000";
        when(smartHomeService.performDeviceAction(eq(id), any(DeviceActionRequest.class)))
                .thenThrow(new IllegalArgumentException("Brightness must be between 10 and 100"));

        String json = "{\"action\":\"SET_BRIGHTNESS\",\"parameters\":[101]}";

        mockMvc.perform(post("/api/smarthome/devices/" + id + "/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void performDeviceAction_InvalidTemperatureLow_ReturnsBadRequest() throws Exception {
        String id = "00000000-0000-0000-0000-000000000000";
        when(smartHomeService.performDeviceAction(eq(id), any(DeviceActionRequest.class)))
                .thenThrow(new IllegalArgumentException("Temperature must be between 60 and 80"));

        String json = "{\"action\":\"SET_DESIRED_TEMPERATURE\",\"parameters\":[59]}";

        mockMvc.perform(post("/api/smarthome/devices/" + id + "/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void performDeviceAction_InvalidTemperatureHigh_ReturnsBadRequest() throws Exception {
        String id = "00000000-0000-0000-0000-000000000000";
        when(smartHomeService.performDeviceAction(eq(id), any(DeviceActionRequest.class)))
                .thenThrow(new IllegalArgumentException("Temperature must be between 60 and 80"));

        String json = "{\"action\":\"SET_DESIRED_TEMPERATURE\",\"parameters\":[81]}";

        mockMvc.perform(post("/api/smarthome/devices/" + id + "/actions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}

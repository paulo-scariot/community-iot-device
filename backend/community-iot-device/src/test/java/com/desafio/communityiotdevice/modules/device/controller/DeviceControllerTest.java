package com.desafio.communityiotdevice.modules.device.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.service.CommandService;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.device.dto.DeviceResponse;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommandService commandService;

    @MockBean
    private DeviceService deviceService;

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private UserService userService;

    private Device device;
    private DeviceRequest deviceRequest;
    private DeviceResponse deviceResponse;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        device = new Device(1, "teste", "teste", "teste", "teste", true, null, Collections.emptyList(), null);

        deviceRequest = new DeviceRequest();
        deviceRequest.setId(1);
        deviceRequest.setDescription("teste");
        deviceRequest.setManufacturer("teste");
        deviceRequest.setUrl("teste");
        deviceRequest.setStatus(true);

        deviceResponse = new DeviceResponse();
        deviceResponse.setId(1);
        deviceResponse.setDescription("teste");
        deviceResponse.setManufacturer("teste");
        deviceResponse.setUrl("teste");
        deviceResponse.setStatus(true);
    }

    @Test
    void testGetDevices_WhenCalledWithPagination_ThenReturnPaginatedDeviceResponse() throws Exception {

        // Given / Arrange
        List<DeviceResponse> devices = List.of(deviceResponse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<DeviceResponse> mockPage = new PageImpl<>(devices, pageable, devices.size());
        given(deviceService.getDevices("", 1, 10)).willReturn(mockPage);

        // When / Act
        ResultActions response = mockMvc.perform(get("/device")
                .queryParam("filter", "")
                .queryParam("page", "1")
                .queryParam("size", "10"));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size").value(mockPage.getSize()))
                .andExpect(jsonPath("$.totalElements").value(mockPage.getTotalElements()));
    }

    @Test
    void testFindById_WhenDeviceExists_ThenReturnDeviceResponse() throws Exception {

        // Given / Arrange
        try (MockedStatic<DeviceResponse> mocked = mockStatic(DeviceResponse.class)) {

            mocked.when(() -> DeviceResponse.of(device)).thenReturn(deviceResponse);
            given(deviceService.findById(anyInt())).willReturn(device);

            // When / Act
            ResultActions response = mockMvc.perform(get("/device/{id}", device.getId()));

            // Then / Assert
            response
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.description").value(device.getDescription()));
        }
    }

    @Test
    void testSave_WhenCalledWithDeviceRequest_ThenReturnDeviceResponse() throws Exception {

        // Given / Arrange
        given(deviceService.save(any(DeviceRequest.class))).willReturn(deviceResponse);

        // When / Act
        ResultActions response = mockMvc.perform(post("/device")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deviceRequest)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(device.getDescription()));
    }

    @Test
    void testUpdate_WhenCalledWithDeviceRequest_ThenReturnDeviceResponse() throws Exception {

        // Given / Arrange
        deviceRequest.setDescription("modificado");
        deviceResponse.setDescription("modificado");
        given(deviceService.findById(anyInt())).willReturn(device);
        given(deviceService.update(deviceRequest, deviceRequest.getId())).willReturn(deviceResponse);

        // When / Act
        ResultActions response = mockMvc.perform(put("/device/{id}", device.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deviceRequest)));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(deviceRequest.getDescription()));
    }

    @Test
    void testDelete_WhenDeviceExists_ThenReturnSucessResponse() throws Exception {

        // Given / Arrange
        SuccessResponse successResponse = SuccessResponse.create("Device with id 1 has been deleted");
        given(deviceService.delete(anyInt())).willReturn(successResponse);

        // When / Act
        ResultActions response = mockMvc.perform(delete("/device/{id}", device.getId()));

        // Then / Assert
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Device with id 1 has been deleted"));
    }

}
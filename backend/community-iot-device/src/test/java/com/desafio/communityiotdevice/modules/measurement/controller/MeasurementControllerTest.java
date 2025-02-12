package com.desafio.communityiotdevice.modules.measurement.controller;

import com.desafio.communityiotdevice.modules.command.service.CommandService;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.dto.MeasurementResponse;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandService commandService;

    @MockBean
    private DeviceService deviceService;

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private UserService userService;

    private MeasurementResponse measurementResponse;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        MockitoAnnotations.openMocks(this);
        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 1, 1);
        measurementResponse = new MeasurementResponse();
        measurementResponse.setId(1);
        measurementResponse.setCreatedAt(localDateTime);
        measurementResponse.setResult(10D);

    }

    @Test
    void testGetMeasurements_WhenCalledWithPagination_ThenReturnPaginatedMeasurementResponse() throws Exception {

        // Given / Arrange
        List<MeasurementResponse> commands = List.of(measurementResponse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<MeasurementResponse> mockPage = new PageImpl<>(commands, pageable, commands.size());
        given(measurementService.getMeasurements("", 1, 10)).willReturn(mockPage);

        // When / Act
        ResultActions response = mockMvc.perform(get("/measurement")
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

}
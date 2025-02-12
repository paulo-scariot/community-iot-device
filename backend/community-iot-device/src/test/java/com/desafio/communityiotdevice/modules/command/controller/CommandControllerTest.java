package com.desafio.communityiotdevice.modules.command.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.command.service.CommandService;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class CommandControllerTest {

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

    private Command command;
    private CommandRequest commandRequest;
    private CommandResponse commandResponse;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        MockitoAnnotations.openMocks(this);
        command = new Command(1, "teste", "teste", Collections.emptyList(), Collections.emptyList());

        commandRequest = new CommandRequest();
        commandRequest.setId(1);
        commandRequest.setDescription("teste");
        commandRequest.setCommand("teste");

        commandResponse = new CommandResponse();
        commandResponse.setId(1);
        commandResponse.setDescription("teste");
        commandResponse.setCommand("teste");
    }

    @Test
    void testGetCommands_WhenCalledWithPagination_ThenReturnPaginatedCommandResponse() throws Exception {

        // Given / Arrange
        List<CommandResponse> commands = List.of(commandResponse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<CommandResponse> mockPage = new PageImpl<>(commands, pageable, commands.size());
        given(commandService.getCommands("", 1, 10)).willReturn(mockPage);

        // When / Act
        ResultActions response = mockMvc.perform(get("/command")
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
    void testFindById_WhenCommandExists_ThenReturnCommandResponse() throws Exception {

        // Given / Arrange
        given(commandService.findById(anyInt())).willReturn(command);

        // When / Act
        ResultActions response = mockMvc.perform(get("/command/{id}", command.getId()));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.command").value(command.getCommand()));
    }

    @Test
    void testSave_WhenCalledWithCommandRequest_ThenReturnCommandResponse() throws Exception {

        // Given / Arrange
        given(commandService.save(any(CommandRequest.class))).willReturn(commandResponse);

        // When / Act
        ResultActions response = mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commandRequest)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.command").value(commandRequest.getCommand()))
                .andExpect(jsonPath("$.description").value(commandRequest.getDescription()));
    }

    @Test
    void testUpdate_WhenCalledWithCommandRequest_ThenReturnCommandResponse() throws Exception {

        // Given / Arrange
        commandResponse.setCommand("modificado");
        commandRequest.setCommand("modificado");
        given(commandService.findById(anyInt())).willReturn(command);
        given(commandService.update(commandRequest, commandRequest.getId())).willReturn(commandResponse);

        // When / Act
        ResultActions response = mockMvc.perform(put("/command/{id}", command.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commandRequest)));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.command").value(commandRequest.getCommand()))
                .andExpect(jsonPath("$.description").value(commandRequest.getDescription()));
    }

    @Test
    void testDelete_WhenCommandExists_ThenReturnSucessResponse() throws Exception {

        // Given / Arrange
        SuccessResponse successResponse = SuccessResponse.create("Command with id 1 has been deleted");
        given(commandService.delete(anyInt())).willReturn(successResponse);

        // When / Act
        ResultActions response = mockMvc.perform(delete("/command/{id}", command.getId()));

        // Then / Assert
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Command with id 1 has been deleted"));
    }
}
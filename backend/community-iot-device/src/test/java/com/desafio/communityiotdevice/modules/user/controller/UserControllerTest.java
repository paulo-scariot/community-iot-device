package com.desafio.communityiotdevice.modules.user.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.service.CommandService;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import com.desafio.communityiotdevice.modules.user.dto.LoginResponse;
import com.desafio.communityiotdevice.modules.user.dto.UserRequest;
import com.desafio.communityiotdevice.modules.user.dto.UserResponse;
import com.desafio.communityiotdevice.modules.user.model.RoleEnum;
import com.desafio.communityiotdevice.modules.user.model.User;
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
class UserControllerTest {

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

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;
    private LoginResponse loginResponse;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        MockitoAnnotations.openMocks(this);
        user = new User(1,"teste", "teste", RoleEnum.ADMIN, Collections.emptyList());

        userRequest = new UserRequest();
        userRequest.setId(1);
        userRequest.setUsername("teste");
        userRequest.setPassword("teste");
        userRequest.setRole(RoleEnum.ADMIN);

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setUsername("teste");
        userResponse.setRole(RoleEnum.ADMIN);

        loginResponse = new LoginResponse("teste");
    }

    @Test
    void testLogin_WhenValidUserRequest_ThenReturnLoginResponse() throws Exception {

        // Given / Arrange
        given(userService.login(userRequest)).willReturn(loginResponse);

        // When / Act
        ResultActions response = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequest)));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.credentials").value(loginResponse.getCredentials()));
    }

    @Test
    void testGetUsers_WhenCalledWithPagination_ThenReturnPaginatedUserResponse() throws Exception {

        // Given / Arrange
        List<UserResponse> users = List.of(userResponse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponse> mockPage = new PageImpl<>(users, pageable, users.size());
        given(userService.getUsers("", 1, 10)).willReturn(mockPage);

        // When / Act
        ResultActions response = mockMvc.perform(get("/user")
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
    void testFindById_WhenUserExists_ThenReturnUserResponse() throws Exception {

        // Given / Arrange
        given(userService.findById(anyInt())).willReturn(user);

        // When / Act
        ResultActions response = mockMvc.perform(get("/user/{id}", user.getId()));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void testSave_WhenCalledWithUserRequest_ThenReturnUserResponse() throws Exception {

        // Given / Arrange
        given(userService.save(any(UserRequest.class))).willReturn(userResponse);

        // When / Act
        ResultActions response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequest)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userRequest.getUsername()));
    }

    @Test
    void testUpdate_WhenCalledWithUserRequest_ThenReturnUserResponse() throws Exception {

        // Given / Arrange
        userResponse.setUsername("modificado");
        userRequest.setUsername("modificado");
        given(userService.findById(anyInt())).willReturn(user);
        given(userService.update(userRequest, userRequest.getId())).willReturn(userResponse);

        // When / Act
        ResultActions response = mockMvc.perform(put("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequest)));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userRequest.getUsername()));
    }

    @Test
    void testDelete_WhenUserExists_ThenReturnSucessResponse() throws Exception {

        // Given / Arrange
        SuccessResponse successResponse = SuccessResponse.create("User with id 1 has been deleted");
        given(userService.delete(anyInt())).willReturn(successResponse);

        // When / Act
        ResultActions response = mockMvc.perform(delete("/user/{id}", user.getId()));

        // Then / Assert
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with id 1 has been deleted"));
    }
}
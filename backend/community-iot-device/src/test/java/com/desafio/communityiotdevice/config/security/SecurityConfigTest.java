package com.desafio.communityiotdevice.config.security;

import com.desafio.communityiotdevice.modules.command.service.CommandService;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
class SecurityConfigTest {

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

    @Test
    void givenPublicEndpoint_whenAccessWithoutAuth_thenSuccess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void givenSecuredEndpoint_whenAccessWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenSecuredEndpoint_whenAccessWithInvalidCredentials_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/user")
                        .with(httpBasic("admin", "wrongpassword")))
                .andExpect(status().isUnauthorized());
    }
}
package com.desafio.communityiotdevice.modules.user.service;

import com.desafio.communityiotdevice.config.exception.CustomHttpException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.user.dto.LoginResponse;
import com.desafio.communityiotdevice.modules.user.dto.UserRequest;
import com.desafio.communityiotdevice.modules.user.dto.UserResponse;
import com.desafio.communityiotdevice.modules.user.model.RoleEnum;
import com.desafio.communityiotdevice.modules.user.model.User;
import com.desafio.communityiotdevice.modules.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
    @Mock
    private UserRepository userRepository;

    @Mock
    private DeviceService deviceService;

    @Mock
    private Command mockCommand;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Base64.Encoder encoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;
    private LoginResponse loginResponse;

    @BeforeEach
    public void setup() {
        // Arrange
        MockitoAnnotations.openMocks(this);

        user = new User("teste", "teste", RoleEnum.ADMIN, Collections.emptyList());

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
    void testLoadUserByUsername_WhenUserExists_ThenReturnUser() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(user);

        // When / Act
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        // Then / Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("teste");
    }

    @Test
    void testLoadUserByUsername_WhenUserIsNull_ThenThrowUsernameNotFoundException() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(null);

        // When / Act
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(user.getUsername());
        });

        // Then / Assert
        assertThat(exception.getMessage()).isEqualTo("User with username: teste not found");
    }

    @Test
    void testLogin_WhenValidUserRequest_ThenReturnLoginResponse() {

        try (MockedStatic<Base64> mocked = mockStatic(Base64.class)) {
            // Given / Arrange
            String expectedEncoded = "mockedBase64String";
            mocked.when(Base64::getEncoder).thenReturn(encoder);
            when(encoder.encodeToString(any())).thenReturn(expectedEncoded);

            given(userRepository.findByUsername(anyString())).willReturn(user);
            given(authenticationManager.authenticate(authentication)).willReturn(authentication);

            // When / Act
            LoginResponse login = userService.login(userRequest);

            // Then / Assert
            assertThat(login).isNotNull();
            assertThat(login.getCredentials()).isEqualTo(expectedEncoded);
        }
    }

    @Test
    void testLogin_WhenUserNotFound_ThenThrowCustomHttpException() {

        // Given / Arrange
        given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("Invalid credentials"));

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.login(userRequest);
        });

        // Then / Assert
        assertThat(exception.getMessage()).isEqualTo("Invalid username or password");
    }

    @Test
    void testFindById_WhenUserExists_ThenReturnUser() {

        // Given / Arrange
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));

        // When / Act
        User findUser = userService.findById(1);

        // Then / Assert
        assertThat(findUser).isNotNull();
        assertThat(findUser.getUsername()).isEqualTo("teste");
    }

    @Test
    void testFindUsername_WhenUserExists_ThenReturnUser() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(user);

        // When / Act
        User findUser = userService.findByUsername(user.getUsername());

        // Then / Assert
        assertThat(findUser).isNotNull();
        assertThat(findUser.getUsername()).isEqualTo("teste");
    }

    @Test
    void testGetUsers_WhenValidRequestWithNullFilter_ThenReturnPaginatedUserResponse() {

        // Given / Arrange
        List<User> users = List.of(user);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(users, pageable, users.size());
        given(userRepository.findAll(pageable)).willReturn(mockPage);

        // When / Act
        Page<UserResponse> page = userService.getUsers(null, 0, 10);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList())
                .anyMatch(command -> command.getUsername().equals("teste"));
    }

    @Test
    void testGetUsers_WhenValidRequestWithFilter_ThenReturnPaginatedUserResponse() {

        // Given / Arrange
        String filter = "teste2";
        user.setUsername(filter);
        List<User> commands = List.of(user);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(commands, pageable, commands.size());
        given(userRepository.findByUsernameContainingIgnoreCase(filter, pageable)).willReturn(mockPage);

        // When / Act
        Page<UserResponse> page = userService.getUsers(filter, 0, 10);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList())
                .anyMatch(user -> user.getUsername().equals("teste2"));
    }

    @Test
    void testGetUsers_WhenValidRequestWithEmptyFilter_ThenReturnPaginatedUserResponse() {

        // Given / Arrange
        List<User> users = List.of(user);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(users, pageable, users.size());
        given(userRepository.findAll(pageable)).willReturn(mockPage);

        // When / Act
        Page<UserResponse> page = userService.getUsers("", 0, 10);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList())
                .anyMatch(user -> user.getUsername().equals("teste"));
    }

    @Test
    void testSave_WhenValidUserRequest_ThenReturnUserResponse() {

        try (MockedStatic<UserResponse> mocked = mockStatic(UserResponse.class)) {
            // Given / Arrange
            userRequest.setUsername("teste2");
            mocked.when(() -> UserResponse.of(any(User.class))).thenReturn(userResponse);
            given(userRepository.save(any(User.class))).willAnswer(invocation -> {
                User save = invocation.getArgument(0);
                save.setId(1);
                return save;
            });
            given(userRepository.findByUsername("teste")).willReturn(user);
            given(userRepository.findByUsername("teste2")).willReturn(null);
            given(authentication.isAuthenticated()).willReturn(true);
            given(authentication.getPrincipal()).willReturn(user);
            given(securityContext.getAuthentication()).willReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // When / Act
            UserResponse userResponse = userService.save(userRequest);

            // Then / Assert
            assertThat(userResponse).isNotNull();
            assertThat(userResponse.getUsername()).isEqualTo("teste");
        }
    }

    @Test
    void testSave_WhenUserRequestUsernameAlreadyExists_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn(user);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("User with username: teste already exists");
    }

    @Test
    void testSave_WhenUserRoleIsUser_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        user.setRole(RoleEnum.USER);
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn(user);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("You are not allow to do this action!");

    }

    @Test
    void testSave_WhenUserIsNotAuthenticated_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(false);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("Username not found");

    }

    @Test
    void testSave_WhenAuthenticationIsNull_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(user);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("Username not found");

    }

    @Test
    void testSave_WhenUserIsNotInstanceofUserDetails_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(true);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("Username not found");

    }

    @Test
    void testSave_WhenUsernameIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        UserRequest userRequest1 = new UserRequest();
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn(user);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest1);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("The username cannot be empty");
    }

    @Test
    void testSave_WhenPasswordIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        UserRequest userRequest1 = new UserRequest();
        userRequest1.setUsername("teste");
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn(user);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest1);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("The password cannot be empty");
    }

    @Test
    void testSave_WhenRoleIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveUser() {

        // Given / Arrange
        UserRequest userRequest1 = new UserRequest();
        userRequest1.setUsername("teste");
        userRequest1.setPassword("teste");
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn(user);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.save(userRequest1);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("The role cannot be empty");
    }

    @Test
    void testUpdate_WhenValidUserRequestAndId_ThenReturnUserResponse() {

        try (MockedStatic<UserResponse> mocked = mockStatic(UserResponse.class)) {
            // Given / Arrange
            userRequest.setUsername("teste2");
            userResponse.setUsername("teste2");
            mocked.when(() -> UserResponse.of(any(User.class))).thenReturn(userResponse);
            given(bCryptPasswordEncoder.encode(anyString())).willReturn("encryptedPassword");
            given(userRepository.save(any(User.class))).willAnswer(invocation -> {
                User save = invocation.getArgument(0);
                save.setId(1);
                return save;
            });
            given(userRepository.findByUsername(anyString())).willReturn(user);
            given(userRepository.findById(anyInt())).willReturn(Optional.of(user)                                                                                                                                                                                                                               );
            given(authentication.isAuthenticated()).willReturn(true);
            given(authentication.getPrincipal()).willReturn(user);
            given(securityContext.getAuthentication()).willReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // When / Act
            UserResponse userResponse = userService.update(userRequest, 1);

            // Then / Assert
            assertThat(userResponse).isNotNull();
            assertThat(userResponse.getUsername()).isEqualTo("teste2");
        }
    }

    @Test
    void testUpdate_WhenIdIsNull_ThenThrowCustomHttpExceptionAndDoNotUpdateUser() {

        // Given / Arrange
        given(userRepository.findByUsername(anyString())).willReturn(user);
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn(user);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            userService.update(userRequest, null);
        });

        // Then / Assert
        then(userRepository).should(never()).save(any(User.class));
        assertThat(exception.getMessage()).isEqualTo("The user id cannot be empty");
    }

    @Test
    void testDelete_WhenUserExists_ThenReturnSucessMessage() {

        try (MockedStatic<UserResponse> mocked = mockStatic(UserResponse.class)) {
            // Given / Arrange
            userRequest.setUsername("teste2");
            userResponse.setUsername("teste2");
            mocked.when(() -> UserResponse.of(any(User.class))).thenReturn(userResponse);
            willDoNothing().given(userRepository).deleteById(anyInt());
            given(userRepository.findByUsername(anyString())).willReturn(user);
            given(userRepository.findById(anyInt())).willReturn(Optional.of(user)                                                                                                                                                                                                                               );
            given(authentication.isAuthenticated()).willReturn(true);
            given(authentication.getPrincipal()).willReturn(user);
            given(securityContext.getAuthentication()).willReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // When / Act
            SuccessResponse successResponse = userService.delete(1);

            // Then / Assert
            assertThat(successResponse).isNotNull();
            assertThat(successResponse.getMessage()).isEqualTo("User with id 1 has been deleted");
        }

    }

}
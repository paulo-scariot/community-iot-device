package com.desafio.communityiotdevice.modules.device.service;

import com.desafio.communityiotdevice.config.exception.CustomHttpException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.device.dto.DeviceResponse;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.repository.DeviceRepository;
import com.desafio.communityiotdevice.modules.user.model.RoleEnum;
import com.desafio.communityiotdevice.modules.user.model.User;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    private Device device;
    private DeviceRequest deviceRequest;
    private DeviceResponse deviceResponse;

    @BeforeEach
    public void setup() {
        // Arrange
        MockitoAnnotations.openMocks(this);

        device = new Device("teste", "teste", "teste", "teste", true, null, Collections.emptyList(), Collections.emptyList());

        deviceRequest = new DeviceRequest();
        deviceRequest.setIdentifier("teste");
        deviceRequest.setDescription("teste");
        deviceRequest.setManufacturer("teste");
        deviceRequest.setUrl("teste");
        deviceRequest.setStatus(true);
        deviceRequest.setCommands(Collections.emptyList());

        deviceResponse = new DeviceResponse();
        deviceResponse.setIdentifier("teste");
        deviceResponse.setDescription("teste");
        deviceResponse.setManufacturer("teste");
        deviceResponse.setUrl("teste");
        deviceResponse.setStatus(true);
        deviceResponse.setCommands(Collections.emptyList());

    }

    @Test
    void testFindById_WhenDeviceExists_ThenReturnDevice() {

        // Given / Arrange
        given(deviceRepository.findById(anyInt())).willReturn(Optional.of(device));

        // When / Act
        Device findDevice = deviceService.findById(1);

        // Then / Assert
        assertThat(findDevice).isNotNull();
        assertThat(findDevice.getIdentifier()).isEqualTo("teste");
    }

    @Test
    void testGetDevices_WhenValidRequestWithNullFilter_ThenReturnPaginatedDeviceResponse() {

        try (MockedStatic<DeviceResponse> mocked = mockStatic(DeviceResponse.class)) {
            // Given / Arrange
            mocked.when(() -> DeviceResponse.of(device)).thenReturn(deviceResponse);
            List<Device> devices = List.of(device);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Device> mockPage = new PageImpl<>(devices, pageable, devices.size());
            given(deviceRepository.findAll(pageable)).willReturn(mockPage);

            // When / Act
            Page<DeviceResponse> page = deviceService.getDevices(null, 0, 10);

            // Then / Assert
            assertThat(page).isNotNull();
            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.toList())
                    .anyMatch(device -> device.getIdentifier().equals("teste"));
        }
    }

    @Test
    void testGetDevices_WhenValidRequestWithFilter_ThenReturnPaginatedDeviceResponse() {

        try (MockedStatic<DeviceResponse> mocked = mockStatic(DeviceResponse.class)) {
            // Given / Arrange
            mocked.when(() -> DeviceResponse.of(device)).thenReturn(deviceResponse);
            String filter = "teste2";
            device.setIdentifier(filter);
            deviceResponse.setIdentifier(filter);
            List<Device> devices = List.of(device);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Device> mockPage = new PageImpl<>(devices, pageable, devices.size());
            given(deviceRepository.findByIdentifierContainingIgnoreCase(filter, pageable)).willReturn(mockPage);

            // When / Act
            Page<DeviceResponse> page = deviceService.getDevices(filter, 0, 10);

            // Then / Assert
            assertThat(page).isNotNull();
            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.toList())
                    .anyMatch(device -> device.getIdentifier().equals("teste2"));
        }
    }

    @Test
    void testGetDevices_WhenValidRequestWithEmptyFilter_ThenReturnPaginatedDeviceResponse() {

        try (MockedStatic<DeviceResponse> mocked = mockStatic(DeviceResponse.class)) {
            // Given / Arrange
            mocked.when(() -> DeviceResponse.of(device)).thenReturn(deviceResponse);
            List<Device> devices = List.of(device);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Device> mockPage = new PageImpl<>(devices, pageable, devices.size());
            given(deviceRepository.findAll(pageable)).willReturn(mockPage);

            // When / Act
            Page<DeviceResponse> page = deviceService.getDevices("", 0, 10);

            // Then / Assert
            assertThat(page).isNotNull();
            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.toList())
                    .anyMatch(device -> device.getIdentifier().equals("teste"));
        }
    }

    @Test
    void testSave_WhenValidRequest_ThenReturnDeviceResponse() {

        try (MockedStatic<DeviceResponse> mocked = mockStatic(DeviceResponse.class)) {
            // Given / Arrange
            mocked.when(() -> DeviceResponse.of(any(Device.class))).thenReturn(deviceResponse);
            given(userService.findUserByContext()).willReturn(user);
            given(deviceRepository.save(any(Device.class))).willAnswer(invocation -> {
                Device save = invocation.getArgument(0);
                save.setId(1);
                return save;
            });

            // When / Act
            DeviceResponse deviceResponse = deviceService.save(deviceRequest);

            // Then / Assert
            assertThat(deviceResponse).isNotNull();
            assertThat(deviceResponse.getIdentifier()).isEqualTo("teste");
        }
    }

    @Test
    void testSave_WhenIdentifierIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveDevice() {

        // Given / Arrange
        DeviceRequest deviceRequest1 = new DeviceRequest();
        deviceRequest1.setDescription("teste");

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            deviceService.save(deviceRequest1);
        });

        // Then / Assert
        then(deviceRepository).should(never()).save(any(Device.class));
        assertThat(exception.getMessage()).isEqualTo("The identifier cannot be empty");
    }

    @Test
    void testSave_WhenDescriptionIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveDevice() {

        // Given / Arrange
        DeviceRequest deviceRequest1 = new DeviceRequest();
        deviceRequest1.setIdentifier("teste");

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            deviceService.save(deviceRequest1);
        });

        // Then / Assert
        then(deviceRepository).should(never()).save(any(Device.class));
        assertThat(exception.getMessage()).isEqualTo("The description cannot be empty");

    }

    @Test
    void testSave_WhenManufacturerIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveDevice() {

        // Given / Arrange
        DeviceRequest deviceRequest1 = new DeviceRequest();
        deviceRequest1.setIdentifier("teste");
        deviceRequest1.setDescription("teste");

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            deviceService.save(deviceRequest1);
        });

        // Then / Assert
        then(deviceRepository).should(never()).save(any(Device.class));
        assertThat(exception.getMessage()).isEqualTo("The manufacturer cannot be empty");
    }

    @Test
    void testSave_WhenUrlIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveDevice() {

        // Given / Arrange
        DeviceRequest deviceRequest1 = new DeviceRequest();
        deviceRequest1.setIdentifier("teste");
        deviceRequest1.setDescription("teste");
        deviceRequest1.setManufacturer("teste");

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            deviceService.save(deviceRequest1);
        });

        // Then / Assert
        then(deviceRepository).should(never()).save(any(Device.class));
        assertThat(exception.getMessage()).isEqualTo("The url cannot be empty");
    }

    @Test
    void testUpdate_WhenUserIsAdminRole_ThenReturnDeviceResponse() {

        try (MockedStatic<DeviceResponse> mocked = mockStatic(DeviceResponse.class)) {
            // Given / Arrange
            mocked.when(() -> DeviceResponse.of(any(Device.class))).thenReturn(deviceResponse);
            device.setId(1);
            given(deviceRepository.save(any(Device.class))).willAnswer(invocation -> {
                Device save = invocation.getArgument(0);
                save.setId(1);
                return save;
            });
            given(userService.findUserByContext()).willReturn(user);
            given(deviceRepository.findById(anyInt())).willReturn(Optional.ofNullable(device));
            given(user.getRole()).willReturn(RoleEnum.ADMIN);

            // When / Act
            DeviceResponse deviceResponse1 = deviceService.update(deviceRequest, 1);

            // Then / Assert
            assertThat(deviceResponse1).isNotNull();
            assertThat(deviceResponse1.getIdentifier()).isEqualTo("teste");
        }
    }

    @Test
    void testUpdate_WhenUserIsNotOwner_ThenThrowCustomHttpExceptionAndDoNotUpdateDevice() {

        // Given / Arrange
        given(userService.findUserByContext()).willReturn(user);
        given(deviceRepository.findById(anyInt())).willReturn(Optional.ofNullable(device));
        given(user.getRole()).willReturn(RoleEnum.USER);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            deviceService.update(deviceRequest, 1);
        });

        // Then / Assert
        then(deviceRepository).should(never()).save(device);
        assertThat(exception.getMessage()).isEqualTo("User is not owner of device");
    }

    @Test
    void testUpdate_WhenUserIsOwner_ThenReturnDeviceResponse() {

        try (MockedStatic<DeviceResponse> mocked = mockStatic(DeviceResponse.class)) {
            // Given / Arrange
            mocked.when(() -> DeviceResponse.of(any(Device.class))).thenReturn(deviceResponse);
            device.setUser(user);
            given(deviceRepository.save(any(Device.class))).willAnswer(invocation -> {
                Device save = invocation.getArgument(0);
                save.setId(1);
                return save;
            });
            given(userService.findUserByContext()).willReturn(user);
            given(deviceRepository.findById(anyInt())).willReturn(Optional.ofNullable(device));

            // When / Act
            DeviceResponse deviceResponse1 = deviceService.update(deviceRequest, 1);

            // Then / Assert
            assertThat(deviceResponse1).isNotNull();
            assertThat(deviceResponse1.getIdentifier()).isEqualTo("teste");
        }
    }

    @Test
    void testUpdate_WhenRequestWithoutId_ThenThrowCustomHttpExceptionAndDoNotUpdateDevice() {

        // Given / Arrange

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            deviceService.update(deviceRequest, null);
        });

        // Then / Assert
        then(deviceRepository).should(never()).save(any(Device.class));
        assertThat(exception.getMessage()).isEqualTo("The device id cannot be empty");
    }

    @Test
    void testDelete_WhenUserIsAdminRole_ThenReturnSucessMessage() {

        // Given / Arrange
        device.setId(1);
        given(deviceRepository.findById(anyInt())).willReturn(Optional.of(device));
        given(userService.findUserByContext()).willReturn(user);
        willDoNothing().given(deviceRepository).deleteById(anyInt());
        given(user.getRole()).willReturn(RoleEnum.ADMIN);

        // When / Act
        SuccessResponse successResponse = deviceService.delete(device.getId());

        // Then / Assert
        then(deviceRepository).should(times(1)).deleteById(device.getId());
        assertThat(successResponse.getMessage()).isEqualTo("Device with id 1 has been deleted");
    }

    @Test
    void testDelete_WhenUserIsUserRoleAndNotOwner_ThenThrowCustomHttpExceptionAndDoNotDeleteDevice() {

        // Given / Arrange
        device.setId(1);
        given(deviceRepository.findById(anyInt())).willReturn(Optional.of(device));
        given(userService.findUserByContext()).willReturn(user);
        willDoNothing().given(deviceRepository).deleteById(anyInt());
        given(user.getRole()).willReturn(RoleEnum.USER);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            deviceService.delete(device.getId());
        });

        // Then / Assert
        then(deviceRepository).should(never()).deleteById(device.getId());
        assertThat(exception.getMessage()).isEqualTo("User is not owner of device");
    }

    @Test
    void testDelete_WhenUserIsUserOwner_ThenReturnSucessResponse() {

        // Given / Arrange
        device.setUser(user);
        device.setId(1);
        given(deviceRepository.findById(anyInt())).willReturn(Optional.of(device));
        given(userService.findUserByContext()).willReturn(user);
        willDoNothing().given(deviceRepository).deleteById(anyInt());
        given(user.getRole()).willReturn(RoleEnum.USER);

        // When / Act
        SuccessResponse successResponse = deviceService.delete(device.getId());

        // Then / Assert
        then(deviceRepository).should(times(1)).deleteById(device.getId());
        assertThat(successResponse.getMessage()).isEqualTo("Device with id 1 has been deleted");
    }

    @Test
    void testExistsByCommandId_WhenDeviceWithStatusTrueExists_ThenReturnTrue() {

        // Given / Arrange
        device.setId(1);
        given(deviceRepository.existsByCommandsId(anyInt())).willReturn(true);

        // When / Act
        Boolean b = deviceService.existsByCommandId(device.getId());

        // Then / Assert
        assertThat(b).isNotNull();
        assertThat(b).isEqualTo(true);
    }

    @Test
    void testGetAllDevicesActive_WhenActiveDevicesExist_ThenReturnListOfActiveDevices() {

        // Given / Arrange
        given(deviceRepository.findByStatusTrue()).willReturn(List.of(device));

        // When / Act
        List<Device> allDevicesActive = deviceService.getAllDevicesActive();

        // Then / Assert
        assertThat(allDevicesActive).isNotNull();
        assertThat(allDevicesActive.size()).isEqualTo(1);
        assertThat(allDevicesActive)
                .anyMatch(device -> device.getIdentifier().equals("teste"));
    }
}
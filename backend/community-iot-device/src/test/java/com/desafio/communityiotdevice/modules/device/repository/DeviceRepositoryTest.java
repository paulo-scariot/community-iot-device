package com.desafio.communityiotdevice.modules.device.repository;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.command.repository.CommandRepository;
import com.desafio.communityiotdevice.modules.device.model.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CommandRepository commandRepository;

    private Device device;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        MockitoAnnotations.openMocks(this);

        device = new Device("teste", "teste", "teste", "teste", true, null, Collections.emptyList(), null);
    }

    @Test
    void testFindById_WhenDeviceExists_ThenReturnDevice() {

        // Given / Arrange
        deviceRepository.save(device);

        // When / Act
        Device findDevice = deviceRepository.findById(device.getId()).get();

        // Then / Assert
        assertThat(findDevice).isNotNull();
        assertThat(findDevice.getId()).isEqualTo(device.getId());
    }

    @Test
    void testFindAll_WhenDevicesExists_ThenReturnDeviceList() {
        // Given / Arrange
        deviceRepository.save(device);

        // When / Act
        List<Device> devices = deviceRepository.findAll();

        // Then / Assert
        assertThat(devices).isNotNull();
        assertThat(devices.size()).isEqualTo(1);
        assertThat(devices).extracting(Device::getDescription).contains("teste");
        assertThat(devices).extracting(Device::getIdentifier).contains("teste");
    }

    @Test
    void testFindByIdentifierContainingIgnoreCase_WhenDeviceMatchesFilter_ThenReturnPaginatedDevice() {
        // Given / Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String filter = "teste";
        deviceRepository.save(device);

        // When / Act
        Page<Device> page = deviceRepository.findByIdentifierContainingIgnoreCase(filter, pageable);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList()).extracting(Device::getIdentifier).contains("teste");
    }

    @Test
    void testExistsByCommandsId_WhenDeviceMatchesFilter_ThenReturnPaginatedDevice() {

        // Given / Arrange
        Command command = new Command("teste", "teste", Collections.emptyList(), Collections.emptyList());
        commandRepository.save(command);
        device.setCommands(List.of(command));
        deviceRepository.save(device);

        // When / Act
        Boolean b = deviceRepository.existsByCommandsId(command.getId());

        // Then / Assert
        assertThat(b).isNotNull();
        assertThat(b).isEqualTo(true);

    }

    @Test
    void testFindByStatusTrue_WhenStatusTrueDevicesExists_ThenReturnDeviceList() {
        // Given / Arrange
        deviceRepository.save(device);

        // When / Act
        List<Device> devices = deviceRepository.findByStatusTrue();

        // Then / Assert
        assertThat(devices).isNotNull();
        assertThat(devices.size()).isEqualTo(1);
        assertThat(devices).extracting(Device::getIdentifier).contains("teste");
    }

    @Test
    void testSave_WhenDeviceIsSaved_ThenReturnSavedDevice() {
        // Given / Arrange

        // When / Act
        Device save = deviceRepository.save(device);

        // Then / Assert
        assertThat(save).isNotNull();
        assertThat(save.getId()).isGreaterThan(0);
    }

    @Test
    void testSave_WhenDeviceIsUpdated_ThenReturnUpdatedDevice() {

        // Given / Arrange
        deviceRepository.save(device);

        // When / Act
        Device findDevice = deviceRepository.findById(device.getId()).get();
        findDevice.setIdentifier("teste1111");
        Device updated = deviceRepository.save(findDevice);

        // Then / Assert
        assertThat(updated).isNotNull();
        assertThat(updated.getIdentifier()).isEqualTo("teste1111");
    }

    @Test
    void testDeleteById_WhenDeviceExists_ThenDeleteDeviceAndReturnEmptyOptionalDevice() {

        // Given / Arrange
        deviceRepository.save(device);

        // When / Act
        deviceRepository.deleteById(device.getId());
        Optional<Device> byId = deviceRepository.findById(device.getId());

        // Then / Assert
        assertThat(byId).isEmpty();
    }

}
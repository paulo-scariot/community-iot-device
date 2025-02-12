package com.desafio.communityiotdevice.modules.measurement.repository;

import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.repository.DeviceRepository;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MeasurementRepositoryTest {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    private Device device;

    private Measurement measurement;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        MockitoAnnotations.openMocks(this);
        device = new Device("device", "teste", "teste", "teste", true, null, Collections.emptyList(), Collections.emptyList());
        deviceRepository.save(device);
        measurement = new Measurement(LocalDateTime.now(), 10D, device, null);
    }

    @Test
    void testFindById_WhenMeasurementExists_ThenReturnMeasurement() {

        // Given / Arrange
        measurement = measurementRepository.save(measurement);

        // When / Act
        Measurement findMeasurement = measurementRepository.findById(measurement.getId()).get();

        // Then / Assert
        assertThat(findMeasurement).isNotNull();
        assertThat(findMeasurement.getId()).isGreaterThan(0);
    }

    @Test
    void testFindLastResultsByDevice_WhenCalledWithPagination_ThenReturnPaginatedMeasurement() {
        // Given / Arrange
        Pageable pageable = PageRequest.of(0, 10);
        measurementRepository.save(measurement);

        // When / Act
        Page<Measurement> page = measurementRepository.findLastResultsByDevice(pageable);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList()).extracting(Measurement::getResult).contains(10D);
    }

    @Test
    void testFindLastResultsByDeviceAndIdentifierContainingIgnoreCase_WhenCalledWithPaginationAndFilter_ThenReturnPaginatedMeasurement() {
        // Given / Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String filter = "device";
        measurementRepository.save(measurement);

        // When / Act
        Page<Measurement> page = measurementRepository.findLastResultsByDeviceAndIdentifierContainingIgnoreCase(filter, pageable);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList()).extracting(Measurement::getResult).contains(10D);
    }

    @Test
    void testSave_WhenMeasurementIsSaved_ThenReturnMeasurement() {
        // Given / Arrange

        // When / Act
        Measurement save = measurementRepository.save(measurement);

        // Then / Assert
        assertThat(save).isNotNull();
        assertThat(save.getId()).isGreaterThan(0);
    }
}
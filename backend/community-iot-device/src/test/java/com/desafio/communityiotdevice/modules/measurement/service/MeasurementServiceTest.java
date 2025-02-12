package com.desafio.communityiotdevice.modules.measurement.service;

import com.desafio.communityiotdevice.modules.measurement.dto.MeasurementResponse;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import com.desafio.communityiotdevice.modules.measurement.repository.MeasurementRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @InjectMocks
    private MeasurementService measurementService;

    private Measurement measurement;
    private MeasurementResponse measurementResponse;

    @BeforeEach
    public void setup() {
        // Arrange
        MockitoAnnotations.openMocks(this);

        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 1, 1);
        measurement = new Measurement(localDateTime, 10D, null, null);
        measurementResponse = new MeasurementResponse();
        measurementResponse.setId(1);
        measurementResponse.setResult(10D);
        measurementResponse.setCreatedAt(localDateTime);
    }

    @Test
    void testGetMeasurements_WhenValidRequestWithNullFilter_ThenReturnPaginatedMeasurementResponse() {

        try (MockedStatic<MeasurementResponse> mocked = mockStatic(MeasurementResponse.class)) {
            // Given / Arrange
            mocked.when(() -> MeasurementResponse.of(measurement)).thenReturn(measurementResponse);
            List<Measurement> commands = List.of(measurement);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Measurement> mockPage = new PageImpl<>(commands, pageable, commands.size());
            given(measurementRepository.findLastResultsByDevice(pageable)).willReturn(mockPage);

            // When / Act
            Page<MeasurementResponse> page = measurementService.getMeasurements(null, 0, 10);

            // Then / Assert
            assertThat(page).isNotNull();
            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.toList())
                    .anyMatch(measurement -> measurement.getResult().equals(10D));
        }
    }

    @Test
    void testGetMeasurements_WhenValidRequestWithFilter_ThenReturnPaginatedMeasurementResponse() {

        try (MockedStatic<MeasurementResponse> mocked = mockStatic(MeasurementResponse.class)) {
            // Given / Arrange
            mocked.when(() -> MeasurementResponse.of(measurement)).thenReturn(measurementResponse);
            String filter = "device";
            List<Measurement> commands = List.of(measurement);
            Pageable pageable = PageRequest.of(0, 10);
            Page<Measurement> mockPage = new PageImpl<>(commands, pageable, commands.size());
            given(measurementRepository.findLastResultsByDeviceAndIdentifierContainingIgnoreCase(filter, pageable)).willReturn(mockPage);

            // When / Act
            Page<MeasurementResponse> page = measurementService.getMeasurements(filter, 0, 10);

            // Then / Assert
            assertThat(page).isNotNull();
            assertThat(page.getTotalElements()).isEqualTo(1);
            assertThat(page.toList())
                    .anyMatch(measurement -> measurement.getResult().equals(10D));
        }
    }

    @Test
    void testSave_WhenValidMeasurement_ThenCallSaveOnce() {

        try (MockedStatic<MeasurementResponse> mocked = mockStatic(MeasurementResponse.class)) {
            // Given / Arrange
            mocked.when(() -> MeasurementResponse.of(any(Measurement.class))).thenReturn(measurementResponse);
            given(measurementRepository.findById(anyInt())).willReturn(Optional.of(measurement));
            given(measurementRepository.save(any(Measurement.class))).willAnswer(invocation -> {
                Measurement save = invocation.getArgument(0);
                save.setId(1);
                return save;
            });

            // When / Act
            measurementService.save(measurement);

            // Then / Assert
            then(measurementRepository).should(times(1)).save(measurement);
        }
    }

}
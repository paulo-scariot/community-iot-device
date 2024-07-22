package com.desafio.communityiotdevice.modules.measurement.service;

import com.desafio.communityiotdevice.modules.measurement.dto.MeasurementResponse;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import com.desafio.communityiotdevice.modules.measurement.repository.MeasurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public Page<MeasurementResponse> getMeasurements(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Measurement> measurementPage;

        if (filter != null && !isEmpty(filter)) {
            measurementPage = measurementRepository.findLastResultsByDeviceAndIdentifierContainingIgnoreCase(filter, pageable);
        } else {
            measurementPage = measurementRepository.findLastResultsByDevice(pageable);
        }
        return measurementPage.map(MeasurementResponse::of);
    }

    public void save(Measurement measurement) {
        Measurement save = measurementRepository.save(measurement);
        MeasurementResponse.of(save);
    }

}

package com.desafio.communityiotdevice.modules.measurement.repository;

import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    @Query(value = "SELECT m.* FROM Measurements m " +
            "WHERE m.id IN (SELECT MAX(m2.id) FROM Measurements m2 GROUP BY m2.device_id, m2.command_id)",
            nativeQuery = true)
    Page<Measurement> findLastResultsByDevice(Pageable pageable);

    @Query(value = "SELECT m.* FROM Measurements m " +
            "LEFT JOIN Devices d ON m.device_id = d.id " +
            "WHERE d.identifier LIKE %:filter% " +
            "AND m.id IN (SELECT MAX(m2.id) FROM Measurements m2 GROUP BY m2.device_id, m2.command_id)",
            nativeQuery = true)
    Page<Measurement> findLastResultsByDeviceAndIdentifierContainingIgnoreCase(String filter, Pageable pageable);

}

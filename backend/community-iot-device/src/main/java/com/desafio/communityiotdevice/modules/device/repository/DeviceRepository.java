package com.desafio.communityiotdevice.modules.device.repository;

import com.desafio.communityiotdevice.modules.device.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    Page<Device> findByIdentifierContainingIgnoreCase(String filter, Pageable pageable);

    Boolean existsByCommandsId(Integer id);
}

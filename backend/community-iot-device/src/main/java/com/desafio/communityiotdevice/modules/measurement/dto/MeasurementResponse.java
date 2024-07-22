package com.desafio.communityiotdevice.modules.measurement.dto;

import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.device.dto.DeviceResponse;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeasurementResponse {

    private Integer id;
    private LocalDateTime createdAt;
    private Double result;
    private DeviceResponse device;
    private CommandResponse command;

    public static MeasurementResponse of(Measurement measurement) {
        MeasurementResponse response = new MeasurementResponse();
        response.setId(measurement.getId());
        response.setCreatedAt(measurement.getCreatedAt());
        response.setResult(measurement.getResult());
        response.setDevice(DeviceResponse.of(measurement.getDevice()));
        response.setCommand(CommandResponse.of(measurement.getCommand()));
        return response;
    }
}

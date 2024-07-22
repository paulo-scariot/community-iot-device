package com.desafio.communityiotdevice.modules.device.dto;

import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import lombok.Data;

import java.util.List;

@Data
public class DeviceRequest {

    private Integer id;
    private String identifier;
    private String description;
    private String manufacturer;
    private String url;
    private Boolean status;
    private List<CommandRequest> commands;

}

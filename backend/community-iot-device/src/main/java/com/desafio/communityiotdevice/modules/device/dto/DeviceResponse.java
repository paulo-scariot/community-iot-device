package com.desafio.communityiotdevice.modules.device.dto;

import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.user.dto.UserResponse;
import lombok.Data;

import java.util.List;

@Data
public class DeviceResponse {

    private Integer id;
    private String identifier;
    private String description;
    private String manufacturer;
    private String url;
    private Boolean status;
    private UserResponse user;
    private List<CommandResponse> commands;

    public static DeviceResponse of(Device device) {
        DeviceResponse deviceResponse = new DeviceResponse();
        deviceResponse.setId(device.getId());
        deviceResponse.setIdentifier(device.getIdentifier());
        deviceResponse.setDescription(device.getDescription());
        deviceResponse.setManufacturer(device.getManufacturer());
        deviceResponse.setUrl(device.getUrl());
        deviceResponse.setStatus(device.getStatus());
        deviceResponse.setUser(UserResponse.of(device.getUser()));
        deviceResponse.setCommands(device.getCommands().stream().map(CommandResponse::of).toList());
        return deviceResponse;
    }
}
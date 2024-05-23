package com.desafio.communityiotdevice.modules.device.dto;

import lombok.Data;

@Data
public class DeviceRequest {

    private String identifier;
    private String description;
    private String manufacturer;
    private String url;

}

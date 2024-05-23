package com.desafio.communityiotdevice.modules.parameter.dto;

import lombok.Data;

@Data
public class ParameterRequest {

    private String name;
    private String description;
    private Integer commandId;

}